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
import org.openmhealth.schema.domain.omh.RespiratoryRate;
import org.openmhealth.schema.domain.omh.TypedUnitValue;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.Collections.singleton;

@Component
public class RespiratoryRateDataPointGenerator extends AbstractDataPointGeneratorImpl<RespiratoryRate> {
    public static final String KEY = "breaths-per-min";

    @Override
    public String getName() {
        return "respiratory-rate";
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
    public RespiratoryRate newMeasure(TimestampedValueGroup valueGroup) {

        return new RespiratoryRate.Builder(new TypedUnitValue<>(RespiratoryRate.RespirationUnit.BREATHS_PER_MINUTE, valueGroup.getValue(KEY))).setEffectiveTimeFrame(valueGroup.getTimestamp()).build();

    }
}