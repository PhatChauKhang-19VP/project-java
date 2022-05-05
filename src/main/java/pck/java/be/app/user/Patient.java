package pck.java.be.app.user;

import cyptography.RSA;
import cyptography.StructClass;
import pck.java.database.DatabaseCommunication;
import pck.java.database.SelectQuery;
import pck.java.be.app.util.Location;
import pck.java.be.app.util.TreatmentLocation;
import pck.java.be.cryptography.RSA;
import pck.java.be.cryptography.StructClass;
import pck.java.database.DatabaseCommunication;
import pck.java.database.SelectQuery;
import pck.java.database.UpdateQuery;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class Patient extends UserDecorator {
    private int status;
    private LocalDate dob;
    private Location address;
    private TreatmentLocation treatmentLocation;
    private ArrayList<Patient> closeContacts;
    private double debitBalance = 0;

    public Patient(IUser user) {
        super(user);
        this.status = 0;
        this.dob = null;
        this.address = null;
        this.treatmentLocation = null;
        this.closeContacts = null;
    }

    public Patient(
            IUser user,
            int status,
            LocalDate dob,
            Location address,
            TreatmentLocation treatmentLocation,
            ArrayList<Patient> closeContacts
    ) {
        super(user);
        this.status = status;
        this.dob = dob;
        this.address = address;
        this.treatmentLocation = treatmentLocation;

        if (this.treatmentLocation != null && treatmentLocation.getCurrentRoom() == treatmentLocation.getCapacity()) {
            this.treatmentLocation = null;
        }

        this.closeContacts = new ArrayList<Patient>(closeContacts);
    }


    public void viewPackages() {
        try {
            SelectQuery selectQuery = new SelectQuery();
            selectQuery.select("*").from("PACKAGES");
            List<Map<String, Object>> rs = DatabaseCommunication.getInstance().executeQuery(selectQuery.getQuery());
            DatabaseCommunication.getInstance().printResult(rs);
        } catch (Exception e) {
            System.out.println("Exception showing packages: " + e.getMessage());
        } finally {
        }
    }

    public boolean buyPackage() {
        try {
            // todo: buy package
        } catch (Exception e) {
            System.out.println("Exception buying package: " + e.getMessage());
            return false;
        } finally {
            return true;
        }
    }


    /**
     * Interfaces that be called by client to pay bill.
     *
     * @return true if successfully purchase.
     */
    public boolean payBill(String password, int amount) {
        try {
            return _payBill(password, amount);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Connect to transaction server and send purchase request.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private boolean _payBill(String password, int amount) throws IOException, InterruptedException {
        String SERVER_IP = "127.0.0.1";
        int SERVER_PORT = 7;

        Socket socket = null;

        try {
            socket = new Socket(SERVER_IP, SERVER_PORT); // Connect to server
            System.out.println("Connected: " + socket);

            HashMap<String, String> requestData = new HashMap<>();
            requestData.put("type", "paybill");
            requestData.put("username", this.getUsername());
            requestData.put("password", password);
            requestData.put("amount", String.valueOf(amount));

            String request = StructClass.pack(requestData);

            sendRequest(socket, request);
            String response = receiveServerResponse(socket);

            HashMap<String, String> responseData = StructClass.unpack(response);

            System.out.println("Response: " + responseData);

            return responseData.get("status").equals("success");
        } catch (IOException ioe) {
            System.out.println("Can’t connect to server");

            return false;
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        // add to history
        // format date to save update history
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = formatter.format(new Date());
        String content = "Update status: " + this.status + " -> " + status;
        this.addRecord(date + " " + content);
        DatabaseCommunication.getInstance().savePatientHistory(this,
                "CHANGE_STATUS;Chuyển từ f" + this.status + " -> f" + status);

        // update status and close contacts' status
        this.status = status;
        updatePatient();
        for (Patient contact : closeContacts) {
            if (contact.status > this.status) {
                contact.setStatus(this.status + 1);
            }
        }
    }

    public String getStatusAsString() {
        return "F" + status;
    }

    public boolean updatePatient() {
        UpdateQuery uq = new UpdateQuery();

        uq.update("PATIENTS")
                .set("f_status = " + this.getStatus())
                .where("username = '" + this.getUsername() + "'");

        try {
            DatabaseCommunication.getInstance().execute(uq.getQuery());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getDobAsString() {
        return dob.toString();
    }

    public Location getAddress() {
        return address;
    }

    public void setAddress(Location address) {
        this.address = address;
    }

    public String getAddressAsString() {
        return address.getAddressLine() + ",\n"
                + address.getWard() + ",\n"
                + address.getDistrict() + ",\n"
                + address.getProvince();
    }

    public ArrayList<Patient> getCloseContacts() {
        return closeContacts;
    }

    public void setCloseContacts(ArrayList<Patient> closeContacts) {
        this.closeContacts = closeContacts;
    }

    public void addCloseContact(Patient newCloseContact) {
        if (this.status + 1 != newCloseContact.status) {
            newCloseContact.setStatus(this.status + 1);
        }
        this.closeContacts.add(newCloseContact);
    }

    public TreatmentLocation getTreatmentLocation() {
        return treatmentLocation;
    }

    public void setTreatmentLocation(TreatmentLocation treatmentLocation) {
        this.treatmentLocation = treatmentLocation;
    }

    public String getTreatmentLocationAsString() {
        return treatmentLocation.toString();
    }

    /**
     * More interpretable way to display patient's information.
     */
    public void showCompactInfo() {
        System.out.print("Username: " + ((UserConcreteComponent) this.user).getUsername());
        System.out.println("\tStatus: F" + status);
    }

    @Override
    public void showInfo() {
        super.showInfo();
        System.out.println("Status: F" + status);
        System.out.println("DOB: " + dob);
        System.out.println("Address: " + address);
        System.out.println("Treatment Location: " + treatmentLocation);
        System.out.println("Close contacts:");
        for (Patient contact : closeContacts) {
            System.out.print("\t");
            contact.showCompactInfo();
        }
    }

    private String receiveServerResponse(Socket socket) {
        try {
            DataInputStream dataIn = new DataInputStream(socket.getInputStream());

            int length = dataIn.readInt();  // read length of incoming message
            if (length > 0) {
                byte[] buffer = new byte[length];
                dataIn.readFully(buffer, 0, buffer.length); // read the message

//                System.out.println("client receive res\n\t" + new String(buffer));

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

    private void sendRequest(Socket socket, String rawRequest) {
        try {
            String encryptedRequest = RSA.getInstance().encrypt(rawRequest);

//            System.out.println("Client send request\n\t" + encryptedRequest);

            DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

            dataOut.writeInt(encryptedRequest.getBytes().length); // write length of the message
            dataOut.write(encryptedRequest.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
