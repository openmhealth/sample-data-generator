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

package org.openmhealth.data.generator.configuration;

import org.openmhealth.data.generator.domain.RealValueRandomVariable;
import org.openmhealth.data.generator.domain.RealValueRandomVariableTrend;
import org.openmhealth.data.generator.domain.MeasureGenerationRequest;
import org.openmhealth.data.generator.domain.MeasureGroup;
import org.openmhealth.data.generator.service.DataPointGenerationService;
import org.openmhealth.data.generator.service.DataPointWritingService;
import org.openmhealth.data.generator.service.MeasureGenerationService;
import org.openmhealth.schema.pojos.DataPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;


/**
 * @author Emerson Farrugia
 */
@Configuration
@PropertySource("classpath:application.yaml")
@ComponentScan(
        basePackages = "org.openmhealth",
        excludeFilters = {@ComponentScan.Filter(value = EnableAutoConfiguration.class)})
@EnableAutoConfiguration
@EnableConfigurationProperties
public class Application {

    @Autowired
    private MeasureGenerationService measureGenerationService;

    @Autowired
    @Qualifier("bloodPressureDataPointGenerationServiceImpl")
    private DataPointGenerationService dataPointGenerationService;

    @Autowired
//    @Qualifier("consoleDataPointWritingServiceImpl")
    @Qualifier("fileSystemDataPointWritingServiceImpl")
    private DataPointWritingService dataPointWritingService;


    public static void main(String[] args) throws IOException {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);

        Application application = applicationContext.getBean(Application.class);
        application.run();
    }

    public void run() throws IOException {

        RealValueRandomVariable systolicRandomVariable = new RealValueRandomVariable(3, 100d, 140d);
        RealValueRandomVariable diastolicRandomVariable = new RealValueRandomVariable(3, 60d, 100d);

        RealValueRandomVariableTrend systolicTrend = new RealValueRandomVariableTrend(systolicRandomVariable, 110, 130);
        RealValueRandomVariableTrend diastolicTrend = new RealValueRandomVariableTrend(diastolicRandomVariable, 70, 90);

        MeasureGenerationRequest request = new MeasureGenerationRequest();
        request.setStartDateTime(OffsetDateTime.now().minusDays(2));
        request.setEndDateTime(OffsetDateTime.now().minusDays(1));
        request.setMeanInterPointDuration(Duration.ofHours(6));
        request.addMeasureValueTrend("systolic", systolicTrend);
        request.addMeasureValueTrend("diastolic", diastolicTrend);

        Iterable<MeasureGroup> measureGroups = measureGenerationService.generateMeasureGroups(request);

        Iterable<DataPoint> dataPoints = dataPointGenerationService.generateDataPoints(measureGroups);

        dataPointWritingService.writeDataPoints(dataPoints);
    }
}
