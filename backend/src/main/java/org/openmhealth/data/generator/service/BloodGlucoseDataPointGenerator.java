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
import org.openmhealth.schema.domain.omh.BloodGlucose;
import org.openmhealth.schema.domain.omh.TypedUnitValue;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.Collections.singleton;
import static org.openmhealth.schema.domain.omh.BloodGlucoseUnit.MILLIGRAMS_PER_DECILITER;


/**
 * @author Emerson Farrugia
 */
@Component
public class BloodGlucoseDataPointGenerator
        extends AbstractDataPointGeneratorImpl<BloodGlucose> {

    public static final String GLUCOSE_KEY = "glucose-in-mg-per-dl";

    @Override
    public String getName() {
        return "blood-glucose";
    }

    @Override
    public Set<String> getSupportedValueGroupKeys() {
        return singleton(GLUCOSE_KEY);
    }

    @Override
    public Set<String> getRequiredValueGroupKeys() {
        return singleton(GLUCOSE_KEY);
    }

    @Override
    public BloodGlucose newMeasure(TimestampedValueGroup valueGroup) {

        // TODO set the specimen source once the SDK is updated to omh:blood-glucose:2.0
        return new BloodGlucose.Builder(
                new TypedUnitValue<>(MILLIGRAMS_PER_DECILITER, valueGroup.getValue(GLUCOSE_KEY)))
                .setEffectiveTimeFrame(valueGroup.getTimestamp())
                .build();
    }
}
