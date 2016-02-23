package com.noctisdrakon.tomaleapp;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.koushikdutta.ion.Ion;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link mainscreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mainscreen extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SliderLayout sliderShow;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mainscreen.
     */
    // TODO: Rename and change types and number of parameters
    public static mainscreen newInstance(String param1, String param2) {
        mainscreen fragment = new mainscreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public mainscreen() {
        // Required empty public constructor
    }

    @Override
   public void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.mainscreen, container, false);
        final ImageView recomendadoimg = (ImageView)view.findViewById (R.id.recomendadoimg);

        Ion.with(recomendadoimg)
                .placeholder(R.drawable.blue_mt)
                .error(R.drawable.ic_forum)

                //.animateLoad(spinAnimation)
                //.animateIn(fadeInAnimation)
                .load("http://www.comohotels.com/metropolitanbangkok/sites/default/files/styles/background_image/public/images/background/metbkk_bkg_nahm_restaurant.jpg");

        recomendadoimg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i(SystemSettings.APP_TAG + " : " + HomeActivity.class.getName(), "Entered onClick method");
                // Toast.makeText(v.getContext(),  "Aquí se abrirá la actividad del perfil",  Toast.LENGTH_LONG).show();
                Context context = v.getContext();
                Intent intent = new Intent(context, PerfilActivity.class);
                intent.putExtra("nombre", "Restaurant Bar La Ensalada");

                context.startActivity(intent);
            }
        });

/*
        recomendadoimg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                switch (arg1.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        recomendadoimg.setAlpha(0.5f);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        recomendadoimg.setAlpha(1.0f);
                        break;
                    }

                    case MotionEvent.ACTION_OUTSIDE: {
                        recomendadoimg.setAlpha(1.0f);
                        break;
                    }

                    case MotionEvent.ACTION_BUTTON_RELEASE: {
                        recomendadoimg.setAlpha(1.0f);
                        break;
                    }/*
                }
                return true;
            }
        });
        */

        //Termina Recomendado

        //Inicia slider
        sliderShow = (SliderLayout) view.findViewById(R.id.slider);

        TextSliderView textSliderView = new TextSliderView(getActivity());
        textSliderView
                .description("Vegan Bistró Nature")
                .image("http://restaurantnews.com/wp-content/uploads/2012/09/The-Big-Salad-Has-Big-Franchise-Plans.jpg");
        textSliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);
        sliderShow.addSlider(textSliderView);


        TextSliderView textSliderView2 = new TextSliderView(getActivity());
        textSliderView2
                .description("Steaks and Wine restaurant")
                .image("http://www.nandos.com/sites/all/themes/nandos/images/restaurants/restaurant-carousel-1.jpg");
        textSliderView2.setScaleType(BaseSliderView.ScaleType.CenterCrop);
        sliderShow.addSlider(textSliderView2);

        TextSliderView textSliderView3 = new TextSliderView(getActivity());
        textSliderView3
                .description("Club Nocturno")
                .image("https://s-media-cache-ak0.pinimg.com/originals/cd/be/9a/cdbe9a9e72ee1f82c9d217c69ae943ee.jpg");
        textSliderView3.setScaleType(BaseSliderView.ScaleType.CenterCrop);
        sliderShow.addSlider(textSliderView3);

        //Termina slider

        LinearLayout popularescont = (LinearLayout)view.findViewById(R.id.popularescont);

        LayoutInflater popcont = LayoutInflater.from(getActivity());

        View v=null;
          for (int i=0; i<=2; i++){
            v=popcont.inflate(R.layout.negocio_row, popularescont, false);
            ImageView bg = (ImageView)v.findViewById(R.id.backgroundneg);

            Ion.with(bg)
                    .placeholder(R.drawable.blue_mt)
                    .error(R.drawable.ic_forum)
                            //.animateLoad(spinAnimation)
                            //.animateIn(fadeInAnimation)
                    .load("http://media-cdn.tripadvisor.com/media/photo-s/02/e7/90/91/oro-nightclub.jpg");

            popularescont.addView(v);
            v=null;
        }



        return view;
    }


}
