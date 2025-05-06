package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates alert data for a given patient based on a probabilistic model.
 * <p>
 * This class simulates a system where alerts can be triggered or resolved for a given patient. Alerts are generated
 * randomly, with a 90% chance to resolve when an alert is active. When no alert is active, there is a probability of
 * generating a new alert based on a specified rate.
 */
public class AlertGenerator implements PatientDataGenerator {


    public static final Random RANDOM_GENERATOR = new Random();//renamed to follow UPPER_SNAKE_CASE cause its a final variable


    private boolean[] alertStates; // false = resolved, true = pressed

     /**
     * Constructs an {@code AlertGenerator} for the specified number of patients.
     * <p>
     * Initializes the {@code alertStates} array to track the alert state for each patient.
     *
     * @param patientCount The number of patients for which alerts will be generated
     */
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];//renamed alertStates to be camel case
    }

      /**
     * Generates an alert for the specified patient based on the current state and outputs the result using the given
     * {@link OutputStrategy}.
     * <p>
     * The method first checks if the patient already has an active alert:
     * <ul>
     *     <li>If the alert is active, there is a 90% chance of resolving it.</li>
     *     <li>If the alert is not active, there is a probability of generating a new alert based on an exponential distribution.</li>
     * </ul>
     * 
     * After determining whether the alert is triggered or resolved, it sends the result using the provided output strategy.
     *
     * @param patientId    The ID of the patient for which the alert is generated
     * @param outputStrategy The strategy used to output the generated data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (RANDOM_GENERATOR.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency CHANGED NAME TO FOLLOW camel case
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
