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
import org.openmhealth.schema.domain.omh.LengthUnitValue;
import org.openmhealth.schema.domain.omh.PhysicalActivity;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.openmhealth.schema.domain.omh.DurationUnit.SECOND;
import static org.openmhealth.schema.domain.omh.LengthUnit.METER;
import static org.openmhealth.schema.domain.omh.TimeInterval.ofStartDateTimeAndDuration;


/**
 * @author Emerson Farrugia
 */
@Component
public class PhysicalActivityDataPointGenerator
        extends AbstractDataPointGeneratorImpl<PhysicalActivity> {

    public static final String DURATION_VALUE_KEY = "duration-in-seconds";
    public static final String DISTANCE_VALUE_KEY = "distance-in-meters";

    @Override
    public String getName() {
        return "physical-activity";
    }

    @Override
    public Set<String> getRequiredValueKeys() {
        return Sets.newHashSet(DURATION_VALUE_KEY);
    }

    @Override
    public Set<String> getSupportedValueKeys() {
        return Sets.newHashSet(DURATION_VALUE_KEY, DISTANCE_VALUE_KEY);
    }

    @Override
    public PhysicalActivity newMeasure(TimestampedValueGroup valueGroup) {

        DurationUnitValue duration = new DurationUnitValue(SECOND, valueGroup.getValue(DURATION_VALUE_KEY));

        PhysicalActivity.Builder builder = new PhysicalActivity.Builder("some activity")
                .setEffectiveTimeFrame(ofStartDateTimeAndDuration(valueGroup.getTimestamp(), duration));

        if (valueGroup.getValue(DISTANCE_VALUE_KEY) != null) {
            builder.setDistance(new LengthUnitValue(METER, valueGroup.getValue(DISTANCE_VALUE_KEY)));
        }

        return builder.build();
    }
}
