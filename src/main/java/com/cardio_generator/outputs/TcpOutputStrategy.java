package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;


/**
 * Implementation of the {@link OutputStrategy} interface that sends output data over a TCP socket.
 * <p>
 * This class starts a TCP server on the specified port and waits for a client to connect.
 * Once connected, all output data is sent to the connected client as comma-separated values.
 */
public class TcpOutputStrategy implements OutputStrategy {

    /**
     * The server socket that listens for incoming client connections.
     */
    private ServerSocket serverSocket;

    /**
     * The socket representing the connected client.
     */
    private Socket clientSocket;

    /**
     * The output stream used to send data to the client.
     */
    private PrintWriter out;

    /**
     * Constructs a {@code TcpOutputStrategy} and starts a TCP server on the given port.
     * <p>
     * The server accepts a single client connection asynchronously.
     *
     * @param port The TCP port to listen on
     */

    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the provided patient data to the connected TCP client in the format:
     * <pre>
     * patientId,timestamp,label,data
     * </pre>
     * <p>
     * If no client is connected, this method does nothing.
     *
     * @param patientId The ID of the patient
     * @param timestamp The time the data was generated (in milliseconds since epoch)
     * @param label     The label describing the data type
     * @param data      The actual data value to send
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
