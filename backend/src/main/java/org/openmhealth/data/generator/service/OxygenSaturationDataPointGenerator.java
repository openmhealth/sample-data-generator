/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
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
import org.openmhealth.schema.domain.omh.OxygenSaturation;
import org.openmhealth.schema.domain.omh.PercentUnit;
import org.openmhealth.schema.domain.omh.TypedUnitValue;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.Collections.singleton;

@Component
public class OxygenSaturationDataPointGenerator extends AbstractDataPointGeneratorImpl<OxygenSaturation> {
    public static final String KEY = "oxygen-saturation-level";

    @Override
    public String getName() {
        return "oxygen-saturation";
    }

    @Override
    public Set<String> getRequiredValueGroupKeys() {
        return singleton(KEY);
    }

    @Override
    public Set<String> getSupportedValueGroupKeys() {
        return singleton(KEY);
    }

    @Override
    public OxygenSaturation newMeasure(TimestampedValueGroup valueGroup) {

        return new OxygenSaturation.Builder(new TypedUnitValue<>(PercentUnit.PERCENT, valueGroup.getValue(KEY))).setMeasurementMethod(OxygenSaturation.MeasurementMethod.PULSE_OXIMETRY).setMeasurementSystem(OxygenSaturation.MeasurementSystem.PERIPHERAL_CAPILLARY).setEffectiveTimeFrame(valueGroup.getTimestamp()).build();

    }
}
