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

import org.testng.annotations.Test;

import java.time.Duration;
import java.time.format.DateTimeParseException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.*;

/**
 * @author Emerson Farrugia
 */
public class StringToDurationConverterUnitTests {

    private StringToDurationConverter converter = new StringToDurationConverter();

    @Test
    public void convertShouldReturnNullOnUndefinedSource() throws Exception {

        assertThat(converter.convert(null), nullValue());
    }

    @Test(expectedExceptions = DateTimeParseException.class)
    public void convertShouldThrowExceptionOnMalformedSource() throws Exception {

        converter.convert("sbcdo");
    }

    @Test
    public void convertShouldReturnCorrectDurationOnPositiveSource() throws Exception {

        Duration duration = converter.convert("PT3H4M");

        assertThat(duration, notNullValue());
        assertThat(duration, equalTo(Duration.ofMinutes(184)));
    }

    @Test
    public void convertShouldReturnCorrectDurationOnNegativeSource() throws Exception {

        Duration duration = converter.convert("PT-6H-3M");

        assertThat(duration, notNullValue());
        assertThat(duration, equalTo(Duration.ofMinutes(-363)));
    }
}