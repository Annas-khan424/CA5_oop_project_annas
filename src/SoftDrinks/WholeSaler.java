package SoftDrinks;

public class WholeSaler {
    String phone;
    String address;
    String country;

    public WholeSaler(String phone, String address, String country){
        this.phone = phone;
        this.address = address;
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "WholeSaler{" +
                "phone=" + phone +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
