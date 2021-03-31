import java.net.*;

public class ConcHTTPAsk {
    public static void main( String[] args) throws Exception {
        
        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
        while(true){
            MyRunnable myRun = new MyRunnable(serverSocket.accept());
            Thread myThread = new Thread(myRun);
            myThread.start();
        }
    }
}

