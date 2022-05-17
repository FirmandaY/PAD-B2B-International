package com.example.b2binternational;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Register2Activity extends AppCompatActivity {

    private EditText mFormEmailAddress, mFormUsername, mFormPassword, mFormConfirmPassword;
    private ProgressBar registerProgressBar2;

    private Button mButtonRegisterAccount;
    private Button mUploadCertificate;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        //Pendefinisian Semua komponen yang ada pada file XML (Style)
        mFormEmailAddress = findViewById(R.id.formEmail);
        mFormUsername = findViewById(R.id.formUsername);
        mFormPassword = findViewById(R.id.formPassword);
        mFormConfirmPassword = findViewById(R.id.formConfirmPassword);

        registerProgressBar2 = findViewById(R.id.registerProgressBar2);

        mUploadCertificate = findViewById(R.id.buttonUploadCertificate);
        mButtonRegisterAccount = findViewById(R.id.buttonRegisterAccount);

        //Pemanggilan Fungsi Authentikasi yang Terhubung ke Firebase
        fAuth = FirebaseAuth.getInstance();

        //Cek Apakah User Telah Pernah Login Saat Membuka Aplikasi
        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        //Pelaksanaan Validasi Inputan Dari Pengguna
        mButtonRegisterAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mFormEmailAddress.getText().toString().trim();
                String password = mFormPassword.getText().toString().trim();
                String confirmPassword = mFormConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    mFormEmailAddress.setError("Email is Required!");
                    return;
                }

                if (!email.contains("@")){
                    mFormEmailAddress.setError("Please Insert Correct Email!");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    mFormPassword.setError("Password is Required!");
                    return;
                }

                if (password.length() < 8){
                    mFormPassword.setError("Password Minimum 8 Character or more!");
                    return;
                }

                if (TextUtils.isEmpty(confirmPassword)){
                    mFormConfirmPassword.setError("Please Confirm Your Password!");
                    return;
                }

                if (confirmPassword != password){
                    mFormConfirmPassword.setError("Incorrect Password, Please Try Again!");
                    return;
                }

                registerProgressBar2.setVisibility(View.VISIBLE);

                //Register user ke Firebase
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Register2Activity.this, "Register Completed! New User Added!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else {
                            Toast.makeText(Register2Activity.this, "Error: Register Failed !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }
}