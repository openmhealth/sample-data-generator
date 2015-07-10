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
import org.openmhealth.schema.domain.omh.DataPoint;
import org.openmhealth.schema.domain.omh.Measure;

import java.util.Set;


/**
 * @author Emerson Farrugia
 */
public interface DataPointGenerator<T extends Measure> {

    /**
     * @return the name of this generator
     */
    String getName();

    /**
     * @return the set of value group keys required by this generator
     */
    Set<String> getRequiredValueGroupKeys();

    /**
     * @return the set of value group keys supported by this generator
     */
    Set<String> getSupportedValueGroupKeys();

    /**
     * @param valueGroups a list of value groups, where each value group corresponds to a data point
     * @return the list of generated data points
     */
    Iterable<DataPoint<T>> generateDataPoints(Iterable<TimestampedValueGroup> valueGroups);
}
