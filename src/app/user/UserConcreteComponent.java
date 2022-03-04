package app.user;

import app.util.History;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setHistory(History history) {
        this.history = history;
    }

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
        // todo: check isActive
        System.out.println(this.role + " " + this.username + " has logged in.");
        return true;
    }

    @Override
    public boolean logout() {
        System.out.println(this.role + " " + this.username + " has logged out.");
        return true;
    }

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public boolean addRecord(String record) { return history.addRecord(record); }

    @Override
    public History showHistory() {
        return this.history;
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
