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

import org.joda.time.DateTime;
import org.openmhealth.schema.pojos.DataPoint;
import org.openmhealth.schema.pojos.DataPointBody;
import org.openmhealth.schema.pojos.Metadata;
import org.springframework.beans.factory.annotation.Value;

import java.time.OffsetDateTime;


/**
 * @author Emerson Farrugia
 */
public class AbstractDataPointGenerationServiceImpl {

    @Value("${data.owner}")
    private String owner;

    public DateTime convert(OffsetDateTime offsetDateTime) {
        return new DateTime(offsetDateTime.toEpochSecond() * 1_000);
    }

    public DataPoint newDataPoint(DataPointBody body, String shim) {

        Metadata metadata = new Metadata();
        metadata.setTimestamp(body.getTimeStamp());
        metadata.setShimKey(shim);

        DataPoint dataPoint = new DataPoint();
        dataPoint.setOwner(owner);
        dataPoint.setMetadata(metadata);
        dataPoint.setBody(body);

        return dataPoint;
    }
}
