package internshipproject.com.dronaid_demo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class drone_tab extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    GoogleApiClient mLocationClient;
    Location mLastLocation;
    double latitude, longitude;
    TextView tvAddress;
    Button btnRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drone_tab);

        tvAddress  = (TextView) findViewById(R.id.tvAddress);
        btnRequest = (Button) findViewById(R.id.btnRequest);

        mLocationClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mLastLocation != null)
                {
                    Toast.makeText(drone_tab.this, "Request Sent Successfully", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    Toast.makeText(drone_tab.this, "Connection Problem", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mLocationClient!=null)
        {
            mLocationClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
        if(mLastLocation!=null)
        {
            Log.i("tag", "not null");
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            Toast.makeText(this, "Latitude: " + latitude + "Longitude: "+ longitude, Toast.LENGTH_SHORT).show();
            getMyLocationAddress(mLastLocation.getLatitude() , mLastLocation.getLongitude());
        }

        else
        {
            Toast.makeText(this, "Cannot find location", Toast.LENGTH_SHORT).show();
        }

    }

    public void getMyLocationAddress(double latitude , double longitude)
    {
        Geocoder geocoder = new Geocoder(this , Locale.ENGLISH);
        try
        {
            List<Address> addresses = geocoder.getFromLocation(latitude , longitude , 1);

            if(addresses!=null)
            {
                android.location.Address fetchedAddress = addresses.get(0);
                tvAddress.setText("This requested loaction is: \n" +
                        fetchedAddress.getFeatureName() + ", " +
                        fetchedAddress.getSubLocality() + ", " +
                        fetchedAddress.getLocality() + ", " +
                        fetchedAddress.getPostalCode() + ", " +
                        fetchedAddress.getAdminArea() + ", " +
                        fetchedAddress.getCountryName());
            }
            else
            {

            }
        }
        catch (IOException e)
        {
            Toast.makeText(this, "Could not get address!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

        Toast.makeText(this, "Connection Suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }


}
