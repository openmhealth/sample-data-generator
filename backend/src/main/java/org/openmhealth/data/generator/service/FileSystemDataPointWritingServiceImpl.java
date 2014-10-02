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
import org.openmhealth.schema.pojos.DataPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


/**
 * @author Emerson Farrugia
 */
@Service
public class FileSystemDataPointWritingServiceImpl implements DataPointWritingService {

    @Value("${filename}")
    private String filename;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void writeDataPoints(Iterable<DataPoint> dataPoints) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {

            for (DataPoint dataPoint : dataPoints) {
                objectMapper.writeValue(writer, dataPoint);
            }
        }
    }
}
