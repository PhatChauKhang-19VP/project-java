package pck.java.be.app.util;

public class District {
    String code, name, name_en;
    Province belong_to;

    public District(String code, String name, String name_en, Province belong_to) {
        this.code = code;
        this.name = name;
        this.name_en = name_en;
        this.belong_to = belong_to;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public Province getBelong_to() {
        return belong_to;
    }

    public void setBelong_to(Province belong_to) {
        this.belong_to = belong_to;
    }

    @Override
    public String toString() {
        return name;
    }
}
