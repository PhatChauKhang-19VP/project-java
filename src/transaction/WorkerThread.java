package transaction;

import cyptography.RSA;
import cyptography.StructClass;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

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

                String encryptedResponse = new String(buffer);
                String decryptedResponse = RSA.getInstance().decrypt(encryptedResponse);

                return decryptedResponse;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String handleClientRequest(String request) {
        HashMap<String, String> requestData = StructClass.unpack(request);

        switch (requestData.get("type")) {
            case "paybill":
                // todo: user payment query should be added here
                return "Username  " + requestData.get("username") + " pay bill with amount = " + requestData.get("amount") + " successfully";

            default:
                return "FAILED";
        }
    }

    private void sendResponse(Socket socket, String rawResponse) {
        try {
            String encryptedResponse = RSA.getInstance().encrypt(rawResponse);

//            System.out.println("server send res\n\t" + encryptedResponse);

            System.out.println();

            DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

            dataOut.writeInt(encryptedResponse.getBytes().length); // write length of the message
            dataOut.write(encryptedResponse.getBytes());
        } catch (Exception e) {
        }
    }

    //Handle client request and send response to client
    public void run() {
        System.out.println("Processing: " + socket);

        //Receive request from client
        String requestData = receiveClientRequest(socket);

        //Process request from client: tra cuu so du, thanh toan,...
        String rawResponse = handleClientRequest(requestData);

        //Send response to client
        sendResponse(socket, rawResponse);
        System.out.println("Complete processing: " + socket);
    }
}