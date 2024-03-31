import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Arrays;

public class Main {
  public static void main(String[] args) {
    // Print statements for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    
    try {
      serverSocket = new ServerSocket(4221);
      while (true) {
        serverSocket.setReuseAddress(true);
        clientSocket = serverSocket.accept();
        System.out.println("accepted new connection");
        handleConnection(clientSocket);
      }
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }

  private static void handleConnection(final Socket clientSocket) {
    String foundResponse = "HTTP/1.1 200 OK\r\n\r\n";
    String notFoundResponse = "HTTP/1.1 404 NOT FOUND\r\n\r\n";
    try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream()));
         OutputStream outputStream = clientSocket.getOutputStream()) {
      String path = reader.readLine().split(" ")[1];
      System.out.println("Requested path: " + path);
      if (path.equals("/")) {
        outputStream.write(foundResponse.getBytes());
      } else {
        outputStream.write(notFoundResponse.getBytes());
      }
      outputStream.flush();
    } catch (IOException e) {
      System.err.println("exception occurred. " + e.getMessage());
    }
  }
  private static byte[] encodeOutput(String value) {
    final String CRLF = "\r\n";
    return String.format("%s%s%s", value, CRLF, CRLF).getBytes();
  }
}
