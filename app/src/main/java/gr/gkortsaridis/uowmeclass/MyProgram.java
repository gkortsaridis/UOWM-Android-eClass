package gr.gkortsaridis.uowmeclass;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by yoko on 13/11/2016.
 */

public class MyProgram {

    List<String> title;
    List<String> from;
    List<String> to;
    List<String> day;

    public MyProgram(Context context){
        SharedPreferences mPrefs = context.getSharedPreferences("SAVE", Context.MODE_PRIVATE);

        title = new ArrayList<>();
        from = new ArrayList<>();
        to = new ArrayList<>();
        day = new ArrayList<>();

        Gson gson = new Gson();
        String titlesStr = mPrefs.getString("titles", "");
        String fromsStr = mPrefs.getString("froms", "");
        String tosStr = mPrefs.getString("tos", "");
        String daysStr = mPrefs.getString("days", "");
        if(!titlesStr.equals("")) {
            title = gson.fromJson(titlesStr, List.class);
            from = gson.fromJson(fromsStr, List.class);
            to = gson.fromJson(tosStr, List.class);
            day = gson.fromJson(daysStr, List.class);

            Log.i("title length",title.size()+"");
        }else{
            title.add("Κυριακή");
            title.add("Σάββατο");
            title.add("Παρασκευή");
            title.add("Πέμπτη");
            title.add("Τετάρτη");
            title.add("Τρίτη");
            title.add("Δευτέρα");

            from.add("");
            from.add("");
            from.add("");
            from.add("");
            from.add("");
            from.add("");
            from.add("");

            to.add("");
            to.add("");
            to.add("");
            to.add("");
            to.add("");
            to.add("");
            to.add("");

            day.add("6");
            day.add("5");
            day.add("4");
            day.add("3");
            day.add("2");
            day.add("1");
            day.add("0");
        }


    }

    public List<String> getTitles(){
        return title;
    }

    public List<String> getFrom(){
        return from;
    }

    public List<String> getTo(){
        return to;
    }

    public List<String> getDay(){
        return day;
    }

    public void addProgram(Context context, String title, String from, String to, String day){

        boolean flag = false;
        for(int i=0; i<this.day.size(); i++){
            if(Integer.parseInt(day) > Integer.parseInt(this.day.get(i))){
                Log.i("Bigger","add");
                this.title.add(i,title);
                this.from.add(i,from);
                this.to.add(i,to);
                this.day.add(i,day);
                flag = true;
                break;
            }
        }

        if(!flag){
            Log.i("same day","add");
            this.title.add(title);
            this.from.add(from);
            this.to.add(to);
            this.day.add(day);
        }

        SharedPreferences mPrefs = context.getSharedPreferences("SAVE", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String titlesjson = gson.toJson(this.title);
        String fromjson = gson.toJson(this.from);
        String tojson = gson.toJson(this.to);
        String dayjson = gson.toJson(this.day);
        prefsEditor.putString("titles", titlesjson);
        prefsEditor.putString("froms", fromjson);
        prefsEditor.putString("tos", tojson);
        prefsEditor.putString("days", dayjson);
        prefsEditor.commit();

    }


    public void deleteProgram(Context context, int where){
        this.title.remove(where);
        this.from.remove(where);
        this.to.remove(where);
        this.day.remove(where);

        SharedPreferences mPrefs = context.getSharedPreferences("SAVE", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String titlesjson = gson.toJson(this.title);
        String fromjson = gson.toJson(this.from);
        String tojson = gson.toJson(this.to);
        String dayjson = gson.toJson(this.day);
        prefsEditor.putString("titles", titlesjson);
        prefsEditor.putString("froms", fromjson);
        prefsEditor.putString("tos", tojson);
        prefsEditor.putString("days", dayjson);
        prefsEditor.commit();
    }
}
