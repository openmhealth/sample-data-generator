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

package org.openmhealth.data.generator.converter;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Duration;


/**
 * A converter that creates {@link Duration} objects from strings.
 *
 * <p>
 * <pre>
 *    "PT20.345S" -- parses as "20.345 seconds"
 *    "PT15M"     -- parses as "15 minutes" (where a minute is 60 seconds)
 *    "PT10H"     -- parses as "10 hours" (where an hour is 3600 seconds)
 *    "P2D"       -- parses as "2 days" (where a day is 24 hours or 86400 seconds)
 *    "P2DT3H4M"  -- parses as "2 days, 3 hours and 4 minutes"
 * </pre>
 *
 * @author Emerson Farrugia
 * @see {@link Duration#parse(CharSequence)}
 */
@Component
@ConfigurationPropertiesBinding
public class StringToDurationConverter implements Converter<String, Duration> {

    @Override
    public Duration convert(String source) {

        if (source == null) {
            return null;
        }

        return Duration.parse(source);
    }
}
