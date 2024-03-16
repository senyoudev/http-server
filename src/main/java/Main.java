import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    final static String HTTP_OK = String.format("%s\r\n\r\n", "HTTP/1.1 200 OK");
    final static String HTTP_NOT_FOUND = String.format("%s\r\n\r\n", "HTTP/1.1 404 Not Found");
  public static void main(String[] args) {

     ServerSocket serverSocket = null;
     Socket clientSocket = null;


     try {
       serverSocket = new ServerSocket(4221);
       serverSocket.setReuseAddress(true);
       clientSocket = serverSocket.accept();
         BufferedReader inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
         String line = inputStream.readLine();
         Boolean isAccepted = isPathValid(line);
         String path = parseRequest(line);
         System.out.println("path "+ path);
         returnResp(isAccepted, clientSocket, path);
       System.out.println("accepted new connection");
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }

  private static void returnResp(Boolean isAccepted, Socket clientSocket,String path) {
      try {
          if (!isAccepted) {
              clientSocket.getOutputStream().write(HTTP_NOT_FOUND.getBytes());
              return;
          } else {
              String response =
                      "HTTP/1.1 200 OK\r\n".concat("Content-Type: text/plain\r\n")
                              .concat(String.format("Content-Length: %d\r\n", path.length()))
                              .concat(String.format("\r\n%s\r\n", path));
                clientSocket.getOutputStream().write(response.getBytes());
          }
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

    private static String parseRequest(String requestLine) {
        String pattern = "^(\\S+)\\s+(/(\\S+)).*";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(requestLine);
        if (!matcher.matches()) {
            System.out.println("INVALID REQUEST");
            return "";
        }
        String fullPath = matcher.group(2).substring(1);
        String[] pathParts = fullPath.split("/", 2);
        return pathParts[1];
    }


    private static boolean isPathValid(String requestLine) {
        String pattern = "^(\\S+)\\s+(/\\S*).*";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(requestLine);
        if (!matcher.matches()) {
            System.out.println("INVALID REQUEST");
            return false;
        }
        String path = matcher.group(2);

            if (!path.equals("/") && !path.contains("/echo")) {
                return false;
            }
            return true;
        }


}
