package app.database;

import app.App;
import app.user.Admin;
import app.util.District;
import app.util.Province;
import app.util.TreatmentLocation;
import app.util.Ward;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DatabaseCommunication {
    private static DatabaseCommunication instance;
    private Connection conn;
    private Statement stmt;

    private DatabaseCommunication() {
        try {
            SQLServerDataSource sqlDs = new SQLServerDataSource();

            sqlDs.setIntegratedSecurity(false);
            sqlDs.setEncrypt(false);

            sqlDs.setUser("sa");
            sqlDs.setPassword("Thoai1234");

            sqlDs.setServerName("localhost");
            sqlDs.setPortNumber(1433);
            sqlDs.setDatabaseName("db_covid_app");

            conn = sqlDs.getConnection();
            System.out.println("DB connected");
            stmt = conn.createStatement();
        } catch (SQLServerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseCommunication getInstance() {
        if (instance == null) {
            instance = new DatabaseCommunication();
        }

        return instance;
    }

    public boolean loadAdmin() {
        try {
            ResultSet rs = stmt.executeQuery(" select l.username as 'username',  l.password as 'password', a.name as 'name' from admins as a join login_infos as l on a.username = l.username ");
            while (rs.next()) {
                Admin.getInstance().setUsername(rs.getString("username"));
                Admin.getInstance().setPassword(rs.getString("password"));
                Admin.getInstance().setName(rs.getString("name"));
                break;
                // only 1 admin in the app
            }
            App.getInstance().addUser(Admin.getInstance());

            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    public boolean loadAdministrativeDivision() {
        boolean returnValue = false;

        try {
            // get provinces
            ResultSet rs = stmt.executeQuery("select * from PROVINCES");
            HashMap<String, Province> provinceList = new HashMap<>();
            while (rs.next()) {
                String code = rs.getString("code"),
                        name = rs.getString("full_name"),
                        name_en = rs.getString("full_name_en");

                provinceList.put(code, new Province(code, name, name_en));
            }
            App.getInstance().setProvinceList(provinceList);

            // get district
            rs = stmt.executeQuery("select * from DISTRICTS");
            HashMap<String, District> districtList = new HashMap<>();
            while (rs.next()) {
                String code = rs.getString("code"),
                        name = rs.getString("full_name"),
                        name_en = rs.getString("full_name_en"),
                        province_code = rs.getString("province_code");

                districtList.put(code, new District(code, name, name_en, provinceList.get(province_code)));
            }
            App.getInstance().setDistrictList(districtList);

            // get wards
            rs = stmt.executeQuery("select * from WARDS");
            HashMap<String, Ward> wardList = new HashMap<>();
            while (rs.next()) {
                String code = rs.getString("code"),
                        name = rs.getString("full_name"),
                        name_en = rs.getString("full_name_en"),
                        district_code = rs.getString("district_code");

                wardList.put(code, new Ward(code, name, name_en, districtList.get(district_code)));
            }
            App.getInstance().setWardList(wardList);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    public boolean loadTreatmentLocation() {
        try {
            // get treatment location
            ResultSet rs = stmt.executeQuery("select * from TREATMENT_LOCATIONS as t");
            while (rs.next()) {
                String code = rs.getString("treatment_location_code"),
                        name = rs.getString("name");
                int capacity = rs.getInt("capacity"),
                        current_room = rs.getInt("current_room");
                App.getInstance().addTreatmentLocation(new TreatmentLocation(code, name, capacity, current_room));
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }
}
