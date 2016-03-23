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
import org.openmhealth.schema.domain.omh.BodyHeight;
import org.openmhealth.schema.domain.omh.LengthUnitValue;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.Collections.singleton;
import static org.openmhealth.schema.domain.omh.LengthUnit.METER;


/**
 * @author Emerson Farrugia
 */
@Component
public class BodyHeightDataPointGenerator extends AbstractDataPointGeneratorImpl<BodyHeight> {

    public static final String HEIGHT_KEY = "height-in-m";

    @Override
    public String getName() {
        return "body-height";
    }

    @Override
    public Set<String> getRequiredValueGroupKeys() {
        return singleton(HEIGHT_KEY);
    }

    @Override
    public Set<String> getSupportedValueGroupKeys() {
        return singleton(HEIGHT_KEY);
    }

    @Override
    public BodyHeight newMeasure(TimestampedValueGroup valueGroup) {

        return new BodyHeight.Builder(new LengthUnitValue(METER, valueGroup.getValue(HEIGHT_KEY)))
                .setEffectiveTimeFrame(valueGroup.getTimestamp())
                .build();
    }
}
