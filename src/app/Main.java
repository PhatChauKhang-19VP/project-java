package app;

import app.user.Admin;
import app.user.IUser;
import app.user.UserConcreteComponent;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        //System.out.println("Project Lap trinh ung dung Java - 19VP");
        App app = App.getInstance();

        IUser user1 = new UserConcreteComponent("admin1", "a", "a", IUser.Role.ADMIN);
        IUser admin1 = new Admin(user1);

        IUser user2 = new UserConcreteComponent("admin2", "b", "b", IUser.Role.ADMIN);
        IUser admin2 = new Admin(user2);

        // Testing
        ArrayList<IUser> userList = new ArrayList<IUser>();
        userList.add(admin1);
        userList.add(admin2);
        app.setUserList(userList);

        for (IUser i : app.getUserList()) {
            i.login();
            i.logout();
        }
    }
}
