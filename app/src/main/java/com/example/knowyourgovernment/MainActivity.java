package com.example.knowyourgovernment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import android.content.Intent;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private final List<Official> politicianList = new ArrayList<>();
    private GovOfficialAdapter oAdapter;
    private String choice;
    private static int MY_LOCATION_REQUEST_CODE_ID = 111;
    private LocationManager locationManager;
    private Criteria criteria;
    private TextView userEnteredLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_View);
        oAdapter = new GovOfficialAdapter(politicianList, this);
        recyclerView.setAdapter(oAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();

        //Using Gps for location
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, MY_LOCATION_REQUEST_CODE_ID);
        } else {
            setLocation();
        }
    }
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull
            String[] permissions, @NonNull
            int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        if(requestCode == MY_LOCATION_REQUEST_CODE_ID){
            if(permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED){
                setLocation();
                return;
            }
        }
        ((TextView)findViewById(R.id.userEnteredLocation)).setText("No Permission!");
    }

    @SuppressLint("MissingPermission")
    public void setLocation() {
        String bestProvider = locationManager.getBestProvider(criteria, true);
        //((TextView)findViewById(R.id.userEnteredLocation)).setText(bestProvider);

        Location currLocation = null;
        if (bestProvider != null) {
            currLocation = locationManager.getLastKnownLocation(bestProvider);
        }

        if (currLocation != null) {
            double lon = currLocation.getLongitude();
            double lat = currLocation.getLatitude();
            Geocoder geo = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> geoFromLocation = geo.getFromLocation(lat, lon, 1);
                Address address = geoFromLocation.get(0);
                String userZip = address.getPostalCode();
                PoliticianDownloader poliDownloader = new PoliticianDownloader(this, userZip);
                new Thread(poliDownloader).start();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void applyLocation(String s){
        userEnteredLocation = findViewById(R.id.userEnteredLocation);
        userEnteredLocation.setText(s);
    }


    public void addNearByPoliticians(ArrayList<Official> politicianL){
        for(Official politician : politicianL){
            politicianList.add(politician);
            oAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_opt_menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.aboutInfo:
                Toast.makeText(this,"Publication info...",Toast.LENGTH_SHORT).show();
                Intent goToAboutActPage = new Intent(this,AboutActivity.class);
                startActivity(goToAboutActPage);
                return true;
            case R.id.addLocation:
                Toast.makeText(this,"Updating current Location...",Toast.LENGTH_SHORT).show();
                //politicianList.clear();
                createLocationDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createLocationDialog() {
        if(!checkNetworkConnection()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Network Connection!");
            builder.setMessage("Location Cannot Be Found Without a Network Connection!");
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setGravity(Gravity.CENTER_HORIZONTAL);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setView(editText);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                politicianList.clear();
                choice = editText.getText().toString().trim();
                PoliticianDownloader politicalD1 = new PoliticianDownloader(MainActivity.this,choice);
                new Thread(politicalD1).start();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //user hit cancel
            }
        });
        builder.setMessage("Please Enter a City, State, or Zip code: ");
        builder.setTitle("Location Selection");
        AlertDialog dialog = builder.create();
        dialog.show();

    }
    @Override
    public void onPause() {
        super.onPause();
    }

    public boolean checkNetworkConnection(){
        ConnectivityManager cm =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Official official = politicianList.get(pos);
        Intent goToOfficialPage = new Intent(this,OfficialActivity.class);
        goToOfficialPage.putExtra("official",official);
        goToOfficialPage.putExtra("location",userEnteredLocation.getText().toString());
        startActivity(goToOfficialPage);
    }

}