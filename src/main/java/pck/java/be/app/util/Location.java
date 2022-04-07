package pck.java.be.app.util;

public class Location {
    private String addressLine;
    Ward ward;
    District district;
    Province province;

    public Location() {
        addressLine = null;
        ward = null;
        district = null;
        province = null;
    }

    public Location(String addressLine, Ward ward, District district, Province province) {
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

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return addressLine +
                ", " + ward +
                ", " + district +
                ", " + province;
    }
}
