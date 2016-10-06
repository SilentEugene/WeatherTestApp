package pershin.eugene.weathertestapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import pershin.eugene.weathertestapp.adapters.ResultListAdapter;
import pershin.eugene.weathertestapp.entity.city.GeoObject;

import static pershin.eugene.weathertestapp.CitiesActivity.CITY_NEW;
import static pershin.eugene.weathertestapp.CitiesActivity.CITY_NEW_POS;

public class SearchResultActivity extends AppCompatActivity {

    private ArrayList<GeoObject> cities = new ArrayList<>();
    private ResultListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        adapter = new ResultListAdapter(this, cities);
        ListView listView = (ListView) findViewById(R.id.result_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent answerIntent = new Intent();
                answerIntent.putExtra(CITY_NEW, cities.get(position).getAddress());
                answerIntent.putExtra(CITY_NEW_POS, cities.get(position).getPos());
                setResult(RESULT_OK, answerIntent);
                finish();
            }
        });

        new CitiesRequestTask().execute(getIntent().getStringExtra(CITY_NEW));
    }

    private class CitiesRequestTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                String url = "https://geocode-maps.yandex.ru/1.x/?format=json&geocode=" + params[0];
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                String response = restTemplate.getForObject(url, String.class);

                JSONObject jsonObject = new JSONObject(response);

                JSONArray array = jsonObject.getJSONObject("response")
                        .getJSONObject("GeoObjectCollection").getJSONArray("featureMember");

                for (int i=0; i<array.length(); i++) {
                    JSONObject object = array.getJSONObject(i).getJSONObject("GeoObject");
                    String pos = object.getJSONObject("Point").getString("pos");
                    object = object.getJSONObject("metaDataProperty")
                            .getJSONObject("GeocoderMetaData");
                    String name = object.getString("text");
                    String address = object.getJSONObject("AddressDetails").getJSONObject("Country")
                            .getString("AddressLine");
                    cities.add(new GeoObject(name, address, pos));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
        }
    }
}
