package org.openmhealth.data.generator.service;

import org.openmhealth.data.generator.domain.TimestampedValueGroup;
import org.openmhealth.schema.domain.omh.OxygenSaturation;
import org.openmhealth.schema.domain.omh.PercentUnit;
import org.openmhealth.schema.domain.omh.TypedUnitValue;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.Collections.singleton;

@Component
public class OxygenSaturationDataPointGenerator extends AbstractDataPointGeneratorImpl<OxygenSaturation> {
    public static final String KEY = "oxygen-saturation-level";

    @Override
    public String getName() {
        return "oxygen-saturation";
    }

    @Override
    public Set<String> getRequiredValueGroupKeys() {
        return singleton(KEY);
    }

    @Override
    public Set<String> getSupportedValueGroupKeys() {
        return singleton(KEY);
    }

    @Override
    public OxygenSaturation newMeasure(TimestampedValueGroup valueGroup) {

        return new OxygenSaturation.Builder(new TypedUnitValue<>(PercentUnit.PERCENT, valueGroup.getValue(KEY))).setMeasurementMethod(OxygenSaturation.MeasurementMethod.PULSE_OXIMETRY).setMeasurementSystem(OxygenSaturation.MeasurementSystem.PERIPHERAL_CAPILLARY).setEffectiveTimeFrame(valueGroup.getTimestamp()).build();

    }
}
