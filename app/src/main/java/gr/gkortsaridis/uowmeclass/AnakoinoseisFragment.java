package gr.gkortsaridis.uowmeclass;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

public class AnakoinoseisFragment extends Fragment {
    LinearLayout anakoinoseisContainer;
    Toolbar toolbar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void onResume() {
        super.onResume();
        getAnakoinoseis();
    }

    public void getAnakoinoseis(){
        final ArrayList<String> anakoinoseisTitles = new ArrayList();
        final ArrayList<String> anakoinoseisMathima = new ArrayList();
        final ArrayList<String> anakoinoseisWhen = new ArrayList();
        final ArrayList<String> anakoinoseisLink = new ArrayList();

        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(getContext());
        client.setCookieStore(myCookieStore);
        BasicClientCookie newCookie = new BasicClientCookie("cookiesare", "awesome");
        newCookie.setVersion(1);
        newCookie.setDomain("http://eclass.uowm.gr/modules/announcements/myannouncements.php");
        newCookie.setPath("/");
        myCookieStore.addCookie(newCookie);
        client.post("http://eclass.uowm.gr/modules/announcements/myannouncements.php", new AsyncHttpResponseHandler() {
            ProgressDialog dialog;

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String str = new String(responseBody, StandardCharsets.UTF_8);

                Iterator i$ = Jsoup.parse(str).select("li.list-item").iterator();
                while (i$.hasNext()) {
                    Element anakoinosi = (Element) i$.next();
                    final Elements anakoinosiTitle = anakoinosi.select("div.text-title");
                    anakoinoseisTitles.add(anakoinosiTitle.text());
                    final String anakoinosiLink = anakoinosiTitle.select("a[href]").attr("href");
                    anakoinoseisLink.add(anakoinosiLink);
                    final Elements anakoinosiLesson = anakoinosi.select("div.text-grey");
                    anakoinoseisMathima.add(anakoinosiLesson.text());
                    final Elements anakoinosiWhen = anakoinosi.select("div:eq(2)");
                    anakoinoseisWhen.add(anakoinosiWhen.text());
                    View card = View.inflate(getActivity(), R.layout.anakoinoseis_card, null);
                    TextView cardLesson = (TextView) card.findViewById(R.id.cardAnakoinosiLesson);
                    TextView cardDate = (TextView) card.findViewById(R.id.cardAnakoinosiDate);
                    ((TextView) card.findViewById(R.id.cardAnakoinosiTitle)).setText(anakoinosiTitle.text());
                    cardLesson.setText(anakoinosiLesson.text());
                    cardDate.setText(anakoinosiWhen.text());
                    card.setClickable(true);
                    card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(),AnakoinosiActivity.class);
                            intent.putExtra("link","http://eclass.uowm.gr"+anakoinosiLink);
                            intent.putExtra("when",anakoinosiWhen.text());
                            intent.putExtra("lesson",anakoinosiLesson.text());
                            intent.putExtra("title",anakoinosiTitle.text());
                            startActivity(intent);
                        }
                    });
                    anakoinoseisContainer.addView(card);
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
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anakoinoseis, container, false);
        this.anakoinoseisContainer = (LinearLayout) view.findViewById(R.id.anakoinoseisContainer);
        this.toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(this.toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Ανακοινώσεις");
            actionBar.setHomeAsUpIndicator(R.drawable.hamburger);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        return view;
    }
}