package pershin.eugene.weathertestapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import pershin.eugene.weathertestapp.entity.weather.CityWeather;

import static java.lang.Integer.parseInt;
import static pershin.eugene.weathertestapp.CitiesActivity.CITY_NEW;
import static pershin.eugene.weathertestapp.CitiesActivity.CITY_NEW_POS;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SwipeRefreshLayout.OnRefreshListener {

    static final private int CHOOSE_CITY = 0;

    private int currentCity = 0;

    static public WeatherWork weatherWork;

    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setProgressViewOffset(false, 0, 120);
        swipeRefreshLayout.setOnRefreshListener(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (weatherWork == null) {
            weatherWork = new WeatherWork(this);
        } else if (weatherWork.getCount() > 0) {
            showWeather();
        }
    }

    public void showWeather(int cityId) {
        currentCity = cityId;
        showWeather();
    }

    public void showWeather() {

        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);

        CityWeather cityWeather = weatherWork.getCityWeather(currentCity);

        ((TextView) findViewById(R.id.tvCity)).setText(cityWeather.getName());
        ((TextView) findViewById(R.id.tvTemp))
                .setText(getTemp(cityWeather));

        String iconName = "icon" + cityWeather.getWeather().get(0).getIcon();
        ((ImageView) findViewById(R.id.icon_main)).setImageResource(getResources()
                .getIdentifier(iconName, "drawable", getPackageName()));

        ((TextView) findViewById(R.id.tvWeather)).setText(cityWeather.getWeather()
                .get(0).getDescription());
        ((TextView) findViewById(R.id.tvPressure))
                .setText(getPressure(cityWeather));

        ((TextView) findViewById(R.id.tvHumidity))
                .setText((cityWeather.getMain().getHumidity() + " %"));

        ((TextView) findViewById(R.id.tvWind)).setText(getWind(cityWeather));

        ((TextView) findViewById(R.id.tvClouds))
                .setText((cityWeather.getClouds().getAll() + " %"));

        ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cities) {
            showCitiesList();
        } else if (id == R.id.nav_settings) {
            showSettings();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TextView tvCity = (TextView) findViewById(R.id.tvCity);

        if (requestCode == CHOOSE_CITY) {
            if (resultCode == RESULT_OK) {
                int id = data.getIntExtra(CitiesActivity.CITY, -1);
                if (id > -1)
                    showWeather(id);
                else {
                    String name = data.getStringExtra(CITY_NEW);
                    String pos = data.getStringExtra(CITY_NEW_POS);
                    weatherWork.addCity(name, pos);
                }
            }
        }
    }

    public void setCurrentCity(int currentCity) {
        this.currentCity = currentCity;
        weatherWork.getWeather();
    }

    private void showCitiesList() {
        Intent intent = new Intent(this, CitiesActivity.class);
        startActivityForResult(intent, CHOOSE_CITY);
    }

    private void showSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        weatherWork.getWeather();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (weatherWork.getCount() > 0) {
            showWeather();
        }
    }

    public String getTemp(CityWeather cityWeather) {
        String tempValue = cityWeather.getMain().getTemp();
        int tempUnit = parseInt(prefs.getString(SettingsActivity.KEY_PREF_TEMP_UNIT, "0"));
        switch (tempUnit) {
            case 0:
                tempValue += "°";
                break;
            case 1:
                String tempF = String.valueOf(1.8*Float.parseFloat(tempValue) + 32);
                tempValue = tempF + "°";
                break;
        }
        return tempValue;
    }

    public String getPressure(CityWeather cityWeather) {
        String pressureValue = cityWeather.getMain().getPressure();
        int pressureUnit = Integer.parseInt(prefs.getString(SettingsActivity
                .KEY_PREF_PRESSURE_UNIT,"0"));
        switch (pressureUnit) {
            case 0:
                String pressureHg = String.valueOf(
                        Math.round(Float.parseFloat(pressureValue) * 0.75006));
                pressureValue = pressureHg + " "
                        + getResources().getStringArray(R.array.pressure_unit)[pressureUnit];
                break;
            case 1:
                pressureValue = pressureValue + " "
                        + getResources().getStringArray(R.array.pressure_unit)[pressureUnit];
                break;
        }
        return pressureValue;
    }

    private String getWind(CityWeather cityWeather) {
        String wind = String.valueOf(Math.round(Float.parseFloat(cityWeather.getWind().getSpeed())));
        int windUnit = parseInt(prefs.getString(SettingsActivity.KEY_PREF_WIND_UNIT, "0"));
        switch (windUnit) {
            case 0:
                wind = wind + " " + getResources().getStringArray(R.array.wind_unit)[windUnit]
                        + ", ";
                break;
            case 1:
                wind = String.valueOf(Math.round(Float.parseFloat(wind) * 2.236936f));
                wind = wind + " " + getResources().getStringArray(R.array.wind_unit)[windUnit]
                        + ", ";
                break;
        }

        int direction = (int) Float.parseFloat(cityWeather
                .getWind().getSpeed());
        wind += getResources()
                .getStringArray(R.array.cardinal_direction)[direction % 6];

        return wind;
    }
}
