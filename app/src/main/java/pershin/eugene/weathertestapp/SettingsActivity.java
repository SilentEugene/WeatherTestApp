package pershin.eugene.weathertestapp;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

    public static String KEY_PREF_TEMP_UNIT = "temp_units";
    public static String KEY_PREF_WIND_UNIT = "wind_units";
    public static String KEY_PREF_PRESSURE_UNIT = "pressure_units";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsFragment()).commit();

    }
}
