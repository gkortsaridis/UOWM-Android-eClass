package gr.gkortsaridis.uowmeclass;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yoko on 30/04/16.
 */
public class EggrafaListAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> eggrafa;
    ArrayList<String> types;
    LayoutInflater layoutInflater;

    EggrafaListAdapter(Context context, ArrayList<String> eggrafa, ArrayList<String> types){
        this.context = context;
        this.eggrafa = eggrafa;
        this.types = types;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return eggrafa.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class Holder{
        public TextView eggrafo_name;
        public ImageView eggrafo_icon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.eggrafa_list_item, null);
            holder = new Holder();
            holder.eggrafo_name = (TextView) convertView.findViewById(R.id.eggrafoText);
            holder.eggrafo_icon = (ImageView) convertView.findViewById(R.id.eggrafoPic);

            holder.eggrafo_name.setText(eggrafa.get(position));
            if(types.get(position).equals("back")){
                holder.eggrafo_icon.setImageResource(R.drawable.back);
            }
            else if(types.get(position).contains("fileURL")){
                holder.eggrafo_icon.setImageResource(R.drawable.file);
            }else{
                holder.eggrafo_icon.setImageResource(R.drawable.folder);
            }
            //Log.i("Name "+eggrafa.get(position),"Type |"+types.get(position)+"|");
        }

        return convertView;
    }
}
