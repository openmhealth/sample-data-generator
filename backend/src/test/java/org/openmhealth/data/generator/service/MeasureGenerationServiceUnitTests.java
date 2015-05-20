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

import com.google.common.collect.Iterables;
import org.openmhealth.data.generator.domain.MeasureGenerationRequest;
import org.openmhealth.data.generator.domain.MeasureGroup;
import org.openmhealth.data.generator.domain.RealValueRandomVariable;
import org.openmhealth.data.generator.domain.RealValueRandomVariableTrend;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.OffsetDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


/**
 * @author Emerson Farrugia
 */
public class MeasureGenerationServiceUnitTests {

    private MeasureGenerationService service = new MeasureGenerationServiceImpl();

    @Test
    public void generateMeasureGroupsShouldCreateMeasureGroups() {

        String measureName = "systolic";
        double minimumSystolicValue = 50d;
        double maximumSystolicValue = 90d;

        RealValueRandomVariable systolicRandomVariable =
                new RealValueRandomVariable(1, minimumSystolicValue, maximumSystolicValue);

        RealValueRandomVariableTrend systolicTrend = new RealValueRandomVariableTrend(systolicRandomVariable, 60, 80);

        OffsetDateTime startDateTime = OffsetDateTime.now().minusDays(500);
        OffsetDateTime endDateTime = OffsetDateTime.now();

        MeasureGenerationRequest request = new MeasureGenerationRequest();
        request.setStartDateTime(startDateTime);
        request.setEndDateTime(endDateTime);
        request.setMeanInterPointDuration(Duration.ofHours(6));
        request.addMeasureValueTrend(measureName, systolicTrend);

        Iterable<MeasureGroup> measureGroups;

        do {
            measureGroups = service.generateMeasureGroups(request);
        }
        while (Iterables.size(measureGroups) == 0); // in the extremely unlikely case of an empty list

        for (MeasureGroup measureGroup : measureGroups) {

            assertThat(measureGroup.getMeasureValue(measureName), greaterThanOrEqualTo(minimumSystolicValue));
            assertThat(measureGroup.getMeasureValue(measureName), lessThanOrEqualTo(maximumSystolicValue));

            long effectiveDateTime = measureGroup.getEffectiveDateTime().toEpochSecond();
            assertThat(effectiveDateTime, greaterThanOrEqualTo(startDateTime.toEpochSecond()));
            assertThat(effectiveDateTime, lessThanOrEqualTo(endDateTime.toEpochSecond()));
        }
    }
}