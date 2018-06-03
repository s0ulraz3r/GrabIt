package com.example.varun.grabit.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.varun.grabit.Interface.ApiInterface;
import com.example.varun.grabit.R;
import com.example.varun.grabit.modelclass.Business;
import com.example.varun.grabit.modelclass.BusinessDataModel;
import com.example.varun.grabit.rest.ApiRequestClass;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YelpActivity extends AppCompatActivity {
    ApiInterface apiInterface;
    private RecyclerViewAdapterClass mAdapter;
    private List<Business> businessArrayList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private String term;
    private String latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yelp);



        Intent yelpIntent = getIntent();
        term = yelpIntent.getStringExtra("term");
        latitude = yelpIntent.getStringExtra("latitude");
        longitude = yelpIntent.getStringExtra("longitude");
        String sort_by = "distance";
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        apiInterface = ApiRequestClass.getClient().create(ApiInterface.class);
        Call<BusinessDataModel> businessDataModelCall = apiInterface.getBusiness(term,latitude,longitude,sort_by);
        businessDataModelCall.enqueue(new Callback<BusinessDataModel>() {
            @Override
            public void onResponse(Call<BusinessDataModel> call, Response<BusinessDataModel> response) {
               try {
                   List<Business> businesses = response.body().getBusinesses();
                   if(businesses.isEmpty()){
                       Toasty.custom(getApplication(),"Oops.. try other place", R.drawable.ic_sentiment_dissatisfied_black_24dp, getResources().getColor(R.color.light_black), Toast.LENGTH_SHORT,true,true).show();
                   }else {
                       businessArrayList = businesses;
                       mAdapter = new RecyclerViewAdapterClass(businessArrayList,getApplicationContext());
                       mLayoutManager = new LinearLayoutManager(getApplicationContext());
                       mRecyclerView.setLayoutManager(mLayoutManager);
                       mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                       mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL));
                       mRecyclerView.setAdapter(mAdapter);
                       mAdapter.notifyDataSetChanged();
                       Log.d("YData", "Number of data received: " + businesses.size());}

               }catch (Exception e){
                   Log.e("Exception",e.toString());
               }

            }

            @Override
            public void onFailure(Call<BusinessDataModel> call, Throwable t) {
                Log.e("Error", t.toString());
            }
        });






    }


}
