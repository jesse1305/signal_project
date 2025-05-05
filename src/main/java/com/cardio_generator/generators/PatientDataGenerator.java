package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;


/**
 * Interface representing a generic patient data generator.
 * <p>
 * Implementations of this interface should define how health-related data
 * is generated for a specific patient and how the data is sent using an
 * output strategy.
 */
public interface PatientDataGenerator {
    void generate(int patientId, OutputStrategy outputStrategy);
}
