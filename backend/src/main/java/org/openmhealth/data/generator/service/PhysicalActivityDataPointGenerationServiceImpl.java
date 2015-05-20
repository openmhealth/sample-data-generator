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

import org.openmhealth.data.generator.domain.MeasureGroup;
import org.openmhealth.schema.domain.omh.DurationUnitValue;
import org.openmhealth.schema.domain.omh.LengthUnitValue;
import org.openmhealth.schema.domain.omh.PhysicalActivity;
import org.springframework.stereotype.Service;

import static org.openmhealth.schema.domain.omh.DurationUnit.SECOND;
import static org.openmhealth.schema.domain.omh.LengthUnit.METER;
import static org.openmhealth.schema.domain.omh.TimeInterval.ofStartDateTimeAndDuration;


/**
 * @author Emerson Farrugia
 */
@Service
public class PhysicalActivityDataPointGenerationServiceImpl
        extends AbstractDataPointGenerationServiceImpl<PhysicalActivity> {

    @Override
    public PhysicalActivity newMeasure(MeasureGroup measureGroup) {

        DurationUnitValue duration = new DurationUnitValue(SECOND, measureGroup.getMeasureValue("durationInSec"));
        LengthUnitValue distance = new LengthUnitValue(METER, measureGroup.getMeasureValue("distance"));

        return new PhysicalActivity.Builder("some activity")
                .setEffectiveTimeFrame(ofStartDateTimeAndDuration(measureGroup.getEffectiveDateTime(), duration))
                .setDistance(distance)
                .build();
    }
}
