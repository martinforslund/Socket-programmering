import java.net.*;
import java.io.*;

public class HTTPEcho {
    static int BUFFERSIZE=1024;
    public static void main( String[] args) throws IOException {
        ServerSocket welcomeSocket = new ServerSocket(Integer.parseInt(args[0]));

        while (true) {
            Socket socket = welcomeSocket.accept();
            byte[] fromClientBuffer = new byte[BUFFERSIZE];
            int fromClientLength = socket.getInputStream().read(fromClientBuffer);

            StringBuilder sb = new StringBuilder();


            for (int i = 0; i < fromClientLength; i++) {
                sb.append((char)fromClientBuffer[i]);
            }
            String s = sb.toString();
            String header = ("HTTP/1.1 200 OK \r\n\r\n");
            socket.getOutputStream().write(header.getBytes());


            socket.getOutputStream().write(s.getBytes());
            socket.close();



        }
    }
}

