package gr.gkortsaridis.uowmeclass;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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

import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

public class MathimaEggrafaFragment extends Fragment {
    ArrayList<String> eggrafa;
    int eggrafaDepth = 0;
    String link;
    ArrayList<String> links;
    ListView listView;
    ArrayList<String> types;

    public MathimaEggrafaFragment(String link) {
        this.link = link;
    }

    public MathimaEggrafaFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mathima_eggrafa, container, false);
        this.listView = (ListView) view.findViewById(R.id.mathimaEggrafaLV);
        view.findViewById(R.id.openegrafa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(intent);
            }
        });
        populateListView(this.link, this.eggrafaDepth);
        return view;
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

    private void populateListView(String theLink, final int depth) {
        this.eggrafa = new ArrayList();
        this.links = new ArrayList();
        this.types = new ArrayList();
        if (!theLink.equals(this.link)) {
            this.eggrafa.add("Πίσω");
            this.links.add(this.link);
            this.types.add("back");
        }

        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(getContext());
        client.setCookieStore(myCookieStore);
        BasicClientCookie newCookie = new BasicClientCookie("cookiesare", "awesome");
        newCookie.setVersion(1);
        newCookie.setDomain(theLink);
        newCookie.setPath("/");
        myCookieStore.addCookie(newCookie);
        Log.i("link",theLink);
        client.post(theLink, new AsyncHttpResponseHandler() {

            ProgressDialog dialog;

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

                String str = new String(responseBody, StandardCharsets.UTF_8);
                Iterator i$ = Jsoup.parse(str).select("tr.visible").iterator();
                while (i$.hasNext()) {
                    Element eggrafoRow = (Element) i$.next();
                    //Log.i("Eggrafo", eggrafoRow.text());
                    Element titleEggrafo = (Element) eggrafoRow.select("td").get(1);
                    String title = titleEggrafo.text();
                    //Log.i("Title", title);
                    Elements linkEggrafo = titleEggrafo.select("a");
                    String linkEgr = linkEggrafo.attr("href");
                    types.add(linkEggrafo.attr("class"));
                    eggrafa.add(title);
                    links.add("https://eclass.uowm.gr/" + linkEgr);
                }
                listView.setAdapter(new EggrafaListAdapter(getContext(), eggrafa, types));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //if(types.get(position).contains("fileURL")){
                        //    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(links.get(position)));
                        //    startActivity(intent);
                        //}else {
                            populateListView(links.get(position), depth);
                        //}
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
                dialog.dismiss();
            }
        });


    }
}

