package pershin.eugene.weathertestapp.db;

public class City {

    private int _id;
    private int _id_city;
    private String _name;

    public City() {

    }

    public City(int id_city, String name) {
        this._id_city = id_city;
        this._name = name;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public int getIdCity() {
        return _id_city;
    }

    public void setIdCity(int _id) {
        this._id_city = _id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    @Override
    public String toString() {
        return _name + " (ID = " + _id + ")";
    }
}
