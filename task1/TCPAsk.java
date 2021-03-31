import java.net.*;
import java.io.*;
import tcpclient.TCPClient;

 public class TCPAsk {
    public static void main( String[] args) {
        String hostname = null;
        int port = 0;
        String userInput = null;

        try {
            hostname = args[0];
            port = Integer.parseInt(args[1]);
            if (args.length >= 3) {
                // Collect remaining arguments into a single string
                StringBuilder builder = new StringBuilder();
                boolean first = true;
                for (int i = 2; i < args.length; i++) {
                    if (first)
                        first = false;
                    else
                        builder.append(" ");
                    builder.append(args[i]);
                }
                builder.append("\n");
                userInput = builder.toString();
            }
        }
        catch (Exception ex) {
            System.err.println("Usage: TCPAsk host port <data to server>");
            System.exit(1);
        }
        try {
            String serverOutput;
            if (userInput != null)
                serverOutput = TCPClient.askServer(hostname, port, userInput);
            else
                serverOutput = TCPClient.askServer(hostname, port);                
            System.out.printf("%s:%d says:\n%s", hostname, port, serverOutput);
        } catch(IOException ex) {
            System.err.println(ex);
            System.exit(1);
        }
    }
}

