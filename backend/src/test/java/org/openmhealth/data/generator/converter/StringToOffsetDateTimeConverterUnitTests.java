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

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * @author Emerson Farrugia
 */
public class StringToOffsetDateTimeConverterUnitTests {

    private StringToOffsetDateTimeConverter converter = new StringToOffsetDateTimeConverter();

    @Test
    public void convertShouldReturnNullOnUndefinedSource() throws Exception {

        assertThat(converter.convert(null), nullValue());
    }

    @Test(expectedExceptions = DateTimeParseException.class)
    public void convertShouldThrowExceptionOnMalformedSource() throws Exception {

        converter.convert("sbcdo");
    }

    @Test
    public void convertShouldReturnCorrectOffsetDateTimeOnValieSource() throws Exception {

        OffsetDateTime dateTime = converter.convert("2013-02-05T07:25:00Z");

        assertThat(dateTime, notNullValue());
        assertThat(dateTime, equalTo(OffsetDateTime.of(2013, 2, 5, 7, 25, 0, 0, ZoneOffset.UTC)));
    }
}