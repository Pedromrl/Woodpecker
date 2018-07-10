package com.example.pedrolemos.livrosfinal.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.pedrolemos.livrosfinal.LivroActivity;
import com.example.pedrolemos.livrosfinal.R;
import com.example.pedrolemos.livrosfinal.ResultadosActivity;
import com.example.pedrolemos.livrosfinal.model.BookResponse;
import com.example.pedrolemos.livrosfinal.model.Items;
import com.example.pedrolemos.livrosfinal.rest.ApiClient;
import com.example.pedrolemos.livrosfinal.rest.ApiInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoriesFragment extends Fragment {

    private final static String API_KEY = "AIzaSyD9mU0KD5bAf4ZcPAOhFR9JUIkA9U0o19c";

    @BindView(R.id.horrorCard)
    CardView horrorCard;
    @BindView(R.id.romanceCard)
    CardView romanceCard;
    @BindView(R.id.actionCard)
    CardView actionCard;
    @BindView(R.id.dramaCard)
    CardView dramaCard;
    @BindView(R.id.adventureCard)
    CardView adventureCard;
    @BindView(R.id.historyCard)
    CardView historyCard;
    @BindView(R.id.artCard)
    CardView artCard;
    @BindView(R.id.natureCard)
    CardView natureCard;
    @BindView(R.id.scienceCard)
    CardView scienceCard;
    @BindView(R.id.businessCard)
    CardView businessCard;
    @BindView(R.id.comedyCard)
    CardView comedyCard;
    @BindView(R.id.sportsCard)
    CardView sportsCard;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        ButterKnife.bind(this, view);

        YoYo.with(Techniques.SlideInUp).playOn(horrorCard);
        YoYo.with(Techniques.SlideInUp).playOn(romanceCard);
        YoYo.with(Techniques.SlideInUp).playOn(actionCard);
        YoYo.with(Techniques.SlideInUp).playOn(dramaCard);
        YoYo.with(Techniques.SlideInUp).playOn(adventureCard);
        YoYo.with(Techniques.SlideInUp).playOn(historyCard);
        YoYo.with(Techniques.SlideInUp).playOn(artCard);
        YoYo.with(Techniques.SlideInUp).playOn(natureCard);
        YoYo.with(Techniques.SlideInUp).playOn(scienceCard);
        YoYo.with(Techniques.SlideInUp).playOn(businessCard);
        YoYo.with(Techniques.SlideInUp).playOn(comedyCard);
        YoYo.with(Techniques.SlideInUp).playOn(sportsCard);

        horrorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ResultadosActivity.class);
                intent.putExtra("Category", "horror");
                startActivity(intent);
            }
        });

        romanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ResultadosActivity.class);
                intent.putExtra("Category", "romance");
                startActivity(intent);
            }
        });

        actionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ResultadosActivity.class);
                intent.putExtra("Category", "action");
                startActivity(intent);
            }
        });

        dramaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ResultadosActivity.class);
                intent.putExtra("Category", "drama");
                startActivity(intent);
            }
        });

        adventureCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ResultadosActivity.class);
                intent.putExtra("Category", "adventure");
                startActivity(intent);
            }
        });

        historyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ResultadosActivity.class);
                intent.putExtra("Category", "history");
                startActivity(intent);
            }
        });

        artCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ResultadosActivity.class);
                intent.putExtra("Category", "art");
                startActivity(intent);
            }
        });

        natureCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ResultadosActivity.class);
                intent.putExtra("Category", "nature");
                startActivity(intent);
            }
        });

        scienceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ResultadosActivity.class);
                intent.putExtra("Category", "science");
                startActivity(intent);
            }
        });

        businessCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ResultadosActivity.class);
                intent.putExtra("Category", "business");
                startActivity(intent);
            }
        });

        comedyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ResultadosActivity.class);
                intent.putExtra("Category", "humor");
                startActivity(intent);
            }
        });

        sportsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ResultadosActivity.class);
                intent.putExtra("Category", "sports");
                startActivity(intent);
            }
        });


        return view;
    }
}
