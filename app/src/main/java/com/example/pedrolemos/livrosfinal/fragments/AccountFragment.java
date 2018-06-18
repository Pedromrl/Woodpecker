package com.example.pedrolemos.livrosfinal.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedrolemos.livrosfinal.LoginActivity;
import com.example.pedrolemos.livrosfinal.R;
import com.example.pedrolemos.livrosfinal.SettingsActivity;
import com.example.pedrolemos.livrosfinal.adapters.FavoritosAdapter;
import com.example.pedrolemos.livrosfinal.adapters.LivrosAdapter;
import com.example.pedrolemos.livrosfinal.model.UserFavorites;
import com.example.pedrolemos.livrosfinal.utils.RecyclerItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.wang.avi.AVLoadingIndicatorView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AccountFragment extends Fragment {

    @BindView(R.id.progressActivity)
    ProgressRelativeLayout progressRelativeLayout;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    @BindView(R.id.logout)
    TextView settings;

    @BindView(R.id.favoritesString)
    TextView favoritesString;

    FirebaseAuth firebaseAuth;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        ButterKnife.bind(this, view);

        firebaseAuth = FirebaseAuth.getInstance();

        favoritesString.setVisibility(View.INVISIBLE);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    startActivity(new Intent(getContext(), SettingsActivity.class));
                   // getActivity().finish();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });


        avi.show();

        final RecyclerView recyclerView =  view.findViewById(R.id.favoritos_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setVisibility(View.INVISIBLE);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        String utilizador = firebaseAuth.getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference(utilizador);
        final DatabaseReference favoritosRef = databaseReference.child("Favoritos");

        favoritosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<UserFavorites> favoritos = new ArrayList<>();

                //Object data = dataSnapshot.getKey();

                for (DataSnapshot individual: dataSnapshot.getChildren()) {
                    String key = individual.getKey();
                    //DatabaseReference bora = databaseReference.child(key);

                    UserFavorites userFavorites = dataSnapshot.child(key).getValue(UserFavorites.class);
                    favoritos.add(userFavorites);
                }
                if (favoritos.isEmpty()){
                    progressRelativeLayout.showEmpty(R.drawable.star_warning, "You have no favorites yet!",
                            "Add a book to your favorites so that you can see it here.");
                    favoritesString.setVisibility(View.INVISIBLE);

                }else{
                    recyclerView.setAdapter(new FavoritosAdapter(favoritos, R.layout.list_item_favorito, getContext()));
                    recyclerView.setVisibility(View.VISIBLE);
                    favoritesString.setVisibility(View.VISIBLE);
                }



                avi.hide();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        /*PieChart pieChart;
        pieChart = view.findViewById(R.id.grafico);

        float[] yData = {5, 10, 15, 30, 40};
        final String[] xData = {"Sony", "Huawei", "LG", "Apple", "Samsung"};


        //Configure pie chart
        pieChart.setUsePercentValues(true);
        pieChart.setDescription("Favorite Categories");

        //Enable hole and configure
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(R.color.colorTransparent);
        pieChart.setHoleRadius(7);
        pieChart.setTransparentCircleRadius(10);

        //Enable rotation of the chart by touch
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);

        //Set a chart value selected listener
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if(e == null){
                    return;
                }
                Toast.makeText(getContext(), e.getData() + "%", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });


        Legend legend = pieChart.getLegend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        legend.setXEntrySpace(7);
        legend.setYEntrySpace(5);

        List<PieEntry> yVals1 = new ArrayList<PieEntry>();
        for (int i = 0; i < yData.length; i++){
            yVals1.add(new PieEntry(yData[i], i));
        }

        List<String> xVals1 = new ArrayList<String>();
        for (int i = 0; i < xData.length; i++){
            xVals1.add(xData[i]);
        }

        PieDataSet dataSet = new PieDataSet(yVals1, "Market Share");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.GRAY);

        pieChart.setData(pieData);

        pieChart.highlightValues(null);

        pieChart.invalidate();*/


        return view;
    }

   /* private void apagarFavorito(DatabaseReference ref){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot d = dataSnapshot.child(idFav.getText().toString());
                d.getRef().removeValue();
                try{
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,
                            new AccountFragment()).commit();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    } */

}
