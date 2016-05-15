package org.openmhealth.data.generator.service;

import org.openmhealth.data.generator.domain.TimestampedValueGroup;
import org.openmhealth.schema.domain.omh.RespiratoryRate;
import org.openmhealth.schema.domain.omh.TypedUnitValue;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.Collections.singleton;

@Component
public class RespiratoryRateDataPointGenerator extends AbstractDataPointGeneratorImpl<RespiratoryRate> {
    public static final String KEY = "breaths-per-min";

    @Override
    public String getName() {
        return "respiratory-rate";
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
    public RespiratoryRate newMeasure(TimestampedValueGroup valueGroup) {

        return new RespiratoryRate.Builder(new TypedUnitValue<>(RespiratoryRate.RespirationUnit.BREATHS_PER_MINUTE, valueGroup.getValue(KEY))).setEffectiveTimeFrame(valueGroup.getTimestamp()).build();

    }
}