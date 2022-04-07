package pck.java.be.app.user;

import pck.java.be.app.util.History;

/**
 * Abstract class for decorators.
 */
public abstract class UserDecorator implements IUser {
    protected IUser user;

    protected UserDecorator() {
    }

    protected UserDecorator(IUser user) {
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public void setUsername(String username) {
        user.setUsername(username);
    }

    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public void setName(String name) {
        user.setName(name);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public void setPassword(String password) {
        user.setPassword(password);
    }

    @Override
    public Role getRole() {
        return user.getRole();
    }

    @Override
    public void setRole(Role role) {
        user.setRole(role);
    }

    @Override
    public History getHistory() {
        return this.user.getHistory();
    }

    @Override
    public void setHistory(History history) {
        user.setHistory(history);
    }

    @Override
    public boolean addRecord(String record) {
        return user.addRecord(record);
    }

    @Override
    public boolean login() {
        return user.login();
    }

    ;

    @Override
    public boolean logout() {
        return user.logout();
    }

    ;

    @Override
    public void deactivate() {
        user.deactivate();
    }

    @Override
    public void activate() {
        user.activate();
    }

    @Override
    public void showInfo() {
        user.showInfo();
    }

    ;
}
