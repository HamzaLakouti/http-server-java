import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {
  public static void main(String[] args) {
    // Print statements for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    
    try {
      serverSocket = new ServerSocket(4221);
      serverSocket.setReuseAddress(true);
      clientSocket = serverSocket.accept(); 

      OutputStream output = clientSocket.getOutputStream();
      String textResponse = "HTTP/1.1 200 OK\r\n\r\n";
      output.write(textResponse.getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}
