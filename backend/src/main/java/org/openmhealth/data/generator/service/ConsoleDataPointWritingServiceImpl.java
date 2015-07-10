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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openmhealth.schema.domain.omh.DataPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
 * @author Emerson Farrugia
 */
@Service
public class ConsoleDataPointWritingServiceImpl implements DataPointWritingService {

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public long writeDataPoints(Iterable<? extends DataPoint<?>> dataPoints) throws IOException {

        long written = 0;

        for (DataPoint dataPoint : dataPoints) {
            // trying to use objectMapper.writeValue(System.out) closes the output stream, so doing it this way instead
            String dataPointAsString = objectMapper.writeValueAsString(dataPoint);

            System.out.println(dataPointAsString);
            written++;
        }

        return written;
    }
}
