package app.user;

import app.util.History;

public interface IUser {
    enum Role {
        ADMIN,
        MANAGER,
        PATIENT
    };

    boolean login();
    boolean logout();
    void deactivate();
    void activate();
    Role getRole();
    History showHistory();
    boolean addRecord(String record);

    void showInfo();
}
