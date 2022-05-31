package com.example.b2binternational;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.type.DateTimeOrBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Register2Activity extends AppCompatActivity {

    public static final String TAG = "TAG";
    //Definisi komponen dari file XML
    private EditText mFormEmailAddress, mFormUsername, mFormPassword, mFormConfirmPassword;
    private ImageView mImagePreview;
    private ProgressBar registerProgressBar2;

    private Button mButtonRegisterAccount;
    //private Button mUploadCertificate;

    //Definisi ke Firebase
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference fStorage;
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

        mImagePreview = findViewById(R.id.imagePreview);

        registerProgressBar2 = findViewById(R.id.registerProgressBar2);

        mButtonRegisterAccount = findViewById(R.id.buttonRegisterAccount);

        //Pemanggilan Fungsi (Inisiasi) Authentikasi yang Terhubung ke Firebase
        fAuth = FirebaseAuth.getInstance();

        //Pemanggilan Fungsi (Inisiasi) Firestore DB
        fStore = FirebaseFirestore.getInstance();

        //Pemanggilan Fungsi (Inisiasi) Firebase Storage File
        fStorage = FirebaseStorage.getInstance().getReference();

        //Cek Apakah User Telah Pernah Login Saat Membuka Aplikasi
        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        //Definisi tindakan jika Button Upload ditekan
        mImagePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Menjalankan fungsi untuk mencari sumber gambar
                selectImage();
            }
        });

        //Definisi tindakan jika Button Register ditekan
        mButtonRegisterAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mFormEmailAddress.getText().toString().trim();
                String password = mFormPassword.getText().toString().trim();
                String confirmPassword = mFormConfirmPassword.getText().toString().trim();


                //Pelaksanaan Validasi Inputan Dari Pengguna
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
                            uploadImage();
                            Toast.makeText(Register2Activity.this, "Register Completed! New User Added!", Toast.LENGTH_SHORT).show();
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

    //Fungsi untuk pencarian sumber gambar
    private void  selectImage(){
        final CharSequence[] items = {"Take Photo", "Choose From Device", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Register2Activity.this);
        builder.setTitle(getString(R.string.app_name));
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(items, (dialog, item) -> {
            //Mengambil data langsung foto kamera
            if (items[item].equals("Take Photo")) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 10);

            //Mengambil data dari penyimpanan / galeri
            }else if (items[item].equals("Choose From Device")) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent , "Select Image"), 20);

            //Membatalkan Aksi Popup
            }else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        //Menampilkan Aksi Popup
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Penerimaan data dari galeri
        if (requestCode == 20 && resultCode == RESULT_OK && data != null){
            final Uri path = data.getData();
            Thread thread = new Thread(() -> {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(path);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    mImagePreview.post(() -> {
                        mImagePreview.setImageBitmap(bitmap);
                    });
                }catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }

        //Penerimaan data dari kamera langsung
        if (requestCode == 10 && resultCode == RESULT_OK) {
            final Bundle extras = data.getExtras();
            Thread thread = new Thread(() -> {
                Bitmap bitmap = (Bitmap) extras.get("data");
                mImagePreview.post(() -> {
                    mImagePreview.setImageBitmap(bitmap);
                });
            });
            thread.start();
        }
    }

    //Untuk upload data gambar ke Storage
    private void uploadImage(){
        //Compress data
        mImagePreview.setDrawingCacheEnabled(true);
        mImagePreview.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) mImagePreview.getDrawable() ).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        //Simpan data ke Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference("images").child("IMG" + new Date().getTime() + ".jpeg");
        UploadTask uploadTask = reference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getMetadata() != null){
                    if (taskSnapshot.getMetadata().getReference() != null){
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.getResult() != null){
                                    saveData(task.getResult().toString());
                                    Toast.makeText(getApplicationContext(), "Sukses Menyimpan data Gambar!", Toast.LENGTH_SHORT).show();

                                }else{
                                    Toast.makeText(getApplicationContext(), "Terjadi Kesalahan Pada saveData!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Terjadi Kesalahan Pada getReference!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Terjadi Kesalahan Pada getMetadata!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveData(String mImagePreview){

        String email = mFormEmailAddress.getText().toString().trim();
        String username = mFormUsername.getText().toString();

        Bundle fromRegister1 = getIntent().getExtras();
        company = fromRegister1.getString(KEY_COMPANY);
        country = fromRegister1.getString(KEY_COUNTRY);
        address = fromRegister1.getString(KEY_ADDRESS);
        phone = fromRegister1.getString(KEY_PHONE);

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
        user.put("dokumen", mImagePreview);

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

        if (userID != null){
            fStore.collection("users").document(userID).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "d", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "n", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else {
            fStore.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getApplicationContext(), "New User Has Made!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }) .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}