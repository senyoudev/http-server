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
       // handle upcomming connection
         BufferedReader inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
         Boolean isAccepted = isPathValid(inputStream.readLine());

         clientSocket.getOutputStream().write(HTTP_OK.getBytes());
       System.out.println("accepted new connection");
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }

  private static void returnResp(Boolean isAccepted, Socket clientSocket) {
      try {
          if (!isAccepted) {
              clientSocket.getOutputStream().write(HTTP_NOT_FOUND.getBytes());
              return;
          } else {
              clientSocket.getOutputStream().write(HTTP_OK.getBytes());
          }
      } catch (IOException e) {
          e.printStackTrace();
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
        String method = matcher.group(1);
        System.out.println("method: " + method);
        if(!path.equals("/")) {
            System.out.println("Invalid path");
            return false;
        }
        return true;
  }


}
