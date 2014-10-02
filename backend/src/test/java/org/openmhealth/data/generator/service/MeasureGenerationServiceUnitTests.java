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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.openmhealth.data.generator.domain.MeasureGenerationRequest;
import org.openmhealth.data.generator.domain.RealValueRandomVariable;
import org.openmhealth.data.generator.domain.RealValueRandomVariableTrend;
import org.openmhealth.data.generator.domain.MeasureGroup;

import java.time.Duration;
import java.time.OffsetDateTime;


/**
 * @author Emerson Farrugia
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class MeasureGenerationServiceUnitTests {

    private MeasureGenerationService service = new MeasureGenerationServiceImpl();
    @Test
    public void generateMeasureGroupsShouldWork() {

        RealValueRandomVariable systolicRandomVariable = new RealValueRandomVariable(1);
        systolicRandomVariable.setMinimum(50d);
        systolicRandomVariable.setMaximum(90d);

        RealValueRandomVariableTrend systolicTrend = new RealValueRandomVariableTrend(systolicRandomVariable, 60, 80);

        MeasureGenerationRequest request = new MeasureGenerationRequest();
        request.setStartDateTime(OffsetDateTime.now().minusDays(2));
        request.setEndDateTime(OffsetDateTime.now().minusDays(1));
        request.setMeanInterPointDuration(Duration.ofHours(6));
        request.addMeasureValueTrend("systolic", systolicTrend);

        Iterable<MeasureGroup> measureGroups = service.generateMeasureGroups(request);

        System.out.println(measureGroups);
    }
}