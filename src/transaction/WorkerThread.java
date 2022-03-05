package transaction;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class WorkerThread extends Thread {
    private Socket socket;

    public WorkerThread(Socket socket) {
        this.socket = socket;
    }

    private String handleClientRequest(String request) {
        return "Success";
    }

    public void processData() {
        System.out.println("Processing: " + socket);
        try {
            DataInputStream dataIn = new DataInputStream(socket.getInputStream());

            int length = dataIn.readInt();  // read length of incoming message
            if(length > 0) {
                byte[] buffer = new byte[length];
                dataIn.readFully(buffer, 0, buffer.length); // read the message

                String requestData = new String(buffer);

                //process request from client: tra cuu so du, thanh toan,...
                String response = handleClientRequest(requestData);
            }
        } catch (IOException e) {
            System.err.println("Request Processing Error: " + e);
        }
        System.out.println("Complete processing: " + socket);
    }
}
