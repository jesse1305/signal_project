package com.cardio_generator.outputs;

/**
 * Interface representing an output strategy for health data.
 * <p>
 * Implementations of this interface define how generated health data is transmitted or stored.
 * Examples include writing to the console, saving to files, or sending via network protocols
 * such as TCP or WebSocket.
 */
public interface OutputStrategy {
    void output(int patientId, long timestamp, String label, String data);
}
