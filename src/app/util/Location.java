package app.util;

public class Location {
    private String addressLine, ward, district, province;

    public Location() {
        addressLine = ward = district = province = null;
    }

    public Location(String addressLine, String ward, String district, String province) {
        this.addressLine = addressLine;
        this.ward = ward;
        this.district = district;
        this.province = province;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return addressLine +
                ", Ward " + ward +
                ", District " + district +
                ", " + province;
    }
}
