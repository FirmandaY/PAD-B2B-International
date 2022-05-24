package com.example.b2binternational;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register2Activity extends AppCompatActivity {

    public static final String TAG = "TAG";
    //Definisi komponen dari file XML
    private EditText mFormEmailAddress, mFormUsername, mFormPassword, mFormConfirmPassword;
    private ProgressBar registerProgressBar2;

    private Button mButtonRegisterAccount;
    //private Button mUploadCertificate;

    //Definisi ke Firebase
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    // Mengambil Default nilai untuk data inputan dari Register1
    private String company;
    private String country;
    private String address;
    private String phone;

    private String KEY_COMPANY = "COMPANY";
    private String KEY_COUNTRY = "COUNTRY";
    private String KEY_ADDRESS = "ADDRESS";
    private String KEY_PHONE = "PHONE";

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

        //mUploadCertificate = findViewById(R.id.buttonUploadCertificate);
        mButtonRegisterAccount = findViewById(R.id.buttonRegisterAccount);

        //Pemanggilan Fungsi (Inisiasi) Authentikasi yang Terhubung ke Firebase
        fAuth = FirebaseAuth.getInstance();

        //Pemanggilan Fungsi (Inisiasi) Firestore DB
        fStore = FirebaseFirestore.getInstance();

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
                String username = mFormUsername.getText().toString();

                Bundle fromRegister1 = getIntent().getExtras();
                company = fromRegister1.getString(KEY_COMPANY);
                country = fromRegister1.getString(KEY_COUNTRY);
                address = fromRegister1.getString(KEY_ADDRESS);
                phone = fromRegister1.getString(KEY_PHONE);

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

                if (!confirmPassword.matches(password)){
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

                            //Mendefinisikan User ID (UID)
                            userID = fAuth.getCurrentUser().getUid();

                            //Membuat document(semacam tabel jika di SQL) berdasarkan User ID (UID)
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String, Object>user = new HashMap<>();

                            //Memasukkan data ke dalam document Ket: user.put(nama kolom/atribut , data inputan);
                            user.put("Company Name", company);
                            user.put("Country", country);
                            user.put("Address", address);
                            user.put("Phone", phone);

                            user.put("Username", username);
                            user.put("Email", email);

                            //Untuk menampilkan Pesan Log saat berhasil atau gagal
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "onSuccess: user Profile is Created for " + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else {
                            Toast.makeText(Register2Activity.this, "Error: Register Failed !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            registerProgressBar2.setVisibility(View.GONE);

                        }
                    }
                });
            }
        });
    }
}