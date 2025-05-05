package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * A data generator that simulates blood oxygen saturation levels for a set of patients.
 * <p>
 * Implements the {@link PatientDataGenerator} interface and generates blood saturation
 * data values that fluctuate slightly within a realistic and healthy range.
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {
    /**
     * Random number generator used to simulate value fluctuations.
     */
    private static final Random random = new Random();

    /**
     * Stores the last recorded saturation value for each patient.
     * Indexed by patient ID.
     */
    private int[] lastSaturationValues;

    /**
     * Constructs a {@code BloodSaturationDataGenerator} for a given number of patients.
     * Each patient is initialized with a baseline saturation value between 95% and 100%.
     *
     * @param patientCount The number of patients to simulate
     */

    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Initializes with a value between 95 and 100
        }
    }


    /**
     * Generates a new blood saturation reading for the specified patient and sends it using the provided output strategy.
     * <p>
     * The saturation value slightly fluctuates from the last value and remains within the range of 90% to 100%.
     *
     * @param patientId      The ID of the patient for whom to generate data
     * @param outputStrategy The output mechanism to use for delivering the data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Simulate blood saturation values
            int variation = random.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Ensure the saturation stays within a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
                    Double.toString(newSaturationValue) + "%");
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // This will print the stack trace to help identify where the error occurred.
        }
    }
}
