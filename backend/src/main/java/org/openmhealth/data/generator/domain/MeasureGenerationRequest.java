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

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Emerson Farrugia
 */
public class MeasureGenerationRequest {

    private OffsetDateTime startDateTime;
    private OffsetDateTime endDateTime;
    private Map<String, RealValueRandomVariableTrend> measureValueTrends = new HashMap<>();
    private Duration meanInterPointDuration;

    public OffsetDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(OffsetDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public OffsetDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(OffsetDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Map<String, RealValueRandomVariableTrend> getMeasureValueTrends() {
        return measureValueTrends;
    }

    public void setMeasureValueTrends(Map<String, RealValueRandomVariableTrend> measureValueTrends) {
        this.measureValueTrends = measureValueTrends;
    }

    public void addMeasureValueTrend(String measure, RealValueRandomVariableTrend trend) {
        this.measureValueTrends.put(measure, trend);
    }

    public Duration getMeanInterPointDuration() {
        return meanInterPointDuration;
    }

    public void setMeanInterPointDuration(Duration meanInterPointDuration) {
        this.meanInterPointDuration = meanInterPointDuration;
    }
}
