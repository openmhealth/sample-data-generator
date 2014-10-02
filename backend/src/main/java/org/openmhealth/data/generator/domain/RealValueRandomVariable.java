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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.security.SecureRandom;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * A random variable that models normally-distributed real values. If limits are specified, returned samples will
 * fall between the specified limits.
 *
 * @author Emerson Farrugia
 */
public class RealValueRandomVariable {

    private static final Random prng = new SecureRandom();

    private double variance;
    private double standardDeviation;
    private Double minimum;
    private Double maximum;


    public RealValueRandomVariable(double standardDeviation) {

        setStandardDeviation(standardDeviation);
    }

    public RealValueRandomVariable(double standardDeviation, double minimum, double maximum) {

        checkArgument(minimum <= maximum);

        setStandardDeviation(standardDeviation);
        setMinimum(minimum);
        setMaximum(maximum);
    }

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {

        checkArgument(variance >= 0);

        this.variance = variance;
        this.standardDeviation = Math.sqrt(variance);
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {

        checkArgument(standardDeviation >= 0);

        this.standardDeviation = standardDeviation;
        this.variance = Math.pow(standardDeviation, 2.0);
    }

    public Double getMinimum() {
        return minimum;
    }

    public void setMinimum(Double minimum) {

        checkArgument(minimum == null || maximum == null || minimum >= maximum);

        this.minimum = minimum;
    }

    public Double getMaximum() {
        return maximum;
    }

    public void setMaximum(Double maximum) {

        checkArgument(minimum == null || maximum == null || minimum >= maximum);

        this.maximum = maximum;
    }

    public double nextValue(double mean) {

        double nextValue;

        do {
            nextValue = prng.nextGaussian() * standardDeviation + mean;

            if ((minimum == null || nextValue >= minimum) && (maximum == null || nextValue <= maximum)) {
                break;
            }
        } while (true);

        return nextValue;
    }

    @Override
    public String toString() {

        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
