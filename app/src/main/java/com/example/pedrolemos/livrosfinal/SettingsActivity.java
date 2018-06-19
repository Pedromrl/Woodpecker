package com.example.pedrolemos.livrosfinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedrolemos.livrosfinal.utils.ConfirmPasswordDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity implements ConfirmPasswordDialog.OnConfirmPasswordListener {

    private Toolbar toolbar;
    private EditText newEmail;
    private CardView change;
    private TextView deleteFavs, logout;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        toolbar = findViewById(R.id.toolbarr);
        newEmail = findViewById(R.id.et_new_email);
        change = findViewById(R.id.carta_change_email);
        deleteFavs = findViewById(R.id.apagar_favoritos);
        logout = findViewById(R.id.logout_final);


        /** Indicar que a Action Bar é a toolbar que criámos, tirar o seu título automático e acrescentar a seta para voltar */
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String texto = newEmail.getText().toString().trim();
                if (texto.isEmpty() || texto.equals("")) {
                    Toast.makeText(SettingsActivity.this, "Insert an email to be able to change your current one", Toast.LENGTH_SHORT).show();
                } else {

                    ConfirmPasswordDialog confirmPasswordDialog = new ConfirmPasswordDialog();
                    confirmPasswordDialog.show(getSupportFragmentManager(), getString(R.string.confirm_password_dialog));
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                finish();
            }
        });

        deleteFavs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String utilizador = firebaseAuth.getCurrentUser().getUid();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = database.getReference(utilizador);
                final DatabaseReference favoritosRef = databaseReference.child("Favoritos");

                favoritosRef.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Toast.makeText(SettingsActivity.this, "Your favorites list has been cleared successfuly!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public void onConfirmPassword(String password) {
        Log.e("pass", password);

        AuthCredential credential = EmailAuthProvider.getCredential(firebaseAuth.getCurrentUser().getEmail(), password);

        firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    firebaseAuth.fetchSignInMethodsForEmail(newEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if (task.isSuccessful()) {

                                try {
                                    if (task.getResult().getSignInMethods().size() == 1) {
                                        Toast.makeText(SettingsActivity.this, "That email is already in use", Toast.LENGTH_SHORT).show();
                                    } else {
                                        firebaseUser.updateEmail(newEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SettingsActivity.this, "Your email has been updated!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                } catch (NullPointerException e) {
                                    Log.e("Null", e.toString());
                                }

                            }
                        }
                    });
                }
            }
        });
    }
}


