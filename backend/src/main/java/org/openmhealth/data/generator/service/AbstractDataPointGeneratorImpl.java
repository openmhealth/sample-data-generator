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

import org.openmhealth.data.generator.domain.TimestampedValueGroup;
import org.openmhealth.schema.domain.omh.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static java.util.UUID.randomUUID;
import static org.openmhealth.schema.domain.omh.DataPointModality.SENSED;


/**
 * @author Emerson Farrugia
 */
public abstract class AbstractDataPointGeneratorImpl<T extends Measure>
        implements DataPointGenerator<T> {

    @Value("${data.header.user-id:some-user}")
    private String userId;

    @Value("${data.header.acquisition-provenance.source-name:generator}")
    private String sourceName;


    @Override
    public Iterable<DataPoint<T>> generateDataPoints(Iterable<TimestampedValueGroup> valueGroups) {

        List<DataPoint<T>> dataPoints = new ArrayList<>();

        for (TimestampedValueGroup timestampedValueGroup : valueGroups) {
            dataPoints.add(newDataPoint(newMeasure(timestampedValueGroup)));
        }

        return dataPoints;
    }

    /**
     * @param valueGroup a group of values
     * @return a measure corresponding to the specified values
     */
    public abstract T newMeasure(TimestampedValueGroup valueGroup);

    /**
     * @param measure a measure
     * @return a data point corresponding to the specified measure
     */
    public DataPoint<T> newDataPoint(T measure) {

        TimeInterval effectiveTimeInterval = measure.getEffectiveTimeFrame().getTimeInterval();
        OffsetDateTime effectiveEndDateTime;

        if (effectiveTimeInterval != null) {
            if (effectiveTimeInterval.getEndDateTime() != null) {
                effectiveEndDateTime = effectiveTimeInterval.getEndDateTime();
            }
            else {
                // use the duration to calculate the end date time of the interval
                if (effectiveTimeInterval.getDuration() != null) {

                    BiFunction<OffsetDateTime, Long, OffsetDateTime> plusFunction;

                    switch (effectiveTimeInterval.getDuration().getTypedUnit()) {

                        case SECOND:
                            plusFunction = OffsetDateTime::plusSeconds;
                            break;
                        case MINUTE:
                            plusFunction = OffsetDateTime::plusMinutes;
                            break;
                        case HOUR:
                            plusFunction = OffsetDateTime::plusHours;
                            break;
                        case DAY:
                            plusFunction = OffsetDateTime::plusDays;
                            break;
                        case WEEK:
                            plusFunction = OffsetDateTime::plusWeeks;
                            break;
                        case MONTH:
                            plusFunction = OffsetDateTime::plusMonths;
                            break;
                        case YEAR:
                            plusFunction = OffsetDateTime::plusYears;
                            break;
                        default:
                            throw new IllegalStateException("A time interval duration type isn't supported.");
                    }

                    effectiveEndDateTime = plusFunction.apply(effectiveTimeInterval.getStartDateTime(),
                            effectiveTimeInterval.getDuration().getValue().longValue());
                }
                else {
                    throw new IllegalStateException("An end date time can't be calculated without a duration.");
                }
            }
        }
        else { // if this is a point in time measure
            effectiveEndDateTime = measure.getEffectiveTimeFrame().getDateTime();
        }

        DataPointAcquisitionProvenance acquisitionProvenance =
                new DataPointAcquisitionProvenance.Builder(sourceName)
                        .setModality(SENSED)
                        .setSourceCreationDateTime(effectiveEndDateTime)
                        .build();

        DataPointHeader header =
                new DataPointHeader.Builder(randomUUID().toString(), measure.getSchemaId(),
                        effectiveEndDateTime.plusMinutes(1))
                        .setAcquisitionProvenance(acquisitionProvenance)
                        .setUserId(userId)
                        .build();

        return new DataPoint<>(header, measure);
    }
}
