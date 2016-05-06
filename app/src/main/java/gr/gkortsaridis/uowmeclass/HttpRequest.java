package gr.gkortsaridis.uowmeclass;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

/**
 * Created by yoko on 01/05/16.
 */
public class HttpRequest {

    HttpRequest(String link,Context context, AsyncHttpResponseHandler asyncHttpResponseHandler){
        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
        client.setCookieStore(myCookieStore);
        BasicClientCookie newCookie = new BasicClientCookie("cookiesare", "awesome");
        newCookie.setVersion(1);
        newCookie.setDomain(link);
        newCookie.setPath("/");
        myCookieStore.addCookie(newCookie);
        client.post(link, asyncHttpResponseHandler);
    }

}
