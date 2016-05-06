package gr.gkortsaridis.uowmeclass;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class MathimaActivity extends AppCompatActivity {
    String kathigitis;
    String lessonCode;
    String link;
    private TabLayout tabLayout;
    String title;
    private Toolbar toolbar;
    private ViewPager viewPager;

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList();
        private final List<String> mFragmentTitleList = new ArrayList();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        public int getCount() {
            return this.mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(title);
        }

        public CharSequence getPageTitle(int position) {
            return (CharSequence) this.mFragmentTitleList.get(position);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mathima);
        this.title = getIntent().getStringExtra("Mathima");
        this.lessonCode = getLessonCode(this.title);
        this.kathigitis = getIntent().getStringExtra("Kathigitis");
        this.link = getIntent().getStringExtra("Link");
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(this.title);
        this.viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(this.viewPager);
        this.tabLayout = (TabLayout) findViewById(R.id.tabs);
        this.tabLayout.setupWithViewPager(this.viewPager);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            finish();
        }
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MathimaDescriptionFragment(this.link, this.title, this.kathigitis), "ΠΕΡΙΓΡΑΦΗ");
        adapter.addFragment(new MathimaAnakoinoseisFragment(this.link, this.title), "Ανακοινώσεις");
        adapter.addFragment(new MathimaEggrafaFragment("https://eclass.uowm.gr/modules/document/?course=" + this.lessonCode), "Έγγραφα");
        viewPager.setAdapter(adapter);
    }

    private String getLessonCode(String title) {
        String code = "";
        String[] words = title.split(" ");
        return words[words.length - 1].substring(1, words[words.length - 1].length() - 1);
    }
}
