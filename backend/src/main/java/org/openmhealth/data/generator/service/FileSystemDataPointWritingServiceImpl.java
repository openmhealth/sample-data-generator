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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * @author Emerson Farrugia
 */
@Service
@Primary
@ConditionalOnExpression("'${output.destination}' == 'file'")
public class FileSystemDataPointWritingServiceImpl implements DataPointWritingService {

    @Value("${output.file.filename}")
    private String filename;

    @Value("${output.file.append:true}")
    private Boolean append;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void clearFile() throws IOException {

        if (!append) {
            Files.deleteIfExists(Paths.get(filename));
        }
    }

    @Override
    public long writeDataPoints(Iterable<? extends DataPoint<?>> dataPoints) throws IOException {

        long written = 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {

            for (DataPoint dataPoint : dataPoints) {
                // this simplifies direct imports into MongoDB
                dataPoint.setAdditionalProperty("id", dataPoint.getHeader().getId());

                String valueAsString = objectMapper.writeValueAsString(dataPoint);
                writer.write(valueAsString);
                writer.write("\n");
                written++;
            }
        }

        return written;
    }
}
