package pershin.eugene.weathertestapp;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import pershin.eugene.weathertestapp.db.City;
import pershin.eugene.weathertestapp.db.DatabaseHandler;
import pershin.eugene.weathertestapp.entity.weather.CityWeather;

public class WeatherWork {

    public static ArrayList<CityWeather> weathersList = new ArrayList<>();

    private DatabaseHandler db;
    private MainActivity mainActivity;

    public WeatherWork(MainActivity mainActivity) {

        this.mainActivity = mainActivity;

        db = new DatabaseHandler(this.mainActivity);

        if (db.getCitiesCount() == 0) {
            List<City> cityList = new ArrayList<>();
            cityList.add(new City(524901, "Москва"));
            cityList.add(new City(519690, "Санкт Петербург"));
            cityList.add(new City(1496747, "Новосибирск"));
            cityList.add(new City(1494346, "Екатеринбург"));
            cityList.add(new City(547861, "Казань"));

            for (City city : cityList) {
                db.addCity(city);
            }
        }

        getWeather();
    }

    public void getWeather() {
        weathersList.clear();

        List<City> cityList = db.getAllCities();

        new WeatherRequestTask().execute(cityList);
    }

    public void addCity(String name, String pos) {
        new CityIdRequestTask().execute(name, pos);
    }

    public int getCount() {
        return weathersList.size();
    }

    public CityWeather getCityWeather(int id) {
        return weathersList.get(id);
    }

    private class CityIdRequestTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            try {

                String part1 = "http://api.openweathermap.org/data/2.5/weather/?lat=",
                        part2 = "&units=metric&lang=ru&APPID=40ba60179f1d0650048152f2e65d824d";

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                String lon = params[1].substring(0, params[1].indexOf(" "));
                String lat = params[1].substring(params[1].indexOf(" ") + 1);

                String url = part1 + lat + "&lon=" + lon + part2;

                CityWeather cityWeather = restTemplate.getForObject(url, CityWeather.class);
                if (cityWeather.getCod().equals("200"))
                    db.addCity(new City(Integer.parseInt(cityWeather.getId()), params[0]));
                return db.getCitiesCount() - 1;
            } catch (Exception e) {
                Log.e("WeatherWork", e.getMessage(), e);
            }

            return -1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            mainActivity.setCurrentCity(integer);
        }
    }

    private class WeatherRequestTask extends AsyncTask<List<City>, Void, Void> {

        boolean error = false;

        @Override
        protected Void doInBackground(List<City>... params) {
            try {
                String part1 = "http://api.openweathermap.org/data/2.5/weather/?id=",
                        part2 = "&units=metric&lang=ru&APPID=40ba60179f1d0650048152f2e65d824d";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                for (City city : params[0]) {
                    String url = part1 + city.getIdCity() + part2;
                    CityWeather cityWeather = restTemplate.getForObject(url, CityWeather.class);
                    cityWeather.setName(city.getName());
                    weathersList.add(cityWeather);
                }

                Log.i("WeatherWork", "The weather forecast received for the " + weathersList.size()
                        + " cities");
            } catch (Exception e) {
                Log.e("WeatherWork", e.getMessage(), e);
                error = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!error) {
                mainActivity.showWeather();
            }
        }
    }
}
