package app.user;

public class UserConcreteComponent implements IUser {
    private String username, name, password;
    private Role role;

    public UserConcreteComponent() {
        username = name = password = null;
        role = Role.PATIENT;
    }

    public UserConcreteComponent(String username, String name, String password, Role role) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    @Override
    public boolean login() {
        System.out.println(this.role + " " + this.username + " has logged in.");
        return true;
    };

    @Override
    public boolean logout() {
        System.out.println(this.role + " " + this.username + " has logged out.");
        return true;
    };

    @Override
    public Role getRole() {
        return role;
    };

    @Override
    public void showInfo() {
        System.out.println("Username: " + username);
    };
}
