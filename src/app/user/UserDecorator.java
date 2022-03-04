package app.user;

import app.util.History;

public abstract class UserDecorator implements IUser {
    protected IUser user;

    protected UserDecorator(IUser user) {
        this.user = user;
    }

    @Override
    public boolean login() {
        return user.login();
    };

    @Override
    public boolean logout() {
        return user.logout();
    };

    @Override
    public void deactivate() {
        user.deactivate();
    }

    @Override
    public void activate() {
        user.activate();
    }

    @Override
    public Role getRole() {
        return user.getRole();
    };

    @Override
    public boolean addRecord(String record) { return user.addRecord(record); }

    @Override
    public History showHistory() {
        return this.user.showHistory();
    }

    @Override
    public void showInfo() {
        user.showInfo();
    };
}
