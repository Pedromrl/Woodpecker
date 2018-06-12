package com.example.pedrolemos.livrosfinal;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedrolemos.livrosfinal.model.BookResponse;
import com.example.pedrolemos.livrosfinal.model.Items;
import com.example.pedrolemos.livrosfinal.model.UserFavorites;
import com.example.pedrolemos.livrosfinal.rest.ApiClient;
import com.example.pedrolemos.livrosfinal.rest.ApiInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.vlonjatg.progressactivity.ProgressLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LivroActivity extends AppCompatActivity {

    @BindView(R.id.ivCapa)
    ImageView capa;

    @BindView(R.id.tvNome)
    TextView nome;

    @BindView(R.id.tvData)
    TextView dataL;

    @BindView(R.id.tvPags)
    TextView paginas;

    @BindView(R.id.tvPublisher)
    TextView publisher;

    @BindView(R.id.tvCategorias)
    TextView categorias;

    @BindView(R.id.tvAutor)
    TextView autor;

    @BindView(R.id.tvDescricao)
    TextView descricao;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

   /* @BindView(R.id.avi)
    AVLoadingIndicatorView avi; */

    @BindView(R.id.progressActivityLivro)
    ProgressLayout progressLayout;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String idFire, nomeFire, categoriaFire, autorFire, descricaoFire, capaFire;



    private final static String API_KEY = "AIzaSyD9mU0KD5bAf4ZcPAOhFR9JUIkA9U0o19c";
    private String TAG = LivroActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livro);

        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Book Info");
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });



        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        //avi.show();

        duranteLoading();

        String isbn = getIntent().getStringExtra("ISBN");

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<BookResponse> call = apiService.getBookByISBN(isbn, "relevance", "1", API_KEY);
            call.enqueue(new Callback<BookResponse>() {
                @Override
                public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                    int statusCode = response.code();

                    //TENTAR VER SE HA RESULTADOS
                    try{
                        Items result = response.body().getItems().get(0);
                        alterarTexto(result);
                      //  avi.hide();
                        depoisLoading();

                    }catch (Exception e){
                        e.printStackTrace();
                    //   avi.hide();
                        progressLayout.showEmpty(R.drawable.search, "Sorry!",
                                "Google doesn't have any more information about this book.");


                    }







                  /*  if (response.body().getItems().size() > 0){
                        Items hehe = response.body().getItems().get(0);
                        Log.w(TAG, hehe.getVolumeInfo().getAuthors().toString());
                    }else{
                        Log.w(TAG, "erro");
                    } */



                }

                @Override
                public void onFailure(Call<BookResponse> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(String.valueOf(call), t.toString());
                }
            });

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fab.setImageResource(R.drawable.ic_favorito_checked);

                    try{
                        String utilizador = firebaseAuth.getCurrentUser().getUid();
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference mRef = firebaseDatabase.getReference(utilizador);

                        UserFavorites user = new UserFavorites(nomeFire, autorFire, descricaoFire, categoriaFire, capaFire);
                        mRef.child("Favoritos").child(idFire).setValue(user);

                        Toast.makeText(LivroActivity.this, nomeFire + " has been added to your favorites", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(LivroActivity.this, "Couldn't add book to favorites", Toast.LENGTH_SHORT).show();
                    }


                }
            });

    }

    private void alterarTexto(Items api){

        //Tentar Imagem
        try{
            Picasso.with(LivroActivity.this).load(api.getVolumeInfo().getImageLinks().getThumbnail()).fit().centerCrop().into(capa);
            capaFire = api.getVolumeInfo().getImageLinks().getThumbnail();
        }catch (Exception e){
            e.printStackTrace();
            Picasso.with(LivroActivity.this).load(R.drawable.ic_close).fit().centerCrop().into(capa);
            capaFire = "R.drawable.ic_close";
        }

        //TENTAR TITULO
        try{
            nome.setText(api.getVolumeInfo().getTitle());
            nomeFire = api.getVolumeInfo().getTitle();

        }catch (Exception e){
            e.printStackTrace();
            nome.setText("Unknown Book Name");
            nomeFire = nome.getText().toString();
        }


        //TENTAR AUTOR
        try{
            autor.setText(api.getVolumeInfo().getAuthors().get(0));
            autorFire = autor.getText().toString();
        }catch (Exception e){
            e.printStackTrace();
            autor.setText("Unknown Author");
            autorFire = autor.getText().toString();
        }

        //TENTAR PUBLISHER
        try{
            publisher.setText("Publisher: " + api.getVolumeInfo().getPublisher());
        }catch (Exception e){
            e.printStackTrace();
            publisher.setText("Unknown Publisher");
        }

        //TENTAR DATA DE RELEASE
        try{
            dataL.setText("Release date: " + api.getVolumeInfo().getPublishedDate());
        }catch (Exception e){
            e.printStackTrace();
            dataL.setText("Unknown Release Date");
        }

        //TENTAR NUMERO DE PAGINAS
        try{
            paginas.setText("Page number: "+ Integer.toString(api.getVolumeInfo().getPageCount()));
        }catch (Exception e){
            e.printStackTrace();
            paginas.setText("Unknown Number of Pages");
        }

        //TENTAR CATEGORIAS
        try{
            categorias.setText("Category: " + api.getVolumeInfo().getCategories().get(0));
            categoriaFire = api.getVolumeInfo().getCategories().get(0);
        }catch (Exception e){
            e.printStackTrace();
            categorias.setText("No Categories Listed");
            categoriaFire = categorias.getText().toString();
        }

        //TENTAR DESCRIÇÃO
        try{
            descricao.setText(api.getVolumeInfo().getDescription());
            descricaoFire = descricao.getText().toString();
            if (descricao.getText().equals("")){
                descricao.setText("No description available at the moment");
            }
        }catch (Exception e){
            e.printStackTrace();
            descricao.setText("No description available at the moment");
        }


        try {
            idFire = api.getId();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void duranteLoading(){
        capa.setVisibility(View.INVISIBLE);
        nome.setVisibility(View.INVISIBLE);
        autor.setVisibility(View.INVISIBLE);
        publisher.setVisibility(View.INVISIBLE);
        dataL.setVisibility(View.INVISIBLE);
        descricao.setVisibility(View.INVISIBLE);
        paginas.setVisibility(View.INVISIBLE);
        categorias.setVisibility(View.INVISIBLE);
    }

    private void depoisLoading(){
        capa.setVisibility(View.VISIBLE);
        nome.setVisibility(View.VISIBLE);
        autor.setVisibility(View.VISIBLE);
        publisher.setVisibility(View.VISIBLE);
        dataL.setVisibility(View.VISIBLE);
        descricao.setVisibility(View.VISIBLE);
        paginas.setVisibility(View.VISIBLE);
        categorias.setVisibility(View.VISIBLE);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }



}
