package com.example.varun.grabit.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.varun.grabit.R;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class MainDisplayActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private TapTargetSequence sequence;
    private ImageView mImgViewDisplay;
    private TextView mTextViewDisplayLocation, mTextViewDisplayName, mTextViewcontactNumber, mTextViewRatingValue, mTextViewBack;
    private RatingBar ratingBar;
    private FloatingActionButton mFabDrive, mFabCall, mFabReview;
    private String mReviewCount;
    private Button mSearchNewPlace;
    private SharedPreferences.Editor editor = null;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_display);

        sharedPref = this.getSharedPreferences("SEQUENCE_TAP_TARGET", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        mImgViewDisplay = (ImageView)findViewById(R.id.ImgViewDisplay);
        mTextViewcontactNumber = (TextView)findViewById(R.id.contactNumber);
        mTextViewDisplayName = (TextView)findViewById(R.id.textViewNameDisplay);
        mTextViewDisplayLocation = (TextView)findViewById(R.id.textViewDisplayLocation);
        mTextViewRatingValue = (TextView)findViewById(R.id.mainDisplayRatingValue);
        //mTextViewBack = (TextView)findViewById(R.id.tvBack);
        ratingBar = (RatingBar)findViewById(R.id.DisplayRating);

        mFabCall = (FloatingActionButton) findViewById(R.id.fab_Call);
        mFabDrive = (FloatingActionButton) findViewById(R.id.fab_Drive);
        mFabReview = (FloatingActionButton) findViewById(R.id.fab_Review);

        mSearchNewPlace = (Button)findViewById(R.id.btnNewSearch);

        mFabCall.setOnClickListener(this);
        mFabDrive.setOnClickListener(this);
        mFabReview.setOnClickListener(this);
//        tapTargetSequence();
        tapTarget();
        getData();

        mSearchNewPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplication(),SearchActivity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
            }
        });

//        mTextViewBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent newIntent = new Intent(getApplication(),YelpActivity.class);
//                startActivity(newIntent);
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_Call:
                checkSdkVersion();
                break;

            case R.id.fab_Drive:
                String destAddress = mTextViewDisplayLocation.getText().toString();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/dir/?api=1&destination="+destAddress+"&travelmode=car"));
                        startActivity(intent);
                break;

            case R.id.fab_Review:
                Toasty.success(getApplication(),"Total users Reviewed "+mReviewCount,Toast.LENGTH_SHORT).show();
                break;

        }

    }


    public void checkSdkVersion(){
        if(Build.VERSION.SDK_INT < 23){
            makeCall();
        }else {
            if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED){
                makeCall();
            }else{
                final String[] PERMISSIONS_STORAGE = {android.Manifest.permission.CALL_PHONE};
                //Asking request Permissions
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean permissionGranted = false;
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CALL_PHONE:
                permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (permissionGranted){
            makeCall();
        }else {
            Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
        }
    }

    private void makeCall() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            String phoneNumber = mTextViewcontactNumber.getText().toString();
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber));
            startActivity(callIntent);
        }else {
            Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
        }
    }


    public void getData(){
        Intent intent = getIntent();
        mTextViewDisplayName.setText(intent.getStringExtra("PlaceName"));
        mTextViewDisplayLocation.setText(intent.getStringExtra("Location"));
        Glide.with(getApplicationContext()).load(intent.getStringExtra("ImgUrl")).apply(new RequestOptions().circleCrop()).into(mImgViewDisplay);
        mTextViewcontactNumber.setText(intent.getStringExtra("Phone"));
        mReviewCount = intent.getStringExtra("ReviewCount");
        ratingBar.setRating(Float.parseFloat(intent.getStringExtra("Rating")));
        mTextViewRatingValue.setText(intent.getStringExtra("Rating"));


    }

    public void tapTarget(){
                sequence = new TapTargetSequence(this).targets(
                        TapTarget.forView(findViewById(R.id.fab_Drive), "Drive to this Location","Click this button. It will navigate you.")
                                        .dimColor(R.color.black)
                                        .outerCircleColor(R.color.color_blue)
                                        .outerCircleAlpha(0.96f)
                                        .titleTextSize(20)
                                        .titleTextColor(R.color.color_white)
                                        .targetCircleColor(R.color.color_white)
                                        .descriptionTextSize(13)
                                        .descriptionTextColor(R.color.color_white)
                                        .textTypeface(Typeface.SANS_SERIF)
                                        .textColor(R.color.color_white)
                                        .drawShadow(true)
                                        .cancelable(false)
                                        .tintTarget(true)
                                        .transparentTarget(true)
                                        .targetRadius(50),
                TapTarget.forView(findViewById(R.id.fab_Review), "Review","It shows the total number of users reviewed this place").id(2)
                        .dimColor(R.color.black)
                        .outerCircleColor(R.color.color_blue)
                        .outerCircleAlpha(0.96f)
                        .titleTextSize(20)
                        .titleTextColor(R.color.color_white)
                        .targetCircleColor(R.color.color_white)
                        .descriptionTextSize(13)
                        .descriptionTextColor(R.color.color_white)
                        .textTypeface(Typeface.SANS_SERIF)
                        .textColor(R.color.color_white)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(50),
                TapTarget.forView(findViewById(R.id.fab_Call), "Make a reservation", "Click here to call").id(3)
                        .dimColor(R.color.black)
                        .outerCircleColor(R.color.color_blue)
                        .outerCircleAlpha(0.96f)
                        .titleTextSize(20)
                        .titleTextColor(R.color.color_white)
                        .targetCircleColor(R.color.color_white)
                        .descriptionTextSize(13)
                        .descriptionTextColor(R.color.color_white)
                        .textTypeface(Typeface.SANS_SERIF)
                        .textColor(R.color.color_white)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(50)
        )
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                        Toasty.success(getApplication(),"You are educated Now!", Toast.LENGTH_SHORT).show();
                        editor.putBoolean("finished",true);
                        editor.commit();
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        Log.d("TapTargetView", "Clicked on " + lastTarget.id());
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        editor.putBoolean("finished",true);
                        editor.commit();
                    }
                });
        boolean isSequenceFinished = sharedPref.getBoolean("finished",false);
                    if(!isSequenceFinished) {
                        sequence.start();
                    }

    }

//    public void tapTargetSequence(){
//
//            tapTarget();
//            TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.fab_Drive), "Drive to this Location","Click this button. It will navigate you.")
//                    .dimColor(R.color.black)
//                    .outerCircleColor(R.color.color_blue)
//                    .outerCircleAlpha(0.96f)
//                    .titleTextSize(20)
//                    .titleTextColor(R.color.color_white)
//                    .targetCircleColor(R.color.color_white)
//                    .descriptionTextSize(13)
//                    .descriptionTextColor(R.color.color_white)
//                    .textTypeface(Typeface.SANS_SERIF)
//                    .textColor(R.color.color_white)
//                    .drawShadow(true)
//                    .cancelable(false)
//                    .tintTarget(true)
//                    .transparentTarget(true)
//                    .targetRadius(50), new TapTargetView.Listener(){
//
//                @Override
//                public void onTargetClick(TapTargetView view) {
//                    super.onTargetClick(view);
//                    boolean isSequenceFinished = sharedPref.getBoolean("finished",false);
//
//                    if(!isSequenceFinished) {
//
//                        sequence.start();
//
//                    }
//                }
//            });
//        }




}
