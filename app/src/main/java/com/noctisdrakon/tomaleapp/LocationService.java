package com.noctisdrakon.tomaleapp;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by NoctisDrakon on 23/11/2015.
 */
public class LocationService extends Service 
{
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;
    public Criteria criteria;
    public String provider;

    //Cosas add imageview
    private WindowManager windowManager;
    private de.hdodenhof.circleimageview.CircleImageView imagencircular;
    private Animation bounce;
    private LinearLayout layout;
    public GoogleApiClient mGoogleApiClient;
    private String TAG = "LocationService";
    private boolean receive = true;
    private SharedPreferences preferences;
    private String currentId="";
    private String currentName="";
    private SQLiteDatabase mydatabase;



    int veces=0;


    Intent intent;
    int counter = 0;

  
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
//don't compare with == as intermediate stages also can be reported, always better to check >= or <=
       System.gc();
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        mydatabase = openOrCreateDatabase("TomaleDB", MODE_PRIVATE, null);

        GoogleApiClient.ConnectionCallbacks gcb = new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                Log.d(TAG, "onConnected: Conectado");
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d(TAG, "onConnectionSuspended: Suspendido "+i);
            }
        };

        GoogleApiClient.OnConnectionFailedListener cfl = new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Log.d(TAG, "onConnectionFailed: Falló: "+connectionResult);
            }
        };

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(gcb)
                .addOnConnectionFailedListener(cfl)
                .build();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
       // Toast.makeText( getApplicationContext(), "Inicia el servicio de sugerencias!", Toast.LENGTH_SHORT ).show();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();

        //Criteria
        criteria=new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        provider=locationManager.getBestProvider(criteria, true);
        Log.d("OnStart","El mejor provider que se encontró fué: "+provider);

        if(ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(provider, 100, 0, listener);
        }

        mGoogleApiClient.connect();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }



    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }



    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        if(ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(listener);
        }
        Log.v("STOP_SERVICE", "DONE");
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }



    public class MyLocationListener implements LocationListener
    {

        public MyLocationListener(){

        }

        public void onLocationChanged(final Location loc)
        {
            Log.i("**********", "Location changed");
            Log.d("LocationChanged", "Cambió el GPS D:");
            //veces++;
            //Toast.makeText(getApplication(), "Actualización número: "+veces+"La Latitud es: "+loc.getLatitude() + " Y la longitud es: " + loc.getLongitude(), Toast.LENGTH_SHORT).show();

            if(isBetterLocation(loc, previousBestLocation)) {
                Log.d("IsBetterLocation", "Si es mejor");
                loc.getLatitude();
                loc.getLongitude();
                intent.putExtra("Latitude", loc.getLatitude());
                intent.putExtra("Longitude", loc.getLongitude());
                intent.putExtra("Provider", loc.getProvider());
                sendBroadcast(intent);
            }

            receive = preferences.getBoolean("updates", false);
            if (receive) {
                Log.d(TAG, "onLocationChanged y bien...");
                try{
                    Log.d(TAG, "onLocationChanged en el try");
                    PlaceFilter pf = new PlaceFilter(true, null);
                    PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, pf);
                    result.setResultCallback(new com.google.android.gms.common.api.ResultCallback<PlaceLikelihoodBuffer>() {
                        @Override
                        public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                            Log.d(TAG, "onResult En el OnResult");
                            if(likelyPlaces.getStatus().isSuccess() && likelyPlaces!=null &&likelyPlaces.getCount()>0){
                                //se encontraron lugares
                                Log.d(TAG, "onResult: Recuperamos Resultados!!");
                                int index=0;
                                int counter=0;

                                if(preferences.getBoolean("food", false)) {
                                    Log.d(TAG, "onResult: El usuario solo quiere lugares de comida!");

                                    List<Integer> lugares = new ArrayList<Integer>();
                                    lugares.add(Place.TYPE_RESTAURANT);
                                    lugares.add(Place.TYPE_BAR);
                                    lugares.add(Place.TYPE_CAFE);
                                    lugares.add(Place.TYPE_FOOD);
                                    lugares.add(Place.TYPE_MEAL_DELIVERY);
                                    lugares.add(Place.TYPE_MEAL_TAKEAWAY);

                                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                        Log.i(TAG, String.format("Lugar: '%s' Id: '%s' Probabilidad: %g",
                                                placeLikelihood.getPlace().getName(),
                                                placeLikelihood.getPlace().getId(),
                                                placeLikelihood.getLikelihood()));

                                        if (!Collections.disjoint(placeLikelihood.getPlace().getPlaceTypes(), lugares)) {
                                            Log.d(TAG, "onResult: Si es un restaurante, o cafe, o tiene que ver con comida");
                                            index = counter;
                                            break;
                                        } else {
                                            Log.d(TAG, "onResult: No tiene nada que ver con comida!");
                                        }
                                        counter++;

                                    }
                                }
                                //Obtenemos el primero de todos los resultados que recuperamos:
                                Place p=likelyPlaces.get(index).getPlace();
                                Log.d(TAG, "onResult: Se recuperó: " + p.getName() + "con el ID: " + p.getId());
                                Cursor c = mydatabase.rawQuery("SELECT COUNT(*) FROM LastBusinessNotified", null);
                                if(c.moveToFirst()){
                                    //si hay cosas en la tabla, buscamos específicamente el que encontró
                                    Log.d(TAG, "onResult Si hay resultados en la base de datos!!");
                                    c = mydatabase.rawQuery("SELECT GoogleID FROM LastBusinessNotified WHERE GoogleID='"+p.getId()+"'", null);
                                    if(c.moveToFirst()){
                                        String cID=c.getString(0);
                                        Log.d(TAG, "onResult el CID es: "+cID);
                                       
                                        if(cID.equals(p.getId())){
                                            //es el mismo negocio, no hagas nada
                                            Log.d(TAG, "onResult Es el mismo negocio, no hagas nada.");
                                            likelyPlaces.release();
                                            return;
                                        }else{
                                            Log.d(TAG, "onResult Es otro negocio diferente, borramos e insertamos en nuevo");
                                            mydatabase.execSQL("DELETE FROM LastBusinessNotified WHERE GoogleID='"+cID+ "'");
                                            mydatabase.execSQL("INSERT INTO LastBusinessNotified VALUES('"+p.getId()+"','"+p.getName()+"');");                                            
                                        }

                                    }else{
                                        Log.d(TAG, "onResult La tabla está vacía.");
                                    }
                                }else{
                                    //la tabla está vacía. Insértalo
                                    Log.d(TAG, "onResult La tabla está vacía. Insértalo");
                                    mydatabase.execSQL("INSERT INTO LastBusinessNotified VALUES('"+p.getId()+"','"+p.getName()+"');");
                                }

                                currentName = p.getName().toString();
                                currentId = p.getId();

                                likelyPlaces.release();
                                //Comenzamos a recuperar alguna foto en caso de que exista con el id del primer negocio
                                new PhotoTask() {
                                    @Override
                                    protected void onPreExecute() {
                                        Log.d(TAG, "onPreExecute ");
                                    }

                                    @Override
                                    protected void onPostExecute(Bitmap imagen) {
                                        Log.d(TAG, "onPostExecute ");
                                    }
                                }.execute(currentId);
                            }//here ends if likelyplaces is ok or is greater than 0
                            likelyPlaces.release();
                        }//here ends likelyplaces's onresult
                    });//here ends result callback

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        public void onProviderDisabled(String provider2)
        {
            //Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
            Log.d("OnProviderDisabled", "Provider deshabilitado D:");

        }


        public void onProviderEnabled(String provider3)
        {
           // Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
            Log.d("OnProviderEnabled", "Provider Habilitado: "+provider);

        }

        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.d("OnStatusChanged", "El provider: " + provider + " Cambió y su status es: " + status);
        }
    }

    //clase de foto
    class PhotoTask extends AsyncTask<String, Void, Bitmap> {


        public PhotoTask() {

        }

        public void sendNotificacionAsync(Bitmap imagennegocio){
            Log.d(TAG, "onResult: Comenzamos la notificacion!");
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder notif = new NotificationCompat.Builder(getApplication());
            notif.setContentTitle("¿Estás en " + currentName+"?");
            notif.setContentText("Si no, entonces estás muy cerca. ¿Por que no lo visitas?");
            notif.setSmallIcon(R.drawable.tomalelogo);
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if(preferences.getBoolean("son", false)) {
                notif.setSound(notification);
            }
            if(preferences.getBoolean("vib", false)) {
                long[] pattern = {0, 500, 500};
                notif.setVibrate(pattern);
            }
            Bitmap imagenfinal;
            if(imagennegocio!=null) {
                imagenfinal=imagennegocio;
            }else{
                imagenfinal=BitmapFactory.decodeResource(getResources(),R.drawable.tomalebg);
            }
            //notif.setStyle(new Notification.InboxStyle().setBigContentTitle("¿Estás en " + currentName + "?").addLine("Si no, entonces estás muy cerca.").addLine("¿Por que no lo visitas?").setSummaryText("Notificado desde PlifMX"));

            notif.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(imagenfinal).setSummaryText("¿No? Date una vuelta entonces!").setBigContentTitle("¿Estás en " + currentName + "?").bigLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.tomalelogo)));
            Notification n = notif.build();
            notificationManager.notify(1,n);
        }

        /**
         * Loads the first photo for a place id from the Geo Data API.
         * The place id must be the first (and only) parameter.
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            if (params.length != 1) {
                Log.d(TAG, "doInBackground Diferente de uno!!!!");
                return null;
            }
            final String placeId = params[0];
            Log.d(TAG, "doInBackground el placeID es: "+placeId);
            Bitmap retorna = null;

            PlacePhotoMetadataResult result = Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, placeId).await();

            if (result.getStatus().isSuccess()) {
                Log.d(TAG, "doInBackground Is Success");
                PlacePhotoMetadataBuffer photoMetadataBuffer = result.getPhotoMetadata();
                if (photoMetadataBuffer.getCount() > 0 && !isCancelled()) {
                    Log.d(TAG, "doInBackground is greater than 1 and isn't cancelled");

                    try {
                        Log.d(TAG, "doInBackground is greater than 1 and isn't cancelled");
                        // Get the first bitmap and its attributions.
                        Log.d(TAG, "doInBackground getting 0 index");
                        PlacePhotoMetadata photo = result.getPhotoMetadata().get(0);
                        // Load a scaled bitmap for this photo.
                        Log.d(TAG, "doInBackground getting bitmap and awaiting");
                        System.gc();
                        retorna = photo.getPhoto(mGoogleApiClient).await().getBitmap();;
                    }catch (Exception ex){
                        Log.d(TAG, "doInBackground Excepcion caught");
                        ex.printStackTrace();
                    }
                }
                // Release the PlacePhotoMetadataBuffer.
                Log.d(TAG, "doInBackground Releasing buffer");
                photoMetadataBuffer.release();
                Log.d(TAG, "doInBackground Running Notificacion");
                sendNotificacionAsync(retorna);

            }
            Log.d(TAG, "doInBackground Justo antes del return retorna");
            return retorna;
        }

    }

}