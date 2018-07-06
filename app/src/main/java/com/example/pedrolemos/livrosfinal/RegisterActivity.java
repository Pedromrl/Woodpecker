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
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedrolemos.livrosfinal.receivers.NetworkChangeReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.etEmailR)
    EditText email;

    @BindView(R.id.etUsernameR)
    EditText username;

    @BindView(R.id.etPasswordR)
    EditText password;

    @BindView(R.id.etConfirmPasswordR)
    EditText confirmPassword;

    @BindView(R.id.signupR)
    LinearLayout signup;

    @BindView(R.id.btnBackToLoginR)
    TextView backToLogin;

    @BindView(R.id.tv_connectedRegister)
    TextView tv_connected;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NetworkChangeReceiver mNetworkReceiver = new NetworkChangeReceiver();
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

        registerReceiver();

        firebaseAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validar()){
                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "User was registered successfuly", Toast.LENGTH_SHORT).show();
                                fazerLogin(email.getText().toString(), password.getText().toString());
                            }else{
                                Toast.makeText(RegisterActivity.this, "The registration failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{

                }
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });


    }

    private Boolean validar(){
        String user = username.getText().toString().trim();
        String mail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confirm = confirmPassword.getText().toString().trim();


        /** Validar se o email está na forma correta */
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            Toast.makeText(RegisterActivity.this, "Enter a valid email", Toast.LENGTH_LONG).show();
            return false;
        }

        /** Validar se os campos estão todos preenchidos */
        if (user.isEmpty() || mail.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please don't leave any field empty", Toast.LENGTH_LONG).show();
            return false;
        }else{
            /** Verificar se a password é igual à sua confirmação */
            if(pass.equals(confirm)){

                return true;
            }
            else{
                Toast.makeText(RegisterActivity.this, "The confirm password isn't the same as the password", Toast.LENGTH_LONG).show();
                return false;
            }
        }


    }


    /** Método que faz login automaticamente após o registo seja um sucesso */
    private void fazerLogin(String nm, String ps){
        firebaseAuth.signInWithEmailAndPassword(nm, ps).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this, "Login unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /** Método que volta para o login se for premido o botão Back no dispositivo */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    /**
     * This is internal BroadcastReceiver which get status from external receiver(NetworkChangeReceiver)
     */
    RegisterActivity.InternalNetworkChangeReceiver internalNetworkChangeReceiver = new RegisterActivity.InternalNetworkChangeReceiver();

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
