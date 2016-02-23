package com.noctisdrakon.tomaleapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Configuracion extends AppCompatActivity {

    private SharedPreferences preferences;
    private SwitchCompat notif;
    private SwitchCompat food;
    private SwitchCompat son;
    private SwitchCompat vib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Configuración");

        notif = (SwitchCompat) findViewById(R.id.notif);
        food = (SwitchCompat) findViewById(R.id.food);
        son = (SwitchCompat) findViewById(R.id.son);
        vib = (SwitchCompat) findViewById(R.id.vib);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final Intent serviceIntent = new Intent(this, LocationService.class);

        if(preferences.getBoolean("updates", false)) {
            notif.setChecked(true);
        }else{
            notif.setChecked(false);
        }

        if(preferences.getBoolean("food", false)) {
            food.setChecked(true);
        }else{
            food.setChecked(false);
        }

        if(preferences.getBoolean("son", false)) {
            son.setChecked(true);
        }else{
            son.setChecked(false);
        }

        if(preferences.getBoolean("vib", false)) {
            vib.setChecked(true);
        }else{
            vib.setChecked(false);
        }

        if(!notif.isChecked()){
            food.setEnabled(false);
            son.setEnabled(false);
            vib.setEnabled(false);
        }

        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notif.isChecked()) {
                    preferences.edit().putBoolean("updates",true).commit();
                    startService(serviceIntent);
                    food.setEnabled(true);
                    son.setEnabled(true);
                    vib.setEnabled(true);
                } else {
                    preferences.edit().putBoolean("updates",false).commit();
                    stopService(serviceIntent);
                    food.setEnabled(false);
                    son.setEnabled(false);
                    vib.setEnabled(false);
                }

            }
        });


        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (food.isChecked()) {
                    preferences.edit().putBoolean("food", true).commit();
                } else {
                    preferences.edit().putBoolean("food", false).commit();
                }

            }
        });

        son.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (son.isChecked()) {
                    preferences.edit().putBoolean("son", true).commit();
                } else {
                    preferences.edit().putBoolean("son", false).commit();
                }

            }
        });

        vib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vib.isChecked()) {
                    preferences.edit().putBoolean("vib", true).commit();
                } else {
                    preferences.edit().putBoolean("vib", false).commit();
                }

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configuracion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                finish();
                return true;


        }

        return super.onOptionsItemSelected(item);
    }
}
