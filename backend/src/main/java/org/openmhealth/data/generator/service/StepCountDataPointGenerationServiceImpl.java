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

package org.openmhealth.data.generator.service;

import org.openmhealth.data.generator.domain.MeasureGroup;
import org.openmhealth.schema.domain.omh.DurationUnitValue;
import org.openmhealth.schema.domain.omh.StepCount;
import org.springframework.stereotype.Service;

import static org.openmhealth.schema.domain.omh.DurationUnit.SECOND;
import static org.openmhealth.schema.domain.omh.TimeInterval.ofStartDateTimeAndDuration;


/**
 * @author Emerson Farrugia
 */
@Service
public class StepCountDataPointGenerationServiceImpl extends AbstractDataPointGenerationServiceImpl<StepCount> {

    @Override
    public StepCount newMeasure(MeasureGroup measureGroup) {

        DurationUnitValue duration = new DurationUnitValue(SECOND, measureGroup.getMeasureValue("durationInSec"));
        double stepsPerMin = measureGroup.getMeasureValue("stepsPerMin");

        double stepCount = stepsPerMin * duration.getValue().doubleValue() / 60.0;

        return new StepCount.Builder(stepCount)
                .setEffectiveTimeFrame(ofStartDateTimeAndDuration(measureGroup.getEffectiveDateTime(), duration))
                .build();
    }
}
