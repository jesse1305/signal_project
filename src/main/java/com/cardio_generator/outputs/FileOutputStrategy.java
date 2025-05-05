package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the {@link OutputStrategy} interface that writes output data to files.
 * <p>
 * Each type of data (label) is written to its own file within a specified base directory.
 * Files are automatically created and appended to as data is received.
 */
public class FileOutputStrategy implements OutputStrategy {//fixed name fileOutputStrategy to make it identical to file name

     /**
     * The base directory where output files will be stored.
     */
    private String baseDirectory; // renamed so that it is

    /**
     * A thread-safe map that caches file paths by data label to avoid repeated path construction.
     * The key is the label (e.g., "ECG", "BloodPressure"), and the value is the corresponding file path.
     */
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>(); // renamed file_map to follow camelCase

    /**
     * Constructs a FileOutputStrategy using the specified base directory.
     *
     * @param baseDirectory The directory where output files will be written
     */
    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * Writes the provided data to a file corresponding to the given label.
     * Each entry is appended in the format:
     * <pre>
     * Patient ID: [id], Timestamp: [timestamp], Label: [label], Data: [data]
     * </pre>
     * <p>
     * If the directory or file does not exist, they are created automatically.
     *
     * @param patientId The ID of the patient
     * @param timeStamp The time the data was generated (in milliseconds since epoch)
     * @param label     The label describing the data type
     * @param data      The actual data value to be recorded
     */

    @Override
    public void output(int patientId, long timeStamp, String label, String data) {//renamed timeStamp to be camlecase
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        String filePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());//renamed filePath to be camel case

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timeStamp, label, data);
        } catch (IOException e) {//changed to IOException to be more specific
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}