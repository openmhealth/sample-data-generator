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

package org.openmhealth.data.generator.domain;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;


/**
 * A group of measure values that share an effective time frame.
 *
 * @author Emerson Farrugia
 */
public class MeasureGroup {

    private OffsetDateTime effectiveDateTime;
    private Map<String, Double> measureValues = new HashMap<>();

    public OffsetDateTime getEffectiveDateTime() {
        return effectiveDateTime;
    }

    public void setEffectiveDateTime(OffsetDateTime effectiveDateTime) {
        this.effectiveDateTime = effectiveDateTime;
    }

    public Map<String, Double> getMeasureValues() {
        return measureValues;
    }

    public void setMeasureValues(Map<String, Double> measureValues) {
        this.measureValues = measureValues;
    }

    public void setMeasureValue(String measure, Double value) {
        this.measureValues.put(measure, value);
    }

    public Double getMeasureValue(String measure) {
        return this.measureValues.get(measure);
    }
}
