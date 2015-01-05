package com.example.alexandre.alex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;
import java.util.Vector;


public class SliderActivity extends FragmentActivity {
    /*Initialisation des variables*/
    private static final int NUM_PAGES = 2;

    List slides = new Vector();
    private ViewPager mPager;

    private PagerAdapter pageA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);

        slides.add(Fragment.instantiate(this, Slides1.class.getName()));
        slides.add(Fragment.instantiate(this, Slides2.class.getName()));
        /*Création de l'adaptateur dans le onCreate et le donner au viewPager*/
        pageA = new PagerHandler(getSupportFragmentManager(), slides);
        ViewPager pager = (ViewPager)findViewById(R.id.viewpager);
        pager.setAdapter(pageA);
    }

    /*Menu avec bouton Home*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent( this,MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*Création de l'adaptateur*/
    private class PagerHandler extends FragmentStatePagerAdapter {
        private final List slides;

        public PagerHandler(FragmentManager fm, List slides) {
            super(fm);
            this.slides= slides;
        }

        @Override
        public Fragment getItem(int position) {
            return (Fragment) this.slides.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}