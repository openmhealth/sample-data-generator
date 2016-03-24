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

import com.google.common.collect.Sets;
import org.openmhealth.data.generator.domain.TimestampedValueGroup;
import org.openmhealth.schema.domain.omh.DurationUnitValue;
import org.openmhealth.schema.domain.omh.StepCount;
import org.springframework.stereotype.Service;

import java.util.Set;

import static org.openmhealth.schema.domain.omh.DurationUnit.SECOND;
import static org.openmhealth.schema.domain.omh.TimeInterval.ofStartDateTimeAndDuration;


/**
 * @author Emerson Farrugia
 */
@Service
public class StepCountDataPointGenerator extends AbstractDataPointGeneratorImpl<StepCount> {

    public static final String STEPS_PER_MINUTE_KEY = "steps-per-minute";
    public static final String DURATION_KEY = "duration-in-seconds";

    @Override
    public String getName() {
        return "step-count";
    }

    @Override
    public Set<String> getRequiredValueGroupKeys() {
        return Sets.newHashSet(DURATION_KEY, STEPS_PER_MINUTE_KEY);
    }

    @Override
    public Set<String> getSupportedValueGroupKeys() {
        return Sets.newHashSet(DURATION_KEY, STEPS_PER_MINUTE_KEY);
    }

    @Override
    public StepCount newMeasure(TimestampedValueGroup valueGroup) {

        DurationUnitValue duration = new DurationUnitValue(SECOND, valueGroup.getValue(DURATION_KEY));
        double stepsPerMin = valueGroup.getValue(STEPS_PER_MINUTE_KEY);

        Double stepCount = stepsPerMin * duration.getValue().doubleValue() / 60.0;

        return new StepCount.Builder(stepCount.longValue())
                .setEffectiveTimeFrame(ofStartDateTimeAndDuration(valueGroup.getTimestamp(), duration))
                .build();
    }
}
