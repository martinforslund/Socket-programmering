import java.net.*;
import java.io.*;
import tcpclient.TCPClient;


public class HTTPAsk {
    static int BUFFERSIZE=1024;
    public static void main( String[] args) throws IOException, NumberFormatException {
        ServerSocket welcomeSocket = new ServerSocket(Integer.parseInt(args[0]));


        while (true) {
            int port = 0;
            String hostname = null;
            String toServer = null;
            Socket socket = welcomeSocket.accept();
            byte[] fromClientBuffer = new byte[BUFFERSIZE];
            int fromClientLength = socket.getInputStream().read(fromClientBuffer);

            StringBuilder sb = new StringBuilder();
            
                for (int i = 0; i < fromClientLength; i++) {
                    sb.append((char) fromClientBuffer[i]);
                }

            String s = sb.toString();
               char[] f = s.toCharArray();

                for (int i = 0; i < f.length-1; i++) {
                    if (f[i] == '?' && f[i-1] != '?' && f[i+1] != '?') {
                        f[i] = ' ';
                    }
                    if (f[i] == '=' && f[i-1] != '=' && f[i+1] != '=') {
                        f[i] = ' ';
                    }
                    if (f[i] == '&' && f[i-1] != '&' && f[i+1] != '&') {
                        f[i] = ' ';
                    }

                }
            s = new String(f);

            String header = ("HTTP/1.1 200 OK \r\n\r\n");
            String badRequest = ("HTTP/1.1 400 Bad Request\r\n\r\n");
            String notFound = ("HTTP/1.1 404 Not Found\r\n\r\n");

            String[] sa = s.split("[\\s+]");
            String getreq = "false";
            String httpCheck = "false";



           /* for (int i = 0; i<sa.length; i++) {
                System.out.println("string number: " + i + "; " + sa[i]);
            } debugkod*/

            for (int i = 0; i < sa.length; i++) {
                    sa[i] = sa[i].toLowerCase();

                    if (sa[i].equals("/favicon.ico")) {
                        break;
                    }

                    if(sa[i].equals("get") && sa[i+1].equals("/ask")) {
                        getreq = "true";
                        //System.out.println(getreq);
                    }
                    if(sa[i].equals("http/1.1") && httpCheck.equals("false") && getreq.equals("true") && port != 0 && hostname != null) {
                        httpCheck = "true";
                       //System.out.println(httpCheck);
                        break; //break då man har hittat alla parametrar
                    }
                    if (sa[i].equals("hostname") && hostname == null) {
                        hostname = sa[i + 1];
                    }

                    if (sa[i].equals("port") && port == 0)  {
                        port = Integer.parseInt(sa[i + 1]);

                    }
                    if (sa[i].equals("string") && toServer == null) {
                        toServer = sa[i + 1].replace("%20", " ");
                    }
                }
                if(hostname != null && port != 0 && getreq.equals("true") && httpCheck.equals("true")) {
                    try {
                        if (toServer != null) {
                            s = TCPClient.askServer(hostname, port, toServer);
                        }
                        else {
                            s = TCPClient.askServer(hostname,port);
                        }
                        socket.getOutputStream().write(header.getBytes());
                        socket.getOutputStream().write(s.getBytes());
                    }
                    catch (Exception e) {
                        socket.getOutputStream().write(notFound.getBytes());
                    }
                }
                else {
                    socket.getOutputStream().write(badRequest.getBytes());
                }

            socket.close();

        }
    }
}

