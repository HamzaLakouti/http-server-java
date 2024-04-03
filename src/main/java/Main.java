import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import model.Request;


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
        String requestStr = parseInput(clientSocket.getInputStream());
        System.out.println("Parsing request:\n" + requestStr);
        var request = Request.from(requestStr);
        var outputStream = clientSocket.getOutputStream();
        var response = getResponse(request);
        System.out.println("Response:\n" + response);
        outputStream.write(response.getBytes());
        clientSocket.close();
      }
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }

  public static String parseInput(InputStream in) throws IOException {
    BufferedReader reader =
            new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
    StringBuilder requestBuilder = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null && !line.isEmpty()) {
      requestBuilder.append(line).append("\r\n");
    }
    return requestBuilder.toString();
  }

  public static String getResponse(Request request) {
    String path = request.path();
    if (path.equals("/")) {
      return "HTTP/1.1 200 OK\r\n\r\n";
    } else if (path.startsWith("/echo/")) {
      String echoValue = path.substring(6);
      return "HTTP/1.1 200 OK\r\n" +
              "Content-Type: text/plain\r\n" +
              "Content-Length: " + echoValue.length() + "\r\n\r\n" +
              echoValue + "\r\n";
    } else if (path.startsWith("/user-agent")) {
      String userAgent = request.headers().get("User-Agent");
      return "HTTP/1.1 200 OK\r\n" +
              "Content-Type: text/plain\r\n" +
              "Content-Length: " + userAgent.length() + "\r\n\r\n" +
              userAgent + "\r\n";
    } else {
      return "HTTP/1.1 404 Not Found\r\n\r\n";
    }
  }
}
