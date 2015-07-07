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

import com.google.common.collect.Iterables;
import org.openmhealth.data.generator.domain.MeasureGenerationRequest;
import org.openmhealth.data.generator.domain.MeasureGroup;
import org.openmhealth.data.generator.domain.RealValueRandomVariable;
import org.openmhealth.data.generator.domain.RealValueRandomVariableTrend;
import org.openmhealth.data.generator.service.DataPointGenerationService;
import org.openmhealth.data.generator.service.DataPointWritingService;
import org.openmhealth.data.generator.service.MeasureGenerationService;
import org.openmhealth.schema.domain.omh.BloodPressure;
import org.openmhealth.schema.domain.omh.BodyWeight;
import org.openmhealth.schema.domain.omh.DataPoint;
import org.openmhealth.schema.domain.omh.StepCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Emerson Farrugia
 */
@SpringBootApplication
@EnableConfigurationProperties
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    private MeasureGenerationService measureGenerationService;

    @Autowired
    private DataPointGenerationService<BloodPressure> bloodPressureDataPointGenerationService;

    @Autowired
    private DataPointGenerationService<BodyWeight> bodyWeightDataPointGenerationService;

    @Autowired
    private DataPointGenerationService<StepCount> stepCountDataPointGenerationService;

    @Autowired
    @Qualifier("consoleDataPointWritingServiceImpl")
//    @Qualifier("fileSystemDataPointWritingServiceImpl")
    private DataPointWritingService dataPointWritingService;

    public static void main(String[] args) throws IOException {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);

        Application application = applicationContext.getBean(Application.class);
        application.run();
    }

    public void run() throws IOException {

        OffsetDateTime startDateTime = OffsetDateTime.now().minusDays(3);
        OffsetDateTime endDateTime = OffsetDateTime.now().minusDays(1);

        List<DataPoint> dataPoints = new ArrayList<>();

        // uncomment as needed
        Iterables.addAll(dataPoints, newBloodPressureDataPoints(startDateTime, endDateTime, 110, 110, 70, 70));
//        Iterables.addAll(dataPoints, newBodyWeightDataPoints(startDateTime, endDateTime, 55, 60));
        //              Iterables.addAll(dataPoints, newStepCountDataPoints(startDateTime, endDateTime, 120, 120, 90,
        // 90));

        dataPointWritingService.writeDataPoints(dataPoints);

        log.info("A total of {} data point(s) have been generated.", dataPoints.size());
    }

    public Iterable<DataPoint<BloodPressure>> newBloodPressureDataPoints(OffsetDateTime startDateTime,
                                                                         OffsetDateTime endDateTime,
                                                                         double systolicStart, double systolicEnd, double diastolicStart, double diastolicEnd) {

        // TODO use limits that reflect parameters
        RealValueRandomVariable systolicRandomVariable = new RealValueRandomVariable(3, 100d, 140d);
        RealValueRandomVariable diastolicRandomVariable = new RealValueRandomVariable(3, 60d, 80d);

        RealValueRandomVariableTrend systolicTrend =
                new RealValueRandomVariableTrend(systolicRandomVariable, systolicStart, systolicEnd);
        RealValueRandomVariableTrend diastolicTrend =
                new RealValueRandomVariableTrend(diastolicRandomVariable, diastolicStart, diastolicEnd);

        MeasureGenerationRequest request = new MeasureGenerationRequest();
        request.setStartDateTime(startDateTime);
        request.setEndDateTime(endDateTime);
        request.setMeanInterPointDuration(Duration.ofHours(24));
        request.addMeasureValueTrend("systolic", systolicTrend);
        request.addMeasureValueTrend("diastolic", diastolicTrend);

        Iterable<MeasureGroup> measureGroups = measureGenerationService.generateMeasureGroups(request);

        return bloodPressureDataPointGenerationService.generateDataPoints(measureGroups);
    }

    public Iterable<DataPoint<BodyWeight>> newBodyWeightDataPoints(OffsetDateTime startDateTime,
                                                                   OffsetDateTime endDateTime,
                                                                   double weightStart, double weightEnd) {

        double minimum = Math.min(weightStart, weightEnd) - 5;
        double maximum = Math.max(weightStart, weightEnd) + 5;
        RealValueRandomVariable weightRandomVariable = new RealValueRandomVariable(0.1, minimum, maximum);

        RealValueRandomVariableTrend weightTrend =
                new RealValueRandomVariableTrend(weightRandomVariable, weightStart, weightEnd);

        MeasureGenerationRequest request = new MeasureGenerationRequest();
        request.setStartDateTime(startDateTime);
        request.setEndDateTime(endDateTime);
        request.setMeanInterPointDuration(Duration.ofHours(24));
        request.addMeasureValueTrend("weight", weightTrend);

        Iterable<MeasureGroup> measureGroups = measureGenerationService.generateMeasureGroups(request);

        return bodyWeightDataPointGenerationService.generateDataPoints(measureGroups);
    }

    public Iterable<DataPoint<StepCount>> newStepCountDataPoints(OffsetDateTime startDateTime,
                                                                 OffsetDateTime endDateTime,
                                                                 double durationStart, double durationEnd, double stepsPerMinStart, double stepsPerMinEnd) {

        // TODO use limits that reflect parameters
        RealValueRandomVariable durationInSecRandomVariable = new RealValueRandomVariable(100, 10, 1800);
        RealValueRandomVariable stepsPerMinRandomVariable = new RealValueRandomVariable(15, 30, 125);

        RealValueRandomVariableTrend durationInSecTrend =
                new RealValueRandomVariableTrend(durationInSecRandomVariable, durationStart, durationEnd);
        RealValueRandomVariableTrend stepsPerMinTrend =
                new RealValueRandomVariableTrend(stepsPerMinRandomVariable, stepsPerMinStart, stepsPerMinEnd);

        MeasureGenerationRequest request = new MeasureGenerationRequest();
        request.setStartDateTime(startDateTime);
        request.setEndDateTime(endDateTime);
        request.setMeanInterPointDuration(Duration.ofMinutes(30));
        request.addMeasureValueTrend("durationInSec", durationInSecTrend);
        request.addMeasureValueTrend("stepsPerMin", stepsPerMinTrend);

        Iterable<MeasureGroup> measureGroups = measureGenerationService.generateMeasureGroups(request);

        return stepCountDataPointGenerationService.generateDataPoints(measureGroups);
    }

    // TODO add support for heart rate and physical activity
}
