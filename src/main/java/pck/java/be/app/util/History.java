package pck.java.be.app.util;

import java.util.ArrayList;

/** Info log for all users. */
public class History {
    private ArrayList<String> history;

    public History() {
        history = new ArrayList<String>();
    }

    public History(ArrayList<String> history) {
        this.history = history;
    }

    public boolean addRecord(String record) {
        try {
            history.add(record);
        } catch (Exception e) {
            System.out.printf("Exception adding new record: " + e);
            return false;
        } finally {
            return true;
        }
    }

    public void show() {
        for (String s : history) {
            System.out.println(s);
        }
    }

    @Override
    public String toString() {
        return "History: " + history.toString();
    }
}
