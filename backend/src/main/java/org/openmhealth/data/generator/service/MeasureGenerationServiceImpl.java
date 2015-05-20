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

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.openmhealth.data.generator.domain.MeasureGenerationRequest;
import org.openmhealth.data.generator.domain.MeasureGroup;
import org.openmhealth.data.generator.domain.RealValueRandomVariableTrend;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.time.temporal.ChronoUnit.SECONDS;


/**
 * @author Emerson Farrugia
 */
@Service
public class MeasureGenerationServiceImpl implements MeasureGenerationService {

    public static final int NIGHT_TIME_START_HOUR = 23;
    public static final int NIGHT_TIME_END_HOUR = 5;

    @Override
    public Iterable<MeasureGroup> generateMeasureGroups(MeasureGenerationRequest request) {

        PoissonDistribution poissonDistribution =
                new PoissonDistribution(request.getMeanInterPointDuration().getSeconds());

        long totalDurationInS = Duration.between(request.getStartDateTime(), request.getEndDateTime()).getSeconds();

        OffsetDateTime effectiveDateTime = request.getStartDateTime();
        List<MeasureGroup> measureGroups = new ArrayList<>();

        do {
            effectiveDateTime = effectiveDateTime.plus(poissonDistribution.sample(), SECONDS);

            if (!effectiveDateTime.isBefore(request.getEndDateTime())) {
                break;
            }

            if (request.isSuppressNightTimeMeasures() &&
                    (effectiveDateTime.getHour() < NIGHT_TIME_END_HOUR ||
                            effectiveDateTime.getHour() > NIGHT_TIME_START_HOUR)) {
                continue;
            }

            MeasureGroup measureGroup = new MeasureGroup();
            measureGroup.setEffectiveDateTime(effectiveDateTime);

            double trendProgressFraction = (double)
                    Duration.between(request.getStartDateTime(), effectiveDateTime).getSeconds() / totalDurationInS;

            for (Map.Entry<String, RealValueRandomVariableTrend> trendEntry : request.getMeasureValueTrends()
                    .entrySet()) {

                String measure = trendEntry.getKey();
                RealValueRandomVariableTrend trend = trendEntry.getValue();

                measureGroup.setMeasureValue(measure, trend.nextValue(trendProgressFraction));
            }

            measureGroups.add(measureGroup);
        }
        while (true);

        return measureGroups;
    }
}
