package gr.gkortsaridis.uowmeclass;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

public class ProfileFragment extends Fragment {
    private String aem;
    private TextView aemTV;
    private String bio;
    private TextView bioTV;
    private String fullName;
    private TextView fullNameTV;
    AsyncTask<String, Void, String> httptask;
    private String idiotita;
    private TextView idiotitaTV;
    private String statistics;
    private TextView statisticsTV;
    private String telephone;
    private TextView telephoneTV;
    Toolbar toolbar;
    private String username;
    private TextView usernameTV;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void onResume() {
        super.onResume();
        getProfile();
    }

    public void getProfile(){
        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(getContext());
        client.setCookieStore(myCookieStore);
        BasicClientCookie newCookie = new BasicClientCookie("cookiesare", "awesome");
        newCookie.setVersion(1);
        newCookie.setDomain("http://eclass.uowm.gr/main/profile/display_profile.php");
        newCookie.setPath("/");
        myCookieStore.addCookie(newCookie);
        client.post("http://eclass.uowm.gr/main/profile/display_profile.php", new AsyncHttpResponseHandler() {
            ProgressDialog dialog;

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String str = new String(responseBody, StandardCharsets.UTF_8);
                //dialog.dismiss();
                int i;
                Document doc = Jsoup.parse(str);
                String[] parts = doc.select("div#pers_info").text().split(" ");
                fullName = parts[0] + " " + parts[1];
                username = parts[2];
                telephone = parts[12];
                idiotita = parts[15];
                aem = parts[19];
                fullNameTV.setText(fullName);
                usernameTV.setText(username);
                telephoneTV.setText(telephone);
                idiotitaTV.setText(idiotita);
                aemTV.setText(aem);
                parts = doc.select("div#profile-about-me").text().split(" ");
                bio = "";
                for (i = 3; i < parts.length; i++) {
                    bio += parts[i] + " ";
                }
                bioTV.setText(bio);
                parts = doc.select("div#profile-departments").text().split(" ");
                statistics = "";
                for (i = 1; i < parts.length; i++) {
                    statistics += parts[i] + " ";
                }
                statisticsTV.setText(statistics);
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        this.toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(this.toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Πριφίλ");
            actionBar.setHomeAsUpIndicator(R.drawable.hamburger);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        this.fullNameTV = (TextView) view.findViewById(R.id.myfullname_prof);
        this.usernameTV = (TextView) view.findViewById(R.id.myusername_prof);
        this.telephoneTV = (TextView) view.findViewById(R.id.mytel_prof);
        this.idiotitaTV = (TextView) view.findViewById(R.id.myidiotita_prof);
        this.aemTV = (TextView) view.findViewById(R.id.myaem_prof);
        this.bioTV = (TextView) view.findViewById(R.id.myabout_prof);
        this.statisticsTV = (TextView) view.findViewById(R.id.mystatistika_prof);




        return view;
    }

}
