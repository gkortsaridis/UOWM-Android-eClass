package gr.gkortsaridis.uowmeclass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

public class MathimataFragment extends Fragment {

    ArrayList<Boolean> clickables;
    FloatingActionButton fab;
    ArrayList<String> lessonTitles;
    ArrayList<String> lessonsLink;
    ArrayList<String> lessonsTeacher;
    LinearLayout mathimataContainer;
    Toolbar toolbar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mathimata, container, false);
        this.fab = (FloatingActionButton) view.findViewById(R.id.fab);
        this.mathimataContainer = (LinearLayout) view.findViewById(R.id.mathimataContainer);
        this.toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(this.toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Μαθήματα");
            actionBar.setHomeAsUpIndicator(R.drawable.hamburger);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        this.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"Νέο μάθημα",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://eclass.uowm.gr/modules/auth/courses.php?fc=21"));
                startActivity(intent);
            }
        });

         return view;
    }

    public void onResume() {
        super.onResume();
        getLessons();
    }

    private void getLessons() {
        this.mathimataContainer.removeAllViews();
        this.lessonTitles = new ArrayList();
        this.lessonsTeacher = new ArrayList();
        this.lessonsLink = new ArrayList();
        this.clickables = new ArrayList();
        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(getContext());
        client.setCookieStore(myCookieStore);
        BasicClientCookie newCookie = new BasicClientCookie("cookiesare", "awesome");
        newCookie.setVersion(1);
        newCookie.setDomain("http://eclass.uowm.gr/main/my_courses.php");
        newCookie.setPath("/");
        myCookieStore.addCookie(newCookie);
        client.post("http://eclass.uowm.gr/main/my_courses.php", new AsyncHttpResponseHandler() {
            ProgressDialog dialog;

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String str = new String(responseBody, StandardCharsets.UTF_8);

                Iterator i$ = Jsoup.parse(str).select("table.table-default").select("tbody").select("tr").iterator();
                while (i$.hasNext()) {

                    Element lessonElement = (Element) i$.next();
                    clickables.add(Boolean.valueOf(true));
                    final Elements lessonName = lessonElement.select("td:eq(0)");
                    lessonTitles.add(lessonName.text());
                    final String lessonLink = lessonName.select("strong").select("a[href]").attr("href");
                    lessonsLink.add(lessonLink);
                    final Elements lessonteacher = lessonElement.select("td:eq(1)");
                    lessonsTeacher.add(lessonteacher.text());

                    View card = View.inflate(getContext(), R.layout.mathimata_card, null);
                    TextView lessonTeacher = (TextView) card.findViewById(R.id.cardLessonTeacher);
                    ((TextView) card.findViewById(R.id.cardLessonTitle)).setText(lessonName.text());
                    lessonTeacher.setText(lessonteacher.text());
                    card.setClickable(true);
                    card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(),MathimaActivity.class);
                            intent.putExtra("Kathigitis",lessonteacher.text());
                            intent.putExtra("Link",lessonLink);
                            intent.putExtra("Mathima",lessonName.text());
                            startActivity(intent);
                        }
                    });
                    mathimataContainer.addView(card);
                }
                dialog.dismiss();
            }

            @Override
            public void onStart() {
                dialog = new ProgressDialog(getActivity()); // this = YourActivity
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage("Loading. Please wait...");
                dialog.setIndeterminate(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }


            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
            }
        });



    }
}
