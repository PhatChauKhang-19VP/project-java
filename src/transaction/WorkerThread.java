package transaction;

import app.user.UserConcreteComponent;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class WorkerThread extends Thread {
    private Socket socket;

    public WorkerThread(Socket socket) {
        this.socket = socket;
    }

    private String receiveClientRequest(Socket socket) {
        try {
            DataInputStream dataIn = new DataInputStream(socket.getInputStream());

            int length = dataIn.readInt();  // read length of incoming message
            if (length > 0) {
                byte[] buffer = new byte[length];
                dataIn.readFully(buffer, 0, buffer.length); // read the message

                String requestData = new String(buffer);
                return requestData;
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String handleClientRequest(String request) {
        return "Success";
    }

    private void sendResponse(Socket socket, String response) {
        try {
            byte[] serverResponse = response.getBytes();

            DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

            dataOut.writeInt(serverResponse.length); // write length of the message
            dataOut.write(serverResponse);
        } catch (IOException e) {}
    }

    //Handle client request and send response to client
    public void run() {
        System.out.println("Processing: " + socket);

        //Receive request from client
        String requestData = receiveClientRequest(socket);
        System.out.println(requestData);

        //Process request from client: tra cuu so du, thanh toan,...
        String response = handleClientRequest(requestData);

        //Send response to client
        sendResponse(socket, response);
        System.out.println("Complete processing: " + socket);
    }
}