package com.example.pedrolemos.livrosfinal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.Utils;
import com.example.pedrolemos.livrosfinal.adapters.LivrosAdapter;
import com.example.pedrolemos.livrosfinal.model.BookResponse;
import com.example.pedrolemos.livrosfinal.model.Items;
import com.example.pedrolemos.livrosfinal.model.VolumeInfo;
import com.example.pedrolemos.livrosfinal.receivers.NetworkChangeReceiver;
import com.example.pedrolemos.livrosfinal.rest.ApiClient;
import com.example.pedrolemos.livrosfinal.rest.ApiInterface;
import com.example.pedrolemos.livrosfinal.utils.RecyclerItemClickListener;
import com.vlonjatg.progressactivity.ProgressLayout;
import com.wang.avi.AVLoadingIndicatorView;
import com.yalantis.jellytoolbar.listener.JellyListener;
import com.yalantis.jellytoolbar.widget.JellyToolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultadosActivity extends AppCompatActivity {

    private final static String API_KEY = "AIzaSyD9mU0KD5bAf4ZcPAOhFR9JUIkA9U0o19c";
    private String TAG = ResultadosActivity.class.getSimpleName();

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    @BindView(R.id.progressActivity)
    ProgressLayout progressLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_connectedResultados)
    TextView tv_connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NetworkChangeReceiver mNetworkReceiver = new NetworkChangeReceiver();
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

        registerReceiver();

        if (!isNetworkAvailable()) {
            progressLayout.showEmpty(R.drawable.wifi_img, "You have no Internet Connection",
                    "To check this book's information, please connect to the internet and try again!");
        }


        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Search Results");
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        avi.show();

        String isbn = getIntent().getStringExtra("ISBN");
        String category = getIntent().getStringExtra("Category");

        String url = "https://www.googleapis.com/books/v1/volumes?q=subject:" + category + "&maxResults=40&key=AIzaSyD9mU0KD5bAf4ZcPAOhFR9JUIkA9U0o19c";

        final RecyclerView recyclerView = findViewById(R.id.livros_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setVisibility(View.INVISIBLE);

        recyclerView.setItemAnimator(new DefaultItemAnimator());


        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        if (isbn == null) {

            Call<BookResponse> call = apiService.getBooksByCategory(url);
            call.enqueue(new Callback<BookResponse>() {
                @Override
                public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                    int statusCode = response.code();
                    ArrayList<Items> api = new ArrayList<Items>();
                    try {
                        Log.w(TAG, call.request().url().toString());
                        api = response.body().getItems();

                        List<VolumeInfo> livros = new ArrayList<VolumeInfo>();

                        for (int g = 0; g < api.size(); g++) {
                            //   items.add(g);
                            livros.add(api.get(g).getVolumeInfo());

                        }
                        recyclerView.setAdapter(new LivrosAdapter(livros, api, R.layout.list_item_livro, getApplicationContext()));


                        avi.hide();
                        recyclerView.setVisibility(View.VISIBLE);

                        recyclerView.addOnItemTouchListener(
                                new RecyclerItemClickListener(ResultadosActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Intent intent = new Intent(ResultadosActivity.this, LivroActivity.class);
                                        String nome = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.id)).getText().toString();
                                        intent.putExtra("ISBN", nome);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.enter, R.anim.exit);

                                    }

                                    @Override
                                    public void onLongItemClick(View view, int position) {
                                        // do whatever
                                    }
                                })
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressLayout.showEmpty(R.drawable.search, "No Search Results Found",
                                "Unfortunately I could not find any results matching your search");
                    }


                    //  List<Integer> items = new ArrayList<Integer>();


                }

                @Override
                public void onFailure(Call<BookResponse> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.toString());
                }
            });


        } else {

            Call<BookResponse> call = apiService.getBookByISBN(isbn, "relevance", "28", API_KEY);
            call.enqueue(new Callback<BookResponse>() {
                @Override
                public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                    int statusCode = response.code();


                    ArrayList<Items> api = new ArrayList<Items>();
                    try {
                        api = response.body().getItems();
                        List<VolumeInfo> livros = new ArrayList<VolumeInfo>();

                        for (int g = 0; g < api.size(); g++) {
                            //   items.add(g);
                            livros.add(api.get(g).getVolumeInfo());

                        }
                        recyclerView.setAdapter(new LivrosAdapter(livros, api, R.layout.list_item_livro, getApplicationContext()));


                        avi.hide();
                        recyclerView.setVisibility(View.VISIBLE);

                        recyclerView.addOnItemTouchListener(
                                new RecyclerItemClickListener(ResultadosActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {

                                        Intent intent = new Intent(ResultadosActivity.this, LivroActivity.class);
                                        String nome = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.id)).getText().toString();
                                        intent.putExtra("ISBN", nome);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.enter, R.anim.exit);
                                    }


                                    @Override
                                    public void onLongItemClick(View view, int position) {
                                        // do whatever
                                    }
                                })
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressLayout.showEmpty(R.drawable.search, "No Search Results Found",
                                "Unfortunately I could not find any results matching your search");
                    }


                    //  List<Integer> items = new ArrayList<Integer>();


                }

                @Override
                public void onFailure(Call<BookResponse> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.toString());
                }
            });
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    /**
     * This is internal BroadcastReceiver which get status from external receiver(NetworkChangeReceiver)
     */
    ResultadosActivity.InternalNetworkChangeReceiver internalNetworkChangeReceiver = new ResultadosActivity.InternalNetworkChangeReceiver();

    class InternalNetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getStringExtra("status").equalsIgnoreCase("internet connected")) {
                tv_connected.setVisibility(View.GONE);
            } else {
                tv_connected.setVisibility(View.VISIBLE);
            }
            //Toast.makeText(HomeActivity.this, intent.getStringExtra("status"), Toast.LENGTH_LONG).show();

        }
    }


    /**
     * This method is responsible to register receiver with NETWORK_CHANGE_ACTION.
     */
    private void registerReceiver() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(NetworkChangeReceiver.NETWORK_CHANGE_ACTION);
            registerReceiver(internalNetworkChangeReceiver, intentFilter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(internalNetworkChangeReceiver);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        super.onDestroy();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


}
