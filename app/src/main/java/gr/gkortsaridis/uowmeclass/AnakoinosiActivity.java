package gr.gkortsaridis.uowmeclass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

public class AnakoinosiActivity extends AppCompatActivity {
    AsyncTask<String, Void, String> httptask;
    String lesson;
    TextView lessontv;
    String link;
    TextView texttv;
    String title;
    TextView titletv;
    String when;
    TextView whentv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anakoinosi);
        Bundle data = getIntent().getExtras();
        this.title = data.getString("title");
        this.lesson = data.getString("lesson");
        this.when = data.getString("when");
        this.link = data.getString("link");
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(this.title);
        this.lessontv = (TextView) findViewById(R.id.anakoinosiLesson);
        this.whentv = (TextView) findViewById(R.id.anakoinosiWhen);
        this.texttv = (TextView) findViewById(R.id.anakoinosiText);
        this.lessontv.setText(this.lesson);
        this.whentv.setText(this.when);

        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(getBaseContext());
        client.setCookieStore(myCookieStore);
        BasicClientCookie newCookie = new BasicClientCookie("cookiesare", "awesome");
        newCookie.setVersion(1);
        newCookie.setDomain(link+"/");
        newCookie.setPath("/");
        myCookieStore.addCookie(newCookie);
        Log.i("link",link);
        client.post(link+"/", new AsyncHttpResponseHandler() {
            ProgressDialog dialog;

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String str = new String(responseBody, StandardCharsets.UTF_8);
                texttv.setText(Jsoup.parse(str).select("p").text());
                dialog.dismiss();
            }

            @Override
            public void onStart() {
                dialog = new ProgressDialog(AnakoinosiActivity.this); // this = YourActivity
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage("Loading. Please wait...");
                dialog.setIndeterminate(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }


            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("anakoinosi",error.toString());
                dialog.dismiss();
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            finish();
        }
        return true;
    }
}
