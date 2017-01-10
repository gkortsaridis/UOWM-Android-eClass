package gr.gkortsaridis.uowmeclass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

public class MainActivity extends AppCompatActivity {
    public static SharedPreferences sharedpreferences;
    AsyncTask<String, Void, String> httptask;
    Button loginBtn;
    EditText password;
    CheckBox remember;
    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences("credentials", 0);
        this.username = (EditText) findViewById(R.id.usernameEdit);
        this.password = (EditText) findViewById(R.id.passwordEdit);
        this.remember = (CheckBox) findViewById(R.id.rememberMeCheck);
        this.loginBtn = (Button) findViewById(R.id.loginBtn);
        this.username.setText(sharedpreferences.getString("username", ""));
        this.password.setText(sharedpreferences.getString("password", ""));
        this.remember.setChecked(sharedpreferences.getBoolean("remember", false));

    }

    public void onResume() {
        super.onResume();
        this.loginBtn.setEnabled(true);
    }


    public void login(View v){

        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (this.remember.isChecked()) {
            editor.putString("username", this.username.getText().toString());
            editor.putString("password", this.password.getText().toString());
            editor.putBoolean("remember", true);
        } else {
            editor.putString("username", "");
            editor.putString("password", "");
            editor.putBoolean("remember", false);
        }
        editor.commit();

        AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        params.put("uname", username.getText().toString());
        params.put("pass", password.getText().toString());
        params.put("submit", "true");
        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        client.setCookieStore(myCookieStore);
        BasicClientCookie newCookie = new BasicClientCookie("cookiesare", "awesome");
        newCookie.setVersion(1);
        newCookie.setDomain("http://eclass.uowm.gr/");
        newCookie.setPath("/");
        myCookieStore.addCookie(newCookie);
        client.post("http://eclass.uowm.gr/", params, new AsyncHttpResponseHandler() {
            ProgressDialog dialog;

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String str = new String(responseBody, StandardCharsets.UTF_8);
                dialog.dismiss();
                if (str.contains("alert alert-warning")) {
                    Toast.makeText(getApplicationContext(), "Λάθος Στοιχεία", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getBaseContext(), LoggedActivity.class));
                }

            }

            @Override
            public void onStart() {
                // called before request is started
                dialog = new ProgressDialog(MainActivity.this); // this = YourActivity
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

