/*
 * Copyright 2015 Open mHealth
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
import org.openmhealth.schema.domain.omh.MinutesModerateActivity;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.openmhealth.schema.domain.omh.DurationUnit.MINUTE;
import static org.openmhealth.schema.domain.omh.TimeInterval.ofStartDateTimeAndDuration;


/**
 * @author Emerson Farrugia
 */
@Component
public class MinutesModerateActivityDataPointGenerator
        extends AbstractDataPointGeneratorImpl<MinutesModerateActivity> {

    public static final String MINUTES_VALUE_KEY = "minutes";

    @Override
    public String getName() {
        return "minutes-moderate-activity";
    }

    @Override
    public Set<String> getRequiredValueKeys() {
        return Sets.newHashSet(MINUTES_VALUE_KEY);
    }

    @Override
    public Set<String> getSupportedValueKeys() {
        return Sets.newHashSet(MINUTES_VALUE_KEY);
    }

    @Override
    public MinutesModerateActivity newMeasure(TimestampedValueGroup valueGroup) {

        DurationUnitValue duration = new DurationUnitValue(MINUTE, valueGroup.getValue(MINUTES_VALUE_KEY));

        MinutesModerateActivity.Builder builder = new MinutesModerateActivity.Builder(duration)
                .setEffectiveTimeFrame(ofStartDateTimeAndDuration(valueGroup.getTimestamp(), duration));

        return builder.build();
    }
}
