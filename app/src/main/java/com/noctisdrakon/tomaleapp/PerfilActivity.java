package com.noctisdrakon.tomaleapp;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class PerfilActivity extends AppCompatActivity  implements mainscreen_perfil.OnFragmentInteractionListener {

    SliderLayout sliderShow;

    //Pinche metodo pendejo
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        Intent intent = getIntent();
        final String NombreNegocio = intent.getStringExtra("nombre");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Restaurant Bar La Ensalada");

        sliderShow = (SliderLayout) findViewById(R.id.slidernegocio);

        if(sliderShow==null){
            Log.d("SliderShow","Pinche Slider es nulo!");
        }

        TextSliderView textSliderView = new TextSliderView(this);
        textSliderView
                .description("")
                .image("http://restaurantnews.com/wp-content/uploads/2012/09/The-Big-Salad-Has-Big-Franchise-Plans.jpg");
        textSliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);
        sliderShow.addSlider(textSliderView);


        TextSliderView textSliderView2 = new TextSliderView(this);
        textSliderView2
                .description("")
                .image("http://www.nandos.com/sites/all/themes/nandos/images/restaurants/restaurant-carousel-1.jpg");
        textSliderView2.setScaleType(BaseSliderView.ScaleType.CenterCrop);
        sliderShow.addSlider(textSliderView2);

        TextSliderView textSliderView3 = new TextSliderView(this);
        textSliderView3
                .description("")
                .image("http://basilgreenpencil.com/wp-content/uploads/2014/06/4.jpg");
        textSliderView3.setScaleType(BaseSliderView.ScaleType.CenterCrop);
        sliderShow.addSlider(textSliderView3);

        textSliderView3
                .description("")
                .image("http://basilgreenpencil.com/wp-content/uploads/2014/06/4.jpg");
        textSliderView3.setScaleType(BaseSliderView.ScaleType.CenterCrop);
        sliderShow.addSlider(textSliderView3);



      // loadBackdrop();

      // loadSlider();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
           // viewPager.setPageTransformer(true, new MainActivity.ZoomOutPageTransformer());
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

       // TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
       // tabLayout.setupWithViewPager(viewPager);

    }



    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());

        adapter.addFragment(new mainscreen_perfil(), "Información");
        adapter.addFragment(new mainscreen_perfil(), "Reseñas");
        adapter.addFragment(new mainscreen_perfil(), "Promociones");
        //adapter.addFragment(new slpashfragment(), "Fox");
/*      adapter.addFragment(new AppFragment(), "Category 1");
        adapter.addFragment(new AppFragment(), "Category 2");
        adapter.addFragment(new AppFragment(), "Category 3");*/
        viewPager.setAdapter(adapter);

    }

    private void loadSlider(){




    }

    private void loadBackdrop() {
     /*   final ImageView backdrop = (ImageView) findViewById(R.id.backdrop);
        //Glide.with(this).load(Cheeses.getRandomCheeseDrawable()).centerCrop().into(imageView);
        Ion.with(backdrop)
                .placeholder(R.drawable.blue_mt)
                .error(R.drawable.ic_forum)

                        //.animateLoad(spinAnimation)
                        //.animateIn(fadeInAnimation)
                .load("http://www.comohotels.com/metropolitanbangkok/sites/default/files/styles/background_image/public/images/background/metbkk_bkg_nahm_restaurant.jpg");
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    //Empieza la clase Adapter
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {

            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }

    }
}
