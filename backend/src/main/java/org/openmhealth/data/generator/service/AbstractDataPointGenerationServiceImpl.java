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
import org.openmhealth.schema.domain.omh.DataPoint;
import org.openmhealth.schema.domain.omh.DataPointAcquisitionProvenance;
import org.openmhealth.schema.domain.omh.DataPointHeader;
import org.openmhealth.schema.domain.omh.Measure;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

import static java.time.OffsetDateTime.now;
import static java.util.UUID.randomUUID;
import static org.openmhealth.schema.domain.omh.DataPointModality.SENSED;


/**
 * @author Emerson Farrugia
 */
public abstract class AbstractDataPointGenerationServiceImpl<T extends Measure>
        implements DataPointGenerationService<T> {

    @Value("${data.userId}")
    private String userId;

    @Value("${data.header.acquisitionProvenance.sourceName:generator}")
    private String sourceName;


    @Override
    public Iterable<DataPoint<T>> generateDataPoints(Iterable<MeasureGroup> measureGroups) {

        List<DataPoint<T>> dataPoints = new ArrayList<>();

        for (MeasureGroup measureGroup : measureGroups) {
            dataPoints.add(newDataPoint(newMeasure(measureGroup)));
        }

        return dataPoints;
    }

    /**
     * @param measureGroup a measure group
     * @return a measure corresponding to the specified measure group
     */
    public abstract T newMeasure(MeasureGroup measureGroup);

    /**
     * @param measure a measure
     * @return a data point corresponding to the specified measure, using sane header values
     */
    public DataPoint<T> newDataPoint(T measure) {

        DataPointAcquisitionProvenance acquisitionProvenance = new DataPointAcquisitionProvenance.Builder(sourceName)
                .setModality(SENSED)
                .setSourceCreationDateTime(now().minusMinutes(5))
                .build();

        DataPointHeader header = new DataPointHeader.Builder(randomUUID().toString(), measure.getSchemaId(), now())
                .setAcquisitionProvenance(acquisitionProvenance)
                .build();

        DataPoint<T> dataPoint = new DataPoint<>(header, measure);

        // TODO update this once it's part of the schema SDK
        dataPoint.setAdditionalProperty("user_id", userId);

        return dataPoint;
    }
}
