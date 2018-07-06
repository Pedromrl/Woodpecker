package com.example.pedrolemos.livrosfinal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedrolemos.livrosfinal.receivers.NetworkChangeReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.emailparareset)
    EditText email;

    @BindView(R.id.btnResetpassword)
    LinearLayout reset;

    @BindView(R.id.btnBackToLogin)
    TextView back;

    @BindView(R.id.tv_connectedForgot)
    TextView tv_connected;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NetworkChangeReceiver mNetworkReceiver = new NetworkChangeReceiver();
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

        registerReceiver();

        firebaseAuth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = email.getText().toString().trim();
                if (useremail.equals("")){
                    Toast.makeText(ForgotPasswordActivity.this, "Please insert your email", Toast.LENGTH_LONG).show();
                }else{
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ForgotPasswordActivity.this, "The password reset has been sent to your email address", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                            }else{
                                Toast.makeText(ForgotPasswordActivity.this, "There has been an error. Check if the email is correct or if your internet is connected", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
    }

    /**
     * This is internal BroadcastReceiver which get status from external receiver(NetworkChangeReceiver)
     */
    ForgotPasswordActivity.InternalNetworkChangeReceiver internalNetworkChangeReceiver = new ForgotPasswordActivity.InternalNetworkChangeReceiver();

    class InternalNetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getStringExtra("status").equalsIgnoreCase("internet connected")){
                tv_connected.setVisibility(View.GONE);
            }
            else{
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
}
