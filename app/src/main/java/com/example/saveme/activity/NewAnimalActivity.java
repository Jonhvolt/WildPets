package com.example.saveme.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saveme.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.Date;

public class NewAnimalActivity
extends AppCompatActivity
implements OnMapReadyCallback
{
    private static final int CAMERA_REQUEST = 0;

    ImageView imageView;
    TextView locationNow;
    SupportMapFragment mapFragment;
    GoogleMap googleMap;

    private LocationManager locationManager;

    private static final String LOCAL_PHOTO_PATH = Environment.getExternalStorageDirectory() + File.separator + "newRandomAnimal.jpg";

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_new_animal );

        locationNow = findViewById( R.id.location_on_photo );
        locationNow.setVisibility( View.INVISIBLE );

        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById( R.id.photo_map_location );
        mapFragment.getView().setVisibility( View.INVISIBLE );
        mapFragment.getMapAsync( this );

        locationManager = (LocationManager)getSystemService( LOCATION_SERVICE );

        imageView = findViewById( R.id.view_first_animal_photo );
        imageView.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                File file = new File( LOCAL_PHOTO_PATH );
                Uri uri = Uri.fromFile( file );

                Intent cameraIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE, uri );
                startActivityForResult( cameraIntent, CAMERA_REQUEST );
            }
        } );


    }

    @Override
    public void onMapReady( GoogleMap googleMap1 )
    {
        this.googleMap = googleMap1;
    }

    @Override
    protected void onActivityResult( int requestCode,
                                     int resultCode,
                                     @Nullable Intent intent )
    {
        if( requestCode == CAMERA_REQUEST && resultCode == RESULT_OK )
        {
            // Фотка сделана, извлекаем картинку

            Bitmap thumbnailBitmap = BitmapFactory.decodeFile( LOCAL_PHOTO_PATH );
            imageView.setImageBitmap( thumbnailBitmap );
        }
        if(
        ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION )
        != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION )
        != PackageManager.PERMISSION_GRANTED )
        {
            Toast toast = Toast.makeText( this, "Вами запрещена геолокация", Toast.LENGTH_LONG );
            toast.show();
            return;
        }
        Location location = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
        if( location == null )
        {
            return;
        }

        mapFragment.getView().setVisibility( View.VISIBLE );
        locationNow.setVisibility( View.VISIBLE );

        double latitude = location.getLatitude(); //широта
        double longitude = location.getLongitude(); //долгота
        LatLng latLng = new LatLng( latitude, longitude );
        MarkerOptions markerOptions = new MarkerOptions().position( latLng );

        googleMap.addMarker( markerOptions );

        CameraPosition cameraPosition = new CameraPosition.Builder().target( latLng ).zoom(28).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if(
        ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION )
        != PackageManager.PERMISSION_GRANTED
        &&
        ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION )
        != PackageManager.PERMISSION_GRANTED )
        {
            Toast toast = Toast.makeText( this, "Вами запрещена геолокация", Toast.LENGTH_LONG );
            toast.show();
            return;
        }

        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0 , 10, locationListener );
        locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 3000, 10, locationListener );
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        locationManager.removeUpdates( locationListener );
    }

    private LocationListener locationListener = new LocationListener()
    {

        @Override
        public void onLocationChanged( Location location )
        {
            //изменение геолокации
        }

        @Override
        public void onProviderDisabled( String provider ) { }

        @Override
        public void onProviderEnabled( String provider ) { }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }
    };

    //на вход берет Location, читает из него данные и форматирует из них строку. Какие данные он берет: getLatitude – широта, getLongitude – долгота, getTime – время определения.
    private String formatLocation(Location location) {

        return String.format( "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT", location.getLatitude(), location.getLongitude(), new Date( location.getTime()));
    }

    //отправить пользователя в настройки геолокации, если она выключена
    public void onClickLocationSettings(View view) {
        startActivity(new Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    };
}
