package com.example.varun.grabit.Interface;

import com.example.varun.grabit.modelclass.BusinessDataModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by kvaru on 5/6/2018.
 */

public interface ApiInterface {
    @Headers("Authorization:Bearer ksNxSbVbfEpPjLoPc6VnbhVVRAGyNTArdwZG9uSaChSWUnq1xtE7iocDmfZV5Fvzi5xEqWMXdw_EQj11dg-iJ5nEERaCZmvem8KyImaIwbDHbAtn9YdeuyCkU23vWnYx")
    @GET("businesses/search")
    Call<BusinessDataModel> getBusiness(@Query("term") String term, @Query("latitude") String lat, @Query("longitude") String lng, @Query("sort_by") String sort_by);

}
