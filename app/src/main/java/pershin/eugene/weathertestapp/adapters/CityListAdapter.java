package pershin.eugene.weathertestapp.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pershin.eugene.weathertestapp.R;
import pershin.eugene.weathertestapp.entity.weather.CityWeather;

public class CityListAdapter extends ArrayAdapter<CityWeather> {

    private final Activity context;
    private ArrayList<CityWeather> weathersList;

    public CityListAdapter(Activity context, ArrayList<CityWeather> weathersList) {
        super(context, R.layout.rowlayout, weathersList);
        this.context = context;
        this.weathersList = weathersList;
    }

    static class ViewHolder {
        public TextView temp;
        public TextView city;
        public ImageView icon;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.rowlayout, null, true);
            holder = new ViewHolder();
            holder.temp = (TextView) rowView.findViewById(R.id.label);
            holder.city = (TextView) rowView.findViewById(R.id.city);
            holder.icon = (ImageView) rowView.findViewById(R.id.icon);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.temp.setText((weathersList.get(position).getMain().getTemp() + "°"));
        holder.city.setText(weathersList.get(position).getName());
        /*String url = "http://openweathermap.org/img/w/"
                + weathersList.get(position).getWeather().get(0).getIcon() + ".png";
        holder.icon.setImageURI(Uri.parse(url));*/
        String iconName = "icon" + weathersList.get(position).getWeather().get(0).getIcon();
        holder.icon.setImageResource(context.getResources().getIdentifier(iconName, "drawable",
                context.getPackageName()));

        return rowView;
    }
}
