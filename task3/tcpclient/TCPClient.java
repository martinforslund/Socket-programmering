package tcpclient;
import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.lang.*;


public class TCPClient {
    private static int BUFFERSIZE = 1024;
    public static String askServer(String hostname, int port, String ToServer) throws IOException {
        if (ToServer == null) {
            return askServer(hostname, port); //skicka ner till andra funktionen om man inte har skickat med något till servern.
        }
        int timeout = 4000;
        byte[] fromUserBuffer = new byte[BUFFERSIZE];
        byte[] fromServerBuffer = new byte[BUFFERSIZE];

        fromUserBuffer = ToServer.getBytes(StandardCharsets.UTF_8);

        Socket socket = new Socket(hostname, port);


        OutputStream output = socket.getOutputStream();
        output.write(fromUserBuffer);
        output.write('\n');

        InputStream input = socket.getInputStream();


        StringBuilder sb = new StringBuilder();
        long st = System.currentTimeMillis();
        long duration = System.currentTimeMillis();
        do  {
                int fromServerLength = input.read(fromServerBuffer);
                sb.append(new String(fromServerBuffer, 0, fromServerLength, StandardCharsets.UTF_8));

            } while (input.available() > 0 && (timeout > (st-duration))); //while loop for att skicka ut mer än som finns plats på arrayen.


        socket.close();
        return sb.toString();

    }

    public static String askServer(String hostname, int port) throws IOException {
        Socket socket = new Socket(hostname,port);
        
        byte[] fromServerBuffer = new byte[BUFFERSIZE];

        InputStream input = socket.getInputStream();
        StringBuilder sb = new StringBuilder();
        int timeout = 4000;
        long st = System.currentTimeMillis();
        long duration = System.currentTimeMillis();
            do {

                    int fromServerLength = input.read(fromServerBuffer);
                    sb.append(new String(fromServerBuffer, 0, fromServerLength, StandardCharsets.UTF_8));
                    duration = System.currentTimeMillis();

            } while (input.available() > 0 && (timeout > (st-duration)));


        socket.close();
        return sb.toString();
    }
}

