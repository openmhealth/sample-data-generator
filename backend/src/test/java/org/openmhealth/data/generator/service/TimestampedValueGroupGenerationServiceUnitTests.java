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
import org.openmhealth.data.generator.domain.BoundedRandomVariable;
import org.openmhealth.data.generator.domain.BoundedRandomVariableTrend;
import org.openmhealth.data.generator.domain.MeasureGenerationRequest;
import org.openmhealth.data.generator.domain.TimestampedValueGroup;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.OffsetDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;


/**
 * @author Emerson Farrugia
 */
public class TimestampedValueGroupGenerationServiceUnitTests {

    private TimestampedValueGroupGenerationService service = new TimestampedValueGroupGenerationServiceImpl();


    @Test
    public void generateValueGroupsShouldWork() {

        String valueKey = "foo";
        double minimumValue = 50d;
        double maximumValue = 90d;

        BoundedRandomVariable randomVariable = new BoundedRandomVariable(1.0, minimumValue, maximumValue);
        BoundedRandomVariableTrend randomVariableTrend = new BoundedRandomVariableTrend(randomVariable, 60d, 80d);

        OffsetDateTime startDateTime = OffsetDateTime.now().minusDays(10);
        OffsetDateTime endDateTime = OffsetDateTime.now();

        MeasureGenerationRequest request = new MeasureGenerationRequest();
        request.setStartDateTime(startDateTime);
        request.setEndDateTime(endDateTime);
        request.setMeanInterPointDuration(Duration.ofHours(6));
        request.addValueTrend(valueKey, randomVariableTrend);

        Iterable<TimestampedValueGroup> valueGroups;

        do {
            valueGroups = service.generateValueGroups(request);
        }
        while (Iterables.size(valueGroups) == 0); // in the extremely unlikely case of an empty list

        for (TimestampedValueGroup valueGroup : valueGroups) {

            assertThat(valueGroup.getValue(valueKey), greaterThanOrEqualTo(minimumValue));
            assertThat(valueGroup.getValue(valueKey), lessThanOrEqualTo(maximumValue));

            long effectiveDateTime = valueGroup.getTimestamp().toEpochSecond();
            assertThat(effectiveDateTime, greaterThanOrEqualTo(startDateTime.toEpochSecond()));
            assertThat(effectiveDateTime, lessThanOrEqualTo(endDateTime.toEpochSecond()));
        }
    }
}