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

import static java.time.OffsetDateTime.now;
import static java.util.UUID.randomUUID;
import static org.openmhealth.schema.domain.omh.DataPointModality.SENSED;


/**
 * @author Emerson Farrugia
 */
public abstract class AbstractDataPointGeneratorImpl<T extends Measure>
        implements DataPointGenerator<T> {

    @Value("${data.header.user-id}")
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

        OffsetDateTime creationDateTime = null;
        TimeInterval effectiveTimeInterval = measure.getEffectiveTimeFrame().getTimeInterval();

        if (effectiveTimeInterval != null) {
            // set the creation date time to the end of the effective time interval
            if (effectiveTimeInterval.getEndDateTime() != null) {
                creationDateTime = effectiveTimeInterval.getEndDateTime();
            }
            else {
                if (effectiveTimeInterval.getDuration() != null) {
                    if (effectiveTimeInterval.getDuration().getTypedUnit() == DurationUnit.SECOND) {
                        creationDateTime = effectiveTimeInterval.getStartDateTime()
                                .plusSeconds(effectiveTimeInterval.getDuration().getValue().longValue());
                    }
                    else if (effectiveTimeInterval.getDuration().getTypedUnit() == DurationUnit.MINUTE) {
                        creationDateTime = effectiveTimeInterval.getStartDateTime()
                                .plusMinutes(effectiveTimeInterval.getDuration().getValue().longValue());
                    }
                    else {
                        throw new IllegalStateException("A creation date time can't be determined.");
                    }
                }
            }
        }
        else {
            // set the creation date time to the effective date time
            creationDateTime = measure.getEffectiveTimeFrame().getDateTime();
        }

        DataPointAcquisitionProvenance acquisitionProvenance =
                new DataPointAcquisitionProvenance.Builder(sourceName)
                        .setModality(SENSED)
                        .setSourceCreationDateTime(now().minusMinutes(5))
                        .build();

        DataPointHeader header =
                new DataPointHeader.Builder(randomUUID().toString(), measure.getSchemaId(), creationDateTime)
                        .setAcquisitionProvenance(acquisitionProvenance)
                        .setUserId(userId)
                        .build();

        return new DataPoint<>(header, measure);
    }
}
