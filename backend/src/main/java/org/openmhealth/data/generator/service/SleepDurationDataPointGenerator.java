/*
 * Copyright 2016 Open mHealth
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

import org.openmhealth.data.generator.domain.TimestampedValueGroup;
import org.openmhealth.schema.domain.omh.DurationUnitValue;
import org.openmhealth.schema.domain.omh.SleepDuration;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.Collections.singleton;
import static org.openmhealth.schema.domain.omh.DurationUnit.HOUR;
import static org.openmhealth.schema.domain.omh.TimeInterval.ofStartDateTimeAndDuration;


/**
 * @author Emerson Farrugia
 */
@Component
public class SleepDurationDataPointGenerator
        extends AbstractDataPointGeneratorImpl<SleepDuration> {

    public static final String DURATION_KEY = "duration-in-hours";

    @Override
    public String getName() {
        return "sleep-duration";
    }

    @Override
    public Set<String> getRequiredValueGroupKeys() {
        return singleton(DURATION_KEY);
    }

    @Override
    public Set<String> getSupportedValueGroupKeys() {
        return singleton(DURATION_KEY);
    }

    @Override
    public SleepDuration newMeasure(TimestampedValueGroup valueGroup) {

        DurationUnitValue duration = new DurationUnitValue(HOUR, valueGroup.getValue(DURATION_KEY));

        SleepDuration.Builder builder = new SleepDuration.Builder(duration)
                .setEffectiveTimeFrame(ofStartDateTimeAndDuration(valueGroup.getTimestamp(), duration));

        return builder.build();
    }
}
