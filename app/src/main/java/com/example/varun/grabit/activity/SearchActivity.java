package com.example.varun.grabit.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.varun.grabit.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import es.dmoral.toasty.Toasty;


//import com.dd.processbutton.iml.ActionProcessButton;

/**
 * Created by kvaru on 5/8/2018.
 */

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private ImageView coffeeImgView;
    private ImageView shoppingImgView;
    private ImageView iceCreamImgView;
    private ImageView pizzaImgView;
    private ImageView burgerImgView;
    private ImageView restaurantsImgView;

    private TextView coffeeTxtView;
    private TextView shoppingTxtView;
    private TextView iceCreamTxtView;
    private TextView pizzaTxtView;
    private TextView burgerTxtView;
    private TextView restaurantsTxtView;

    private TextView textViewAddress;
    private String googleAddress;
    private String placeName;
    private Button buttonSearch;
    private String lat;
    private String lng;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("US")
                .build();
        autocompleteFragment.setFilter(typeFilter);
        coffeeImgView = (ImageView) findViewById(R.id.coffeeIV);
        burgerImgView = (ImageView) findViewById(R.id.burgerIV);
        pizzaImgView = (ImageView) findViewById(R.id.pizzaIV);
        shoppingImgView = (ImageView) findViewById(R.id.shoppingIV);
        iceCreamImgView = (ImageView) findViewById(R.id.icecreamIV);
        restaurantsImgView = (ImageView) findViewById(R.id.restaurantIV);

        coffeeTxtView = (TextView) findViewById(R.id.coffeeTV);
        burgerTxtView = (TextView) findViewById(R.id.burgerTV);
        pizzaTxtView = (TextView) findViewById(R.id.pizzaTV);
        shoppingTxtView = (TextView) findViewById(R.id.shoppingTV);
        iceCreamTxtView = (TextView) findViewById(R.id.icecreamTV);
        restaurantsTxtView = (TextView) findViewById(R.id.restaurantTV);

        textViewAddress = (TextView) findViewById(R.id.addressTextView);
        buttonSearch = (Button) findViewById(R.id.btnSearch);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i("PlaceName", "Place: " + place.getName());
                placeName = String.valueOf(place.getName());
                googleAddress = String.valueOf(place.getAddress());
                textViewAddress.setMovementMethod(new ScrollingMovementMethod());
                textViewAddress.setText(googleAddress);
               // lat = String.valueOf(place.getLatLng().latitude);
               // lng = String.valueOf(place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                Log.i("Error", "An error occurred: " + status);
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (placeName == null){
                    Log.d("Auto","Null");
                    Toasty.error(getApplication(),"Select a place",Toast.LENGTH_SHORT).show();
                }else {
                    getCurrentLocationPermission();
                    sendDataToYelp(lat,lng);
                    autocompleteFragment.setText("");
                    textViewAddress.setText("");
                }
            }
        });

        burgerImgView.setOnClickListener(this);
        coffeeImgView.setOnClickListener(this);
        pizzaImgView.setOnClickListener(this);
        shoppingImgView.setOnClickListener(this);
        iceCreamImgView.setOnClickListener(this);
        restaurantsImgView.setOnClickListener(this);

    getCurrentLocationPermission();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.burgerIV:
                String burgerStr = burgerTxtView.getText().toString();
                placeName = burgerStr;
                getCurrentLocationPermission();
                sendDataToYelp(lat,lng);
                break;

            case R.id.pizzaIV:
                String pizzaStr = pizzaTxtView.getText().toString();
                placeName = pizzaStr;
                getCurrentLocationPermission();
                sendDataToYelp(lat,lng);
                break;

            case R.id.coffeeIV:
                String coffeeStr = coffeeTxtView.getText().toString();
                placeName = coffeeStr;
                getCurrentLocationPermission();
                sendDataToYelp(lat,lng);
                break;

            case R.id.shoppingIV:
                String shoppingStr = shoppingTxtView.getText().toString();
                placeName = shoppingStr;
                getCurrentLocationPermission();
                sendDataToYelp(lat,lng);
                break;

            case R.id.icecreamIV:
                String icecreamStr = iceCreamTxtView.getText().toString();
                placeName = icecreamStr;
                getCurrentLocationPermission();
                sendDataToYelp(lat,lng);
                break;

            case R.id.restaurantIV:
                String restaurantStr = restaurantsTxtView.getText().toString();
                placeName = restaurantStr;
                getCurrentLocationPermission();
                sendDataToYelp(lat,lng);
                break;
        }
    }

    public void sendDataToYelp(String lat, String lng) {
        Intent searchIntent = new Intent(getApplication(), YelpActivity.class);
        searchIntent.putExtra("term", placeName);
        searchIntent.putExtra("latitude", lat);
        searchIntent.putExtra("longitude", lng);
        startActivity(searchIntent);

    }

    public void dialogRequestPermission(){
        new MaterialDialog.Builder(this)
            .titleColorRes(R.color.pink)
            .title("Map Permission")
            .contentColor(Color.BLACK)
            .content("Allow us to Navigate you to Nearest places." +
                    "Goto settings->App->click on GrabIt->permissions->Allow")
            .positiveColor(getResources().getColor(R.color.red))
            .positiveText("Agree")
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            })
            .negativeColor(getResources().getColor(R.color.red))
            .negativeText("Disagree")
            .onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    Toasty.error(getApplication(),"Location needs to be activated",Toast.LENGTH_SHORT,true).show();
                dialogRequestPermission();
                }
            })
            .show();
    }

    //TODO Location module needs to be implemented

    public boolean getCurrentLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)){
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            }
        } else {
            // Permission has already been granted
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);
            if(!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                new MaterialDialog.Builder(this)
                        .title("Location Services")
                        .content("Enable the Location Service")
                        .positiveText("Enable")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        }).show();

            }else {
                try{
                    lat = String.valueOf(location.getLatitude());
                    lng = String.valueOf(location.getLongitude());
                }catch (Exception e)
                {

                }
            }

        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    dialogRequestPermission();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            lat = String.valueOf(location.getLongitude());
            lng = String.valueOf(location.getLatitude());

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }


    };
}