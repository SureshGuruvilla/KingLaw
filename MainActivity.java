package com.example.admin.kinglaw;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements
        FeedFragment.OnFragmentInteractionListener,
        DummyFragment.OnFragmentInteractionListener{
    private TabLayout tabLayout;
    private ViewPager viewpager;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        viewpager = (ViewPager) findViewById(R.id.main_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ClassFragmentHelper adapter = new ClassFragmentHelper(getSupportFragmentManager());
        adapter.addFragment(new FeedFragment(),"hai");
        adapter.addFragment(new DummyFragment(),"hello");
        viewpager.setAdapter(adapter);


        tabLayout.addTab(tabLayout.newTab().setText("hai"));
        tabLayout.addTab(tabLayout.newTab().setText("hello"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewpager);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {


    }
}