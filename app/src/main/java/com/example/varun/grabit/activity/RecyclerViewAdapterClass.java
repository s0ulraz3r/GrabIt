package com.example.varun.grabit.activity;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.varun.grabit.R;
import com.example.varun.grabit.modelclass.Business;

import java.util.List;

/**
 * Created by kvaru on 5/10/2018.
 */

public class RecyclerViewAdapterClass extends RecyclerView.Adapter<RecyclerViewAdapterClass.ViewHolder> {


    //private static Context = YelpActivity;
    private List<Business> mBusinessesData;
    private RequestManager glide;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textViewName, textViewAddress, textViewPrice, textViewPhone, textViewRatingValue, textViewDistance, textReviewCount;
        public RatingBar ratingBar;
        public CardView parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imgView);
            textViewName = (TextView) itemView.findViewById(R.id.placeName);
            textViewAddress = (TextView) itemView.findViewById(R.id.placeAddress);
            textViewPhone = (TextView) itemView.findViewById(R.id.contactNumber);
            textViewPrice = (TextView) itemView.findViewById(R.id.placePrice);
            textViewRatingValue = (TextView) itemView.findViewById(R.id.textViewRating);
            textViewDistance = (TextView) itemView.findViewById(R.id.distanceTextView);
            ratingBar = (RatingBar) itemView.findViewById(R.id.placeRating);
            parentLayout = (CardView) itemView.findViewById(R.id.parentLayout);
            textReviewCount = (TextView) itemView.findViewById(R.id.reviewCount);
        }
    }

    public RecyclerViewAdapterClass(List<Business> mBusinessesData, Context mContext) {
        this.mBusinessesData = mBusinessesData;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public RecyclerViewAdapterClass.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.yelp_display_cardview,parent,false);
        ViewHolder vh =  new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterClass.ViewHolder holder, final int position) {
        final Business business = mBusinessesData.get(position);
        Glide.with(mContext).load(business.getImageUrl()).apply(new RequestOptions().override(200,200).centerCrop()).into(holder.imageView);
        holder.textViewName.setMovementMethod(new ScrollingMovementMethod());
        holder.textViewName.setText(business.getName());
        final String s = String.valueOf(business.getLocation().getDisplayAddress()).replaceAll("\\[","").replaceAll("\\]","");
//        holder.textViewAddress.setMovementMethod(new ScrollingMovementMethod());
        holder.textViewAddress.setText(s);
        holder.textViewAddress.setSelected(true);
        if (business.getDisplayPhone().isEmpty()){
            holder.textViewPhone.setText("N/A");
        }else{
            holder.textViewPhone.setText(business.getDisplayPhone());
        }
        double miles = business.getDistance();          //For miles to display in double value format
        String m = String.valueOf(miles/1609.34);
        String convertedMiles = m.substring(0, Math.min(m.length(),4));
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            DecimalFormat decimalFormat = new DecimalFormat("##.##");
//            holder.textViewDistance.setText(String.valueOf(decimalFormat.format(miles/1000))+"mi");
//        }
        holder.textViewDistance.setText(convertedMiles+"mi");
        holder.textViewPrice.setText(business.getPrice());
        holder.textViewRatingValue.setText(String.valueOf(business.getRating()));
        holder.ratingBar.setRating(business.getRating());
        final String reviewCount = String.valueOf(business.getReviewCount());
        holder.textReviewCount.setText(String.valueOf(reviewCount));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(mContext,reviewCount,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext,MainDisplayActivity.class);
                intent.putExtra("PlaceName", business.getName());
                intent.putExtra("ImgUrl", business.getImageUrl());
                intent.putExtra("Location",s);
                intent.putExtra("Phone",business.getDisplayPhone());
                intent.putExtra("ReviewCount",reviewCount);
                intent.putExtra("Rating",String.valueOf(business.getRating()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mBusinessesData.size();
    }



}
