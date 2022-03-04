package app.user;

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
    public Role getRole() {
        return user.getRole();
    };

    @Override
    public void showInfo() {
        user.showInfo();
    };
}
