/*
 * Copyright 2014 Open mHealth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openmhealth.data.generator;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import org.openmhealth.data.generator.configuration.DataGenerationSettings;
import org.openmhealth.data.generator.domain.MeasureGenerationRequest;
import org.openmhealth.data.generator.domain.TimestampedValueGroup;
import org.openmhealth.data.generator.service.DataPointGenerator;
import org.openmhealth.data.generator.service.DataPointWritingService;
import org.openmhealth.data.generator.service.TimestampedValueGroupGenerationService;
import org.openmhealth.schema.domain.omh.DataPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;


/**
 * This application loads a data generation fixture from application.yml and creates data points according to that
 * fixture. The data points are then either written to the console or to a file, as configured.
 *
 * @author Emerson Farrugia
 */
@SpringBootApplication
@EnableConfigurationProperties
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    private DataGenerationSettings dataGenerationSettings;

    @Autowired
    private Validator validator;

    @Autowired
    private List<DataPointGenerator> dataPointGenerators = new ArrayList<>();

    @Autowired
    private TimestampedValueGroupGenerationService valueGroupGenerationService;

    @Autowired
    private DataPointWritingService dataPointWritingService;

    private Map<String, DataPointGenerator<?>> dataPointGeneratorMap = new HashMap<>();


    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);

        Application application = applicationContext.getBean(Application.class);
        application.run();
    }

    @PostConstruct
    public void initializeDataPointGenerators() {

        for (DataPointGenerator generator : dataPointGenerators) {
            dataPointGeneratorMap.put(generator.getName(), generator);
        }
    }

    public void run() throws Exception {

        setMeasureGenerationRequestDefaults();

        if (!areMeasureGenerationRequestsValid()) {
            return;
        }

        long totalWritten = 0;

        for (MeasureGenerationRequest request : dataGenerationSettings.getMeasureGenerationRequests()) {

            Iterable<TimestampedValueGroup> valueGroups = valueGroupGenerationService.generateValueGroups(request);
            DataPointGenerator<?> dataPointGenerator = dataPointGeneratorMap.get(request.getGeneratorName());

            Iterable<? extends DataPoint<?>> dataPoints = dataPointGenerator.generateDataPoints(valueGroups);

            long written = dataPointWritingService.writeDataPoints(dataPoints);
            totalWritten += written;

            log.info("The '{}' generator has written {} data point(s).", dataPointGenerator.getName(), written);
        }

        log.info("A total of {} data point(s) have been written.", totalWritten);
    }


    private void setMeasureGenerationRequestDefaults() {

        for (MeasureGenerationRequest request : dataGenerationSettings.getMeasureGenerationRequests()) {

            if (request.getStartDateTime() == null) {
                request.setStartDateTime(dataGenerationSettings.getStartDateTime());
            }

            if (request.getEndDateTime() == null) {
                request.setEndDateTime(dataGenerationSettings.getEndDateTime());
            }

            if (request.getMeanInterPointDuration() == null) {
                request.setMeanInterPointDuration(dataGenerationSettings.getMeanInterPointDuration());
            }

            if (request.isSuppressNightTimeMeasures() == null) {
                request.setSuppressNightTimeMeasures(dataGenerationSettings.isSuppressNightTimeMeasures());
            }
        }
    }

    /**
     * @return true if the requests are valid, false otherwise
     */
    private boolean areMeasureGenerationRequestsValid() {

        List<MeasureGenerationRequest> requests = dataGenerationSettings.getMeasureGenerationRequests();
        Joiner joiner = Joiner.on(", ");

        for (int i = 0; i < requests.size(); i++) {
            MeasureGenerationRequest request = requests.get(i);

            Set<ConstraintViolation<MeasureGenerationRequest>> constraintViolations = validator.validate(request);

            if (!constraintViolations.isEmpty()) {
                log.error("The measure generation request with index {} is not valid.", i);
                log.error(request.toString());
                log.error(constraintViolations.toString());
                return false;
            }

            if (!dataPointGeneratorMap.containsKey(request.getGeneratorName())) {
                log.error("The data generator '{}' in request {} doesn't exist.", request.getGeneratorName(), i);
                log.error(request.toString());
                log.error("The allowed data generators are: {}", joiner.join(dataPointGeneratorMap.keySet()));
                return false;
            }

            DataPointGenerator<?> generator = dataPointGeneratorMap.get(request.getGeneratorName());

            Set<String> specifiedTrendKeys = request.getTrends().keySet();
            Set<String> requiredTrendKeys = generator.getRequiredValueGroupKeys();

            if (!specifiedTrendKeys.containsAll(requiredTrendKeys)) {
                log.error("Request {} for generator '{}' is missing required trend keys.", i, generator.getName());
                log.error("The generator requires the following missing keys: {}.",
                        joiner.join(Sets.difference(requiredTrendKeys, specifiedTrendKeys)));
                return false;
            }

            Set<String> supportedTrendKeys = generator.getSupportedValueGroupKeys();

            if (!supportedTrendKeys.containsAll(specifiedTrendKeys)) {
                log.warn("Request {} for generator '{}' specifies unsupported trend keys.", i, generator.getName());
                log.warn("The generator supports the following keys: {}.", joiner.join(supportedTrendKeys));
                log.warn("The following keys are being ignored: {}.",
                        joiner.join(Sets.difference(specifiedTrendKeys, supportedTrendKeys)));
            }
        }

        return true;
    }
}