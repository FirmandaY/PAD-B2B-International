package com.example.b2binternational;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button mRegisterActivity, mLoginApp;
    private EditText mEmail, mPassword;
    private ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginApp = findViewById(R.id.buttonLogin);
        mEmail = findViewById(R.id.formEmailLogin);
        mPassword = findViewById(R.id.formPasswordLogin);
        progressBar = findViewById(R.id.loginProgressBar);
        fAuth = FirebaseAuth.getInstance();
        mRegisterActivity = findViewById(R.id.buttonRegister);

        //Cek Apakah User Telah Pernah Login Saat Membuka Aplikasi
        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mLoginApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required!");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required!");
                    return;
                }

                if (password.length() < 8){
                    mPassword.setError("Password Minimum 8 Character or more!");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // autentikasi user

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            Intent toHome = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(toHome);
                            //startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }else {
                            Toast.makeText(LoginActivity.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }

                    }
                });
            }
        });

        mRegisterActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toRegister1Activity();
            }
        });
    }

    private void toRegister1Activity(){
        Intent intent = new Intent(this, Register1Activity.class);
        startActivity(intent);
    }
}