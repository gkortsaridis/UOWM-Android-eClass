package gr.gkortsaridis.uowmeclass;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

public class MathimaDescriptionFragment extends Fragment {
    TextView descr;
    String lesson;
    String link;
    String teacher;
    TextView teachertv;

    public MathimaDescriptionFragment(String link, String lesson, String teacher) {
        this.link = link;
        this.lesson = lesson;
        this.teacher = teacher;
    }

    public MathimaDescriptionFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("link",link);
        outState.putString("teacher",teacher);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            link = savedInstanceState.getString("link");
            teacher = savedInstanceState.getString("teacher");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mathima_description, container, false);
        descr = (TextView) v.findViewById(R.id.mathimaDescriptionTV);
        teachertv = (TextView) v.findViewById(R.id.kathigitisDescrTV);
        teachertv.setText("Καθηγητής : " + teacher);

        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(getContext());
        client.setCookieStore(myCookieStore);
        BasicClientCookie newCookie = new BasicClientCookie("cookiesare", "awesome");
        newCookie.setVersion(1);
        newCookie.setDomain(link+"/");
        newCookie.setPath("/");
        myCookieStore.addCookie(newCookie);
        client.post(link+"/", new AsyncHttpResponseHandler() {

            ProgressDialog dialog;
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String str = new String(responseBody, StandardCharsets.UTF_8);
                descr.setText(Jsoup.parse(str).select("div.course_info").text());
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

        return v;
    }
}
