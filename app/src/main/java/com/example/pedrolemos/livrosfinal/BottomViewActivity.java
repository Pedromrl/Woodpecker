package com.example.pedrolemos.livrosfinal;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.Utils;
import com.example.pedrolemos.livrosfinal.fragments.AccountFragment;
import com.example.pedrolemos.livrosfinal.fragments.HomeFragment;
import com.example.pedrolemos.livrosfinal.fragments.ScanFragment;
import com.example.pedrolemos.livrosfinal.fragments.CategoriesFragment;
import com.example.pedrolemos.livrosfinal.receivers.NetworkChangeReceiver;
import com.google.firebase.auth.FirebaseAuth;
import com.yalantis.jellytoolbar.listener.JellyListener;
import com.yalantis.jellytoolbar.widget.JellyToolbar;

import org.w3c.dom.Text;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottomViewActivity extends AppCompatActivity {

    @BindView(R.id.bottom_view)
    BottomNavigationView mBottomNavigationView;
   /* @BindView(R.id.toolbar)
    Toolbar mToolbar; */

   @BindView(R.id.tv_connected)
   TextView tv_connected;

    private String frag;

    Snackbar mySnackbar;

    private static final String TEXT_KEY = "text";


    private JellyToolbar toolbar;
    private AppCompatEditText editText;
    private JellyListener jellyListener = new JellyListener() {
        @Override
        public void onCancelIconClicked() {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            if (TextUtils.isEmpty(editText.getText())) {
                toolbar.collapse();
            } else {
                editText.getText().clear();
            }
        }

        @Override
        public void onToolbarExpandingStarted() {
            super.onToolbarExpandingStarted();
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    };


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_view);

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NetworkChangeReceiver mNetworkReceiver = new NetworkChangeReceiver();
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

        registerReceiver();

        mySnackbar = Snackbar.make(findViewById(R.id.product_clMainView), R.string.nointernet, Snackbar.LENGTH_INDEFINITE);
        mySnackbar.setActionTextColor(getColor(R.color.colorItem));
        View sbView = mySnackbar.getView();
        sbView.setBackgroundColor(Color.RED);
        sbView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        TextView txtv = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);



        toolbar = (JellyToolbar) findViewById(R.id.toolbar);
        // toolbar.getToolbar().setNavigationIcon(R.drawable.ic_menu);

        toolbar.setJellyListener(jellyListener);
        toolbar.getToolbar().setPadding(0, getStatusBarHeight(), 0, 0);

        editText = (AppCompatEditText) LayoutInflater.from(this).inflate(R.layout.edit_text, null);
        editText.setBackgroundResource(R.color.colorTransparent);
        toolbar.setContentView(editText);


        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(editText.getText().toString());
                    return true;
                }
                return false;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            disableShiftMode(mBottomNavigationView);
        }

        if (!isNetworkAvailable()) {
            mySnackbar.show();
        }



        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragCategory = null;
                // init corresponding fragment
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        fragCategory = new HomeFragment();
                        frag = "0";
                        break;
                    case R.id.menu_categories:
                        fragCategory = new CategoriesFragment();
                        frag = "1";
                        break;
                    case R.id.menu_services:
                        fragCategory = new ScanFragment();
                        frag = "2";
                        break;
                    case R.id.menu_account:
                        fragCategory = new AccountFragment();
                        frag = "3";
                        break;
                }
                //Set bottom menu selected item text in toolbar
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(item.getTitle());
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragCategory, frag);
                //transaction.addToBackStack(null);
                transaction.commit();
                return true;
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new HomeFragment());
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(TEXT_KEY, editText.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        editText.setText(savedInstanceState.getString(TEXT_KEY));
        editText.setSelection(editText.getText().length());

        /*Fragment visibleFragment=getCurrentFragment();
        if (visibleFragment.toString().contains("HomeFragment")){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new HomeFragment());
            transaction.commit();
        } else if (visibleFragment.toString().contains("CategoriesFragment")){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new CategoriesFragment());
            transaction.commit();
        }
        Log.d("RESTOREINSTANCE", visibleFragment.toString()); */
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
        }
    }

    private void performSearch(String procura) {
        Intent intent = new Intent(BottomViewActivity.this, ResultadosActivity.class);
        intent.putExtra("ISBN", procura.toString());
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    /*Fragment getCurrentFragment()
    {
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.container);
        return currentFragment;
    }*/

    /**
     * This is internal BroadcastReceiver which get status from external receiver(NetworkChangeReceiver)
     */
    InternalNetworkChangeReceiver internalNetworkChangeReceiver = new InternalNetworkChangeReceiver();

    class InternalNetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getStringExtra("status").equalsIgnoreCase("internet connected")){

              //  tv_connected.setVisibility(View.GONE);
                mySnackbar.dismiss();
            }
            else{
                //tv_connected.setVisibility(View.VISIBLE);
                mySnackbar.show();
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