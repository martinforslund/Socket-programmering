import java.net.*;
import java.io.*;
import tcpclient.TCPClient;
import java.lang.Runnable;
public class MyRunnable implements Runnable {

    static int BUFFERSIZE=1024;
    private Socket socket;

    public MyRunnable(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
        int port = 0;
        String hostname = null;
        String toServer = null;

        byte[] fromClientBuffer = new byte[BUFFERSIZE];

    try {
        int fromClientLength = socket.getInputStream().read(fromClientBuffer);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < fromClientLength; i++) {
            sb.append((char) fromClientBuffer[i]);
        }

        String s = sb.toString();
        char[] f = s.toCharArray();

        for (int i = 0; i < f.length - 1; i++) {
            if (f[i] == '?' && f[i - 1] != '?' && f[i + 1] != '?') {
                f[i] = ' ';
            }
            if (f[i] == '=' && f[i - 1] != '=' && f[i + 1] != '=') {
                f[i] = ' ';
            }
            if (f[i] == '&' && f[i - 1] != '&' && f[i + 1] != '&') {
                f[i] = ' ';
            }

        }
        s = new String(f);

        String header = ("HTTP/1.1 200 OK \r\n\r\n");
        String badRequest = ("HTTP/1.1 400 Bad Request\r\n\r\n");
        String notFound = ("HTTP/1.1 404 Not Found\r\n\r\n");

        String[] sa = s.split("[\\s+]");
        /* debug kod
        for (int i=0;i<sa.length;i++){
            System.out.println("STRING: "+i + " " + sa[i]);
        }*/

        if ((sa[6].equals("HTTP/1.1") || sa[8].equals("HTTP/1.1"))) {
            for (int i = 0; i < 7; i++) {
                sa[i] = sa[i].toLowerCase();

                if (sa[i].equals("/favicon.ico")) {
                    break;
                }

                if (sa[i].equals("hostname") && sa[i - 1].equals("/ask")) {
                    hostname = sa[i + 1];
                }

                if (sa[i].equals("port") && sa[i - 2].equals("hostname")) {
                    port = Integer.parseInt(sa[i + 1]);
                }
                if (sa[i].equals("string") && sa[i - 2].equals("port")) {
                    toServer = sa[i + 1].replace("%20", " ");

                }
            }
            if (hostname != null && port != 0) {
                try {
                    s = TCPClient.askServer(hostname, port, toServer);
                    socket.getOutputStream().write(header.getBytes());
                    socket.getOutputStream().write(s.getBytes());
                } catch (Exception e) {
                    socket.getOutputStream().write(notFound.getBytes());
                }
            } else {
                socket.getOutputStream().write(badRequest.getBytes());
            }
        } else {
            socket.getOutputStream().write(badRequest.getBytes());
        }


        socket.close();

    }
    catch(Exception runEx){
        System.out.println("Error");
        }
    }
}