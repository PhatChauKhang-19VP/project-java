package pck.java.be.cryptography;

import java.util.HashMap;

public class StructClass {

    public StructClass() {
    }

    public static String pack(HashMap<String, String> data) {
        String[] keys = data.keySet().toArray(new String[0]);
        String str = "";
        for (int i = 0; i < keys.length - 1; ++i) {
            str += keys[i] + "=" + data.get(keys[i]) + ";";
        }
        int index = keys.length - 1;
        str += keys[index] + "=" + data.get(keys[index]);
        return str;
    }

    public static HashMap<String, String> unpack(String message, String... type) {
        HashMap<String, String> data = new HashMap<>();
        if (type.length == 0) {
            String[] string;
            try {
                string = message.split(";");
                for (int i = 0; i < string.length; i++) {
                    String[] infor = string[i].split("=");
                    if (infor.length == 2) {
                        data.put(infor[0], infor[1]);
                    }
                }
            } catch (Exception e) {
                System.out.println("Unpack = " + message);
            }

        }
        return data;
    }
}

