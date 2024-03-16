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
  public static void main(String[] args) {

     ServerSocket serverSocket = null;
     Socket clientSocket = null;
     final String HTTP_OK = String.format("%s\r\n\r\n", "HTTP/1.1 200 OK");
     final String HTTP_NOT_FOUND = String.format("%s\r\n\r\n", "HTTP/1.1 404 Not Found");

     try {
       serverSocket = new ServerSocket(4221);
       serverSocket.setReuseAddress(true);
       clientSocket = serverSocket.accept();
       // handle upcomming connection
         BufferedReader inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
         Boolean isAccepted = isPathValid(inputStream.readLine());
            if (!isAccepted) {
                clientSocket.getOutputStream().write(HTTP_NOT_FOUND.getBytes());
                return;
            } else {
                clientSocket.getOutputStream().write(HTTP_OK.getBytes());

            }
         clientSocket.getOutputStream().write(HTTP_OK.getBytes());
       System.out.println("accepted new connection");
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }


  private static Boolean isPathValid(String line) {
      String pattern = "^(\\S+)\\s+(/\\S*).*";
      Pattern regex  = Pattern.compile(pattern);
      Matcher matcher = regex.matcher(line);

        if (!matcher.matches()) {
            System.out.println("Invalid path");
            return false;
        }
        String path = matcher.group(2);
        if(!path.equals("/")) {
            System.out.println("Invalid path");
            return false;
        }
        return true;
  }


}
