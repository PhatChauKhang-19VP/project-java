package pck.java.be.app.util;

public class Province {
    private String code, name, name_en;

    public Province(String code, String name, String name_en) {
        this.code = code;
        this.name = name;
        this.name_en = name_en;
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

    @Override
    public String toString() {
        return name;
    }
}
