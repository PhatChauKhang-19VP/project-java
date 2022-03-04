package app.user;

public interface IUser {
    enum Role {
        ADMIN,
        MANAGER,
        PATIENT
    };

    boolean login();
    boolean logout();
    Role getRole();
    void showInfo();
}
