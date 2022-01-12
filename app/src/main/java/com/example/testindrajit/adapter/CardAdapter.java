package com.example.testindrajit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.testindrajit.MainActivity;
import com.example.testindrajit.R;
import com.example.testindrajit.model.MovieModel;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<MovieModel> movieModelArrayList;
    Context ctx;

    public CardAdapter(Context ctx, ArrayList<MovieModel> movieModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.movieModelArrayList = movieModelArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_movielist, parent, false);
        CardAdapter.MyViewHolder holder = new CardAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.tvType.setText(movieModelArrayList.get(position).getType());
        holder.tvMoviename.setText(movieModelArrayList.get(position).getTitle());
        holder.tvYear.setText(movieModelArrayList.get(position).getYear());
        Glide.with(ctx)
                .load(movieModelArrayList.get(position).getPoster())
                .placeholder(R.drawable.pic2)
                .into(holder.imgBanner);
        holder.ll_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity) ctx).ViewMovies(movieModelArrayList.get(position));


            }
        });


    }

    @Override
    public int getItemCount() {
        return movieModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgBanner;
        TextView tvType, tvMoviename, tvYear;
        RelativeLayout ll_details;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvType = itemView.findViewById(R.id.tvType);
            imgBanner = itemView.findViewById(R.id.imgBanner);
            tvMoviename = itemView.findViewById(R.id.tvMoviename);
            tvYear = itemView.findViewById(R.id.tvYear);
            ll_details = itemView.findViewById(R.id.ll_details);


        }
    }

}
