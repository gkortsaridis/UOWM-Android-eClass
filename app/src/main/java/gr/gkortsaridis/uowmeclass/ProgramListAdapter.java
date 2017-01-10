package gr.gkortsaridis.uowmeclass;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoko on 14/11/2016.
 */

public class ProgramListAdapter extends BaseAdapter {

    private Activity activity;
    private List<String> titles,infos;
    private static LayoutInflater inflater=null;

    public ProgramListAdapter(Activity a, List<String> titles, List<String> infos) {
        activity = a;
        this.titles = titles;
        this.infos = infos;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return titles.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;

        if(titles.get(position).equals("Δευτέρα") || titles.get(position).equals("Τρίτη") || titles.get(position).equals("Τετάρτη") || titles.get(position).equals("Πέμπτη") || titles.get(position).equals("Παρασκευή") || titles.get(position).equals("Σάββατο") || titles.get(position).equals("Κυριακή")){
            if(convertView==null) vi = inflater.inflate(R.layout.simple_day_list_item, null);

            TextView day = (TextView) vi.findViewById(R.id.simpleDay);
            day.setText(titles.get(position));

        }else{
            if(convertView==null) vi = inflater.inflate(R.layout.program_list_item, null);

            TextView type = (TextView) vi.findViewById(R.id.lessonTitle);
            TextView date = (TextView) vi.findViewById(R.id.lessonDay);

            type.setText(titles.get(position));
            date.setText(infos.get(position));
        }
        return vi;
    }
}
