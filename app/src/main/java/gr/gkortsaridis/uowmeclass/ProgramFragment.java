package gr.gkortsaridis.uowmeclass;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramFragment extends Fragment {

    ListView programLV;
    FloatingActionButton addLessonBtn;
    Toolbar toolbar;

    public ProgramFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_program, container, false);

        this.toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(this.toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.hamburger);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addLessonBtn = (FloatingActionButton) view.findViewById(R.id.addLessonBtn);
        programLV = (ListView) view.findViewById(R.id.listviewProgram);

        addLessonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AddLessonToProgram.class));
            }
        });



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        final String[] days = {"Δευτέρα","Τρίτη","Τετάρτη","Πέμπτη","Παρασκευή","Σάββατο","Κυριακή"};

        final MyProgram myprogram = new MyProgram(getActivity());
        List<String> finalInfos = new ArrayList<>();
        for(int i=0; i<myprogram.getTitles().size(); i++){
            finalInfos.add("Κάθε "+days[Integer.parseInt(myprogram.getDay().get(i))]+" στις "+myprogram.getFrom().get(i)+" - "+myprogram.getTo().get(i));
        }
        programLV.setAdapter(new ProgramListAdapter(getActivity(),myprogram.getTitles(),finalInfos));

        programLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if(myprogram.getTitles().get(position).equals("Δευτέρα") || myprogram.getTitles().get(position).equals("Τρίτη") || myprogram.getTitles().get(position).equals("Τετάρτη") || myprogram.getTitles().get(position).equals("Πέμπτη") || myprogram.getTitles().get(position).equals("Παρασκευή") || myprogram.getTitles().get(position).equals("Σάββατο") || myprogram.getTitles().get(position).equals("Κυριακή")){

                }else {
                    myprogram.deleteProgram(getActivity(), position);

                    final MyProgram myprogram1 = new MyProgram(getActivity());
                    List<String> finalInfos = new ArrayList<>();
                    for (int i = 0; i < myprogram1.getTitles().size(); i++) {
                        finalInfos.add("Κάθε " + days[Integer.parseInt(myprogram1.getDay().get(i))] + " στις " + myprogram1.getFrom().get(i) + " - " + myprogram1.getTo().get(i));
                    }
                    programLV.setAdapter(new ProgramListAdapter(getActivity(), myprogram1.getTitles(), finalInfos));
                }
                return false;
            }
        });
    }
}
