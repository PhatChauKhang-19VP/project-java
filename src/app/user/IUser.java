package app.user;

import app.util.History;

/** Interface for user class. */
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
    void setRole(Role role);

    String getUsername();
    void setUsername(String username);

    String getName();
    void setName(String name);

    String getPassword();
    void setPassword(String password);

    void setHistory(History history);
    History getHistory();

    /** Add new record to user's history. */
    boolean addRecord(String record);

    void showInfo();
}
