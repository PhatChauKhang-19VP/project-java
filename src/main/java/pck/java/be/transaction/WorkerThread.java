package pck.java.be.transaction;

import pck.java.be.cryptography.StructClass;
import pck.java.be.cryptography.RSA;
import pck.java.database.DatabaseCommunication;
import pck.java.database.SelectQuery;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private String handleClientRequest(String request){
        HashMap<String, String> requestData = StructClass.unpack(request);
        HashMap<String, String> responseData = new HashMap<>();
        switch (requestData.get("type")) {
            case "paybill":
                DatabaseCommunication dbc = DatabaseCommunication.getInstance();

                SelectQuery sq = new SelectQuery();
                sq.select("*").from("LOGIN_INFOS").where("username", "'" + requestData.get("username") + "'")
                        .where("password", "HASHBYTES('SHA2_512', '" + requestData.get("password") + "')").where("account_status='ACTIVE'");

                System.out.println(sq.getQuery());

                List<Map<String, Object>> rs = null;
                try {
                    rs = dbc.executeQuery(sq.getQuery());
                } catch (SQLException e) {
                    e.printStackTrace();
                    responseData.clear();
                    responseData.put("status", "failed");
                    responseData.put("msg", "db error");

                    return StructClass.pack(responseData);
                }
                if (rs.size() == 0) {
                    responseData.clear();
                    responseData.put("status", "failed");
                    responseData.put("msg", "login failed");

                    return StructClass.pack(responseData);
                }
                requestData.clear();
                responseData.put("status", "success");
                responseData.put("amount", requestData.get("amount"));
                responseData.put("new balance", "<NEW BALACE>");
                return StructClass.pack(responseData);

            default:
                responseData.put("status", "failed");

                return StructClass.pack(responseData);

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