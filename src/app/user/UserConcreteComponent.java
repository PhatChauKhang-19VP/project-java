package app.user;

import app.util.History;

/** Concrete user class. */
public class UserConcreteComponent implements IUser {
    private String username, name, password;
    private Role role;
    private History history;
    private boolean isActive;

    public UserConcreteComponent() {
        username = name = password = null;
        role = Role.PATIENT;
        history = null;
        isActive = true;
    }

    public UserConcreteComponent(String username, String name, String password, Role role) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.role = role;
        history = new History();
        isActive = true;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public History getHistory() {
        return this.history;
    }

    @Override
    public void setHistory(History history) {
        this.history = history;
    }

    @Override
    public boolean addRecord(String record) { return history.addRecord(record); }

    @Override
    public void deactivate() {
        isActive = false;
    }

    @Override
    public void activate() {
        isActive = true;
    }

    @Override
    public boolean login() {
        if (isActive) {
            System.out.println(this.role + " " + this.username + " has logged in.");
            return true;
        } else {
            System.out.println("This account has been deactivated.");
            return false;
        }

    }

    @Override
    public boolean logout() {
        System.out.println(this.role + " " + this.username + " has logged out.");
        return true;
    }

    @Override
    public void showInfo() {
        System.out.println("Name: " + name);
        System.out.println("Username: " + username);
        System.out.println("Role: " + role);
        System.out.println("History:");
        history.show();
    }
}
