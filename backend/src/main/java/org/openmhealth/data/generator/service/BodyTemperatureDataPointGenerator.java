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
import org.openmhealth.schema.domain.omh.BodyTemperature;
import org.openmhealth.schema.domain.omh.TemperatureUnitValue;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.Collections.singleton;
import static org.openmhealth.schema.domain.omh.BodyTemperature.MeasurementLocation.ORAL;
import static org.openmhealth.schema.domain.omh.TemperatureUnit.CELSIUS;


/**
 * @author Emerson Farrugia
 */
@Component
public class BodyTemperatureDataPointGenerator extends AbstractDataPointGeneratorImpl<BodyTemperature> {

    public static final String TEMPERATURE_KEY = "temperature-in-c";

    @Override
    public String getName() {
        return "body-temperature";
    }

    @Override
    public Set<String> getRequiredValueGroupKeys() {
        return singleton(TEMPERATURE_KEY);
    }

    @Override
    public Set<String> getSupportedValueGroupKeys() {
        return singleton(TEMPERATURE_KEY);
    }

    @Override
    public BodyTemperature newMeasure(TimestampedValueGroup valueGroup) {

        return new BodyTemperature.Builder(new TemperatureUnitValue(CELSIUS, valueGroup.getValue(TEMPERATURE_KEY)))
                .setMeasurementLocation(ORAL)
                .setEffectiveTimeFrame(valueGroup.getTimestamp())
                .build();
    }
}
