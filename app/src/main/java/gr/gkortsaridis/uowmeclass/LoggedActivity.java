package gr.gkortsaridis.uowmeclass;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import java.util.concurrent.ExecutionException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class LoggedActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    String what;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);

        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);

        if(savedInstanceState != null){
            view.getMenu().getItem(0).setChecked(true);
            setFragment(savedInstanceState.getString("what"));

        }else {
            view.getMenu().getItem(0).setChecked(true);
            setFragment("Τα Μαθήματα μου");
        }
        view.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Snackbar.make(LoggedActivity.this.findViewById(R.id.your_placeholder), menuItem.getTitle(), Snackbar.LENGTH_SHORT).show();
                LoggedActivity.this.setFragment(menuItem.getTitle().toString());
                LoggedActivity.this.what = menuItem.getTitle().toString();
                menuItem.setChecked(true);
                LoggedActivity.this.drawerLayout.closeDrawers();
                return true;
            }
        });

    }

    public void setFragment(String what) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (what.equals("Τα Μαθήματα μου")) {
            ft.replace(R.id.your_placeholder, new MathimataFragment());
        } else if (what.equals("Οι Ανακοινώσεις μου")) {
            ft.replace(R.id.your_placeholder, new AnakoinoseisFragment());
        } else if (what.equals("Το Προφίλ μου")) {
            ft.replace(R.id.your_placeholder, new ProfileFragment());
        }
        ft.commit();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("what", this.what);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("onRestore","InstanceState");
        setFragment(savedInstanceState.getString("what", "Τα Μαθηματά μου"));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        if (this.drawerLayout == null || !this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            new Builder(this).setIcon(R.drawable.uowm_logo_second).setTitle("Έξοδος").setMessage("Θέλετε να κλείσετε την εφαρμογή?").setPositiveButton("Ναι", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    LoggedActivity.this.startActivity(intent);
                    LoggedActivity.this.finish();
                }
            }).setNegativeButton("Όχι", null).show();
        } else if (this.drawerLayout != null) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}