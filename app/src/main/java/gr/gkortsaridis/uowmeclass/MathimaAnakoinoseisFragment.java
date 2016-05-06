package gr.gkortsaridis.uowmeclass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

public class MathimaAnakoinoseisFragment extends Fragment {

    ArrayList<String> dates,links,titles;
    String lesson;
    String link;
    ListView listView;

    public MathimaAnakoinoseisFragment(String link, String lesson) {
        this.link = link;
        this.lesson = lesson;
    }

    public MathimaAnakoinoseisFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("link",link);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            link = savedInstanceState.getString("link");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_mathima_anakoinoseis, container, false);
        this.listView = (ListView) view.findViewById(R.id.lessonAnakoinoseisLV);
        this.titles = new ArrayList();
        this.links = new ArrayList();
        this.dates = new ArrayList();

        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(getContext());
        client.setCookieStore(myCookieStore);
        BasicClientCookie newCookie = new BasicClientCookie("cookiesare", "awesome");
        newCookie.setVersion(1);
        newCookie.setDomain(link+"/");
        newCookie.setPath("/");
        myCookieStore.addCookie(newCookie);
        Log.i("Link",link);
        client.post(link+"/", new AsyncHttpResponseHandler() {
            ProgressDialog dialog;

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String str = new String(responseBody, StandardCharsets.UTF_8);

                Document doc = Jsoup.parse(str);
                ArrayList<String> anakoinoseisListData = new ArrayList();
                Iterator i$ = doc.select("li.list-item").iterator();
                while (i$.hasNext()) {
                    Element lessonElement = (Element) i$.next();
                    String title = lessonElement.select("div.text-title").text();
                    //Log.i("Title", title);
                    titles.add(title);
                    Elements lessonLinkEl = lessonElement.select("a[href]");
                    //Log.i("link", lessonLinkEl.attr("href"));
                    links.add(lessonLinkEl.attr("href"));
                    Elements dateElement = lessonElement.select("span");
                    String date = dateElement.text().substring(title.length(), dateElement.text().length());
                    //Log.i("Date", date);
                    dates.add(date);
                    anakoinoseisListData.add(title + "\n" + date);
                }
                listView.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, anakoinoseisListData));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getContext(),AnakoinosiActivity.class);
                        intent.putExtra("link","http://eclass.uowm.gr"+links.get(position));
                        intent.putExtra("when",dates.get(position));
                        intent.putExtra("lesson",lesson);
                        intent.putExtra("title",titles.get(position));
                        startActivity(intent);
                    }
                });
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
                Log.i("error",error.toString());
                Log.i("body",new String(responseBody, StandardCharsets.UTF_8));
                dialog.dismiss();
            }
        });


        return view;
    }
}
