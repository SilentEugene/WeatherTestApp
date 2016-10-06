package pershin.eugene.weathertestapp;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import pershin.eugene.weathertestapp.adapters.CityListAdapter;

public class CitiesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public final static String CITY = "weathertestapp.CITY";
    public final static String CITY_NEW = "weathertestapp.CITY_NEW";
    public final static String CITY_NEW_POS = "weathertestapp.CITY_NEW_POS";

    private int CITY_TO_ADD = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        CityListAdapter adapter = new CityListAdapter(this, WeatherWork.weathersList);

        ListView listView = (ListView) findViewById(R.id.list_cities);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent answerIntent = new Intent();
                answerIntent.putExtra(CITY, position);
                setResult(RESULT_OK, answerIntent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.city_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_add_city);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CITY_TO_ADD) {
            if (resultCode == RESULT_OK) {
                Intent answerIntent = new Intent();
                answerIntent.putExtra(CITY_NEW, data.getStringExtra(CITY_NEW));
                answerIntent.putExtra(CITY_NEW_POS, data.getStringExtra(CITY_NEW_POS));
                setResult(RESULT_OK, answerIntent);
                finish();
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra(CITY_NEW, query);
        startActivityForResult(intent, CITY_TO_ADD);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }
}
