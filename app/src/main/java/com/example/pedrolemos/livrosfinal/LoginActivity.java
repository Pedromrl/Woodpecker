package com.example.pedrolemos.livrosfinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etEmail)
    EditText email;

    @BindView(R.id.etPassword)
    EditText password;

    @BindView(R.id.login)
    LinearLayout login;

    @BindView(R.id.signup)
    LinearLayout signup;

    @BindView(R.id.forgotpassword)
    TextView forgotpassword;

    @BindView(R.id.aviView)
    RelativeLayout aviView;


    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        aviView.setVisibility(View.INVISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();

        //Verificar se algum utilizador já está logged in e se estiver mandá-lo automaticamente para a SecondActivity
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            startActivity(new Intent(LoginActivity.this, BottomViewActivity.class));
            finish();
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validar()){
                    aviView.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                startActivity(new Intent(LoginActivity.this, BottomViewActivity.class));
                            }else{
                                Toast.makeText(LoginActivity.this, "Login failed. Check your credentials and/or your internet connection", Toast.LENGTH_LONG).show();
                                aviView.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }else{

                }
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });


    }

    private Boolean validar(){
        Boolean result = false;
        String mail = email.getText().toString();
        String pass = password.getText().toString();

        if (mail.isEmpty() || pass.isEmpty()){
            Toast.makeText(LoginActivity.this, "Please don't leave any field empty", Toast.LENGTH_LONG).show();
        }else{
            result = true;
        }
        return result;
    }
}
