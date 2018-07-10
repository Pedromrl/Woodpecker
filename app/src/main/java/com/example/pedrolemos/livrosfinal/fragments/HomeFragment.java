package com.example.pedrolemos.livrosfinal.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pedrolemos.livrosfinal.BottomViewActivity;
import com.example.pedrolemos.livrosfinal.LivroActivity;
import com.example.pedrolemos.livrosfinal.LoginActivity;
import com.example.pedrolemos.livrosfinal.R;
import com.example.pedrolemos.livrosfinal.ResultadosActivity;
import com.example.pedrolemos.livrosfinal.adapters.CategoriasAdapter;
import com.example.pedrolemos.livrosfinal.adapters.FavoritosAdapter;
import com.example.pedrolemos.livrosfinal.adapters.LivrosAdapter;
import com.example.pedrolemos.livrosfinal.model.BookResponse;
import com.example.pedrolemos.livrosfinal.model.Items;
import com.example.pedrolemos.livrosfinal.model.UserFavorites;
import com.example.pedrolemos.livrosfinal.model.VolumeInfo;
import com.example.pedrolemos.livrosfinal.rest.ApiClient;
import com.example.pedrolemos.livrosfinal.rest.ApiInterface;
import com.example.pedrolemos.livrosfinal.utils.RecyclerItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    @BindView(R.id.progressActivityHome)
    ProgressRelativeLayout progressRelativeLayout;

    @BindView(R.id.categoriaString1)
    TextView categoriaString1;

    @BindView(R.id.categoriaString2)
    TextView categoriaString2;

    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;

    private FirebaseAuth firebaseAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);

        categoriaString1.setVisibility(View.INVISIBLE);
        categoriaString2.setVisibility(View.INVISIBLE);


        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.recomendar_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setVisibility(View.VISIBLE);

        recyclerView2 = view.findViewById(R.id.recomendar_recycler_view2);
        RecyclerView.LayoutManager mLayoutManager2 = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(mLayoutManager2);
        recyclerView2.setVisibility(View.VISIBLE);


        String utilizador = firebaseAuth.getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference(utilizador);
        final DatabaseReference favoritosRef = databaseReference.child("Favoritos");

        favoritosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<UserFavorites> favoritos = new ArrayList<>();
                List<String> categoriasCount = new ArrayList<>();

                //Object data = dataSnapshot.getKey();

                for (DataSnapshot individual : dataSnapshot.getChildren()) {
                    String key = individual.getKey();
                    //DatabaseReference bora = databaseReference.child(key);

                    UserFavorites userFavorites = dataSnapshot.child(key).getValue(UserFavorites.class);
                    favoritos.add(userFavorites);
                    try {
                        if (userFavorites.categoria.equals("No Categories Listed")) {

                        } else {
                            categoriasCount.add(userFavorites.categoria);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (favoritos.isEmpty()) {
                    progressRelativeLayout.showEmpty(R.drawable.sad, "Not Enough Categories Found",
                            "Add at least 2 favorites with categories to your list so that we can recommend you new books!");

                } else {
                    if (categoriasCount.size() > 1) {
                        String espaco = " ";
                        String tratar1 = categoriasCount.get(0);
                        String tratar2 = categoriasCount.get(1);


                        if (tratar1.contains(espaco) || tratar2.contains(espaco)) {
                            enviarCategoria(tratar1);
                            enviarCategoria2(tratar2);
                            recyclerView.setVisibility(View.VISIBLE);

                        } else {
                            enviarCategoria(tratar1);
                            enviarCategoria2(tratar2);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                    } else {
                        progressRelativeLayout.showEmpty(R.drawable.sad, "Not Enough Categories Found",
                                "Add at least 2 favorites with categories to your list so that we can recommend you new books!");

                    }


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }

    private void enviarCategoria(String categoria) {

        String lol = "If you like " + categoria + ", you might like these:";
        categoriaString1.setText(lol);
        categoriaString1.setVisibility(View.VISIBLE);

        /*
        Spannable spannable = new SpannableString(lol);
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)), lol.indexOf(categoria), lol.indexOf(categoria) + categoria.length(),     Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        categoriaString1.setText(spannable);
        */

        String url = "https://www.googleapis.com/books/v1/volumes?q=category:" + categoria + "&maxResults=15&orderBy=newest&key=AIzaSyD9mU0KD5bAf4ZcPAOhFR9JUIkA9U0o19c";

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<BookResponse> call = apiService.getBooksByCategory(url);
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                int statusCode = response.code();
                ArrayList<Items> api = new ArrayList<Items>();
                try {
                    Log.w("LINK", call.request().url().toString());
                    api = response.body().getItems();

                    List<VolumeInfo> livros = new ArrayList<VolumeInfo>();

                    for (int g = 0; g < api.size(); g++) {
                        //   items.add(g);
                        livros.add(api.get(g).getVolumeInfo());

                    }
                    recyclerView.setAdapter(new CategoriasAdapter(livros, api, R.layout.list_item_categoria, getActivity()));


                    recyclerView.addOnItemTouchListener(
                            new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(getContext(), LivroActivity.class);
                                    String nome = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.id_cat)).getText().toString();
                                    intent.putExtra("ISBN", nome);
                                    startActivity(intent);

                                }

                                @Override
                                public void onLongItemClick(View view, int position) {
                                    // do whatever
                                }
                            })
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    progressRelativeLayout.showEmpty(R.drawable.search, "No Search Results Found",
                            "Unfortunately I could not find any results matching your search");

                }


                //  List<Integer> items = new ArrayList<Integer>();


            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("Erro", t.toString());
            }
        });
    }

    private void enviarCategoria2(String categoria) {

        String lol2 = "If you like " + categoria + ", you might like these:";
        categoriaString2.setText(lol2);
        categoriaString2.setVisibility(View.VISIBLE);
        /*
        Spannable spannable = new SpannableString(lol);
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)), lol.indexOf(categoria), lol.indexOf(categoria) + categoria.length(),     Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        categoriaString2.setText(spannable);
        */

        String url = "https://www.googleapis.com/books/v1/volumes?q=category:" + categoria + "&maxResults=15&orderBy=relevance&key=AIzaSyD9mU0KD5bAf4ZcPAOhFR9JUIkA9U0o19c";

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<BookResponse> call = apiService.getBooksByCategory(url);
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                int statusCode = response.code();
                ArrayList<Items> api = new ArrayList<Items>();
                try {
                    Log.w("LINK", call.request().url().toString());
                    api = response.body().getItems();

                    List<VolumeInfo> livros = new ArrayList<VolumeInfo>();

                    for (int g = 0; g < api.size(); g++) {
                        //   items.add(g);
                        livros.add(api.get(g).getVolumeInfo());

                    }
                    recyclerView2.setAdapter(new CategoriasAdapter(livros, api, R.layout.list_item_categoria, getActivity()));


                    recyclerView2.addOnItemTouchListener(
                            new RecyclerItemClickListener(getContext(), recyclerView2, new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(getContext(), LivroActivity.class);
                                    String nome = ((TextView) recyclerView2.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.id_cat)).getText().toString();
                                    intent.putExtra("ISBN", nome);
                                    startActivity(intent);

                                }

                                @Override
                                public void onLongItemClick(View view, int position) {
                                    // do whatever
                                }
                            })
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    progressRelativeLayout.showEmpty(R.drawable.search, "No Search Results Found",
                            "Unfortunately I could not find any results matching your search");
                }


                //  List<Integer> items = new ArrayList<Integer>();


            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("ERRO", t.toString());
            }
        });
    }
}

