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
import org.openmhealth.schema.domain.omh.BloodPressure;
import org.openmhealth.schema.domain.omh.DiastolicBloodPressure;
import org.openmhealth.schema.domain.omh.SystolicBloodPressure;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.openmhealth.schema.domain.omh.BloodPressureUnit.MM_OF_MERCURY;


/**
 * @author Emerson Farrugia
 */
@Component
public class BloodPressureDataPointGenerator
        extends AbstractDataPointGeneratorImpl<BloodPressure> {

    public static final String SYSTOLIC_KEY = "systolic-in-mmhg";
    public static final String DIASTOLIC_KEY = "diastolic-in-mmhg";

    @Override
    public String getName() {
        return "blood-pressure";
    }

    @Override
    public Set<String> getSupportedValueGroupKeys() {
        return Sets.newHashSet(SYSTOLIC_KEY, DIASTOLIC_KEY);
    }

    @Override
    public Set<String> getRequiredValueGroupKeys() {
        return Sets.newHashSet(SYSTOLIC_KEY, DIASTOLIC_KEY);
    }

    @Override
    public BloodPressure newMeasure(TimestampedValueGroup valueGroup) {

        return new BloodPressure.Builder(
                new SystolicBloodPressure(MM_OF_MERCURY, valueGroup.getValue(SYSTOLIC_KEY)),
                new DiastolicBloodPressure(MM_OF_MERCURY, valueGroup.getValue(DIASTOLIC_KEY)))
                .setEffectiveTimeFrame(valueGroup.getTimestamp())
                .build();
    }
}
