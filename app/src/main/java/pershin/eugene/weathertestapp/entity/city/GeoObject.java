package pershin.eugene.weathertestapp.entity.city;

public class GeoObject {

    String name;
    String address;
    String pos;

    public GeoObject() {

    }

    public GeoObject(String name, String address, String pos) {
        this.name = name;
        this.address = address;
        this.pos = pos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
}
