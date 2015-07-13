/*
 * Copyright 2015 Open mHealth
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

package org.openmhealth.data.generator.domain;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;


/**
 * A group of real values sharing a common timestamp.
 *
 * @author Emerson Farrugia
 */
public class TimestampedValueGroup {

    private OffsetDateTime timestamp;
    private Map<String, Double> values = new HashMap<>();

    /**
     * @return the timestamp common to the values in this group
     */
    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the values as a map
     */
    public Map<String, Double> getValues() {
        return values;
    }

    public void setValues(Map<String, Double> values) {
        this.values = values;
    }

    public void setValue(String key, Double value) {
        this.values.put(key, value);
    }

    public Double getValue(String key) {
        return this.values.get(key);
    }
}
