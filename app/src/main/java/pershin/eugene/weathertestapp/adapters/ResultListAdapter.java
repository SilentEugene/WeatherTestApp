package pershin.eugene.weathertestapp.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pershin.eugene.weathertestapp.entity.city.GeoObject;

public class ResultListAdapter extends ArrayAdapter<GeoObject> {

    private final Activity context;
    private ArrayList<GeoObject> result;

    public ResultListAdapter(Activity context, ArrayList<GeoObject> result) {
        super(context, android.R.layout.simple_list_item_1, result);
        this.context = context;
        this.result = result;
    }

    static class ViewHolder {
        public TextView name;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(android.R.layout.simple_list_item_1, null, true);
            holder = new ViewHolder();
            holder.name = (TextView) rowView.findViewById(android.R.id.text1);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.name.setText(result.get(position).getName());

        return rowView;
    }
}
