import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args) {

     ServerSocket serverSocket = null;
     Socket clientSocket = null;
     final String HTTP_OK = String.format("%s\r\n\r\n", "HTTP/1.1 200 OK");

     try {
       serverSocket = new ServerSocket(4221);
       serverSocket.setReuseAddress(true);
       clientSocket = serverSocket.accept();
       // handle upcomming connection
         InputStream inputStream = clientSocket.getInputStream();
         readData(inputStream);
         clientSocket.getOutputStream().write(HTTP_OK.getBytes());
       System.out.println("accepted new connection");
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }

  public static void readData(InputStream data) {
      // read data
  }


}
