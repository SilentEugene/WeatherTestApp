package pershin.eugene.weathertestapp.db;

import java.util.List;

public interface IDatabaseHandler {
    void addCity(City city);
    City getCity(int id);
    List<City> getAllCities();
    int getCitiesCount();
    int updateCity(City city);
    void deleteCity(City city);
    void deleteAll();
}
