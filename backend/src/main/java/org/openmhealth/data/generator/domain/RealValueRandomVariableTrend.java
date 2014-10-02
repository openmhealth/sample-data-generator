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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * A trend wraps a {@link RealValueRandomVariable} in order to provide samples whose mean falls along a linear
 * interpolation from a start value to an end value.
 *
 * @author Emerson Farrugia
 */
public class RealValueRandomVariableTrend {

    private RealValueRandomVariable realValueRandomVariable;
    private double startValue;
    private double endValue;

    public RealValueRandomVariableTrend(RealValueRandomVariable realValueRandomVariable, double startValue,
            double endValue) {

        checkNotNull(realValueRandomVariable);

        this.realValueRandomVariable = realValueRandomVariable;
        this.startValue = startValue;
        this.endValue = endValue;
    }

    public RealValueRandomVariable getRealValueRandomVariable() {
        return realValueRandomVariable;
    }

    public void setRealValueRandomVariable(RealValueRandomVariable realValueRandomVariable) {

        checkNotNull(realValueRandomVariable);

        this.realValueRandomVariable = realValueRandomVariable;
    }

    public double getStartValue() {
        return startValue;
    }

    public void setStartValue(double startValue) {
        this.startValue = startValue;
    }

    public double getEndValue() {
        return endValue;
    }

    public void setEndValue(double endValue) {
        this.endValue = endValue;
    }

    public double interpolate(double fraction) {

        checkArgument(fraction >= 0);
        checkArgument(fraction <= 1);

        return startValue + (endValue - startValue) * fraction;
    }

    public double nextValue(double fraction) {

        double mean = interpolate(fraction);
        return realValueRandomVariable.nextValue(mean);
    }

    @Override
    public String toString() {

        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}

