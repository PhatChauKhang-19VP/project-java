package pck.java.be.app.user;

import cyptography.RSA;
import cyptography.StructClass;
import pck.java.be.app.database.DatabaseCommunication;
import pck.java.be.app.database.SelectQuery;
import pck.java.be.app.util.Location;
import pck.java.be.app.util.TreatmentLocation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class Patient extends UserDecorator {
    private int status;
    private LocalDate dob;
    private Location address;
    private TreatmentLocation treatmentLocation;
    private ArrayList<Patient> closeContacts;

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

        if (!this.treatmentLocation.addPatient()) {
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
    public boolean payBill() {
        try {
            _payBill();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } finally {
            return true;
        }
    }

    /**
     * Connect to transaction server and send purchase request.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private void _payBill() throws IOException, InterruptedException {
        String SERVER_IP = "127.0.0.1";
        int SERVER_PORT = 7;

        Socket socket = null;

        try {
            socket = new Socket(SERVER_IP, SERVER_PORT); // Connect to server
            System.out.println("Connected: " + socket);

            HashMap<String, String> requestData = new HashMap<>();
            requestData.put("type", "paybill");
            requestData.put("username", this.getUsername());
            requestData.put("amount", "100");

            String request = StructClass.pack(requestData);

            sendRequest(socket, request);
            String response = receiveServerResponse(socket);
            System.out.println("Response: " + response);
        } catch (IOException ioe) {
            System.out.println("Can???t connect to server");
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
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = formatter.format(new Date());
        String content = "Update status: " + this.status + " -> " + status;
        this.addRecord(date + " " + content);

        // update status and close contacts' status
        this.status = status;
        for (Patient contact : closeContacts) {
            if (contact.status > this.status) {
                contact.setStatus(this.status + 1);
            }
        }
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Location getAddress() {
        return address;
    }

    public void setAddress(Location address) {
        this.address = address;
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
