package com.example.b2binternational;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register1Activity extends AppCompatActivity {

    // Membuat Variabel untuk setiap EditText
    EditText mCompName;
    EditText mCountryName;
    EditText mAddress;
    EditText mPhoneNumber;

    // Membuat Default nilai untuk data inputan
    private String KEY_COMPANY = "COMPANY";
    private String KEY_COUNTRY = "COUNTRY";
    private String KEY_ADDRESS = "ADDRESS";
    private String KEY_PHONE = "PHONE";

    //private Button mButtonBackToLogin;
    private Button mButtonNextToRegister2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        mButtonNextToRegister2 = findViewById(R.id.buttonToRegister2);
        mCompName = findViewById(R.id.formCompanyName);
        mCountryName = findViewById(R.id.formCountry);
        mAddress = findViewById(R.id.formAddress);
        mPhoneNumber = findViewById(R.id.formPhoneNumber);

        mButtonNextToRegister2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Pengambilan data dari EditText untuk divalidasi
                String company = mCompName.getText().toString().trim();
                String country = mCountryName.getText().toString().trim();
                String address = mAddress.getText().toString().trim();
                String phone = mPhoneNumber.getText().toString().trim();

                //Pelaksanaan Validasi Inputan Dari Pengguna
                if (TextUtils.isEmpty(company)){
                    mCompName.setError("Company Name is Required!");
                    return;
                }

                if (TextUtils.isEmpty(country)){
                    mCountryName.setError("Country is Required!");
                    return;
                }

                if (TextUtils.isEmpty(address)){
                    mAddress.setError("Please Confirm Your Address!");
                    return;
                }

                if (TextUtils.isEmpty(phone)){
                    mPhoneNumber.setError("Phone Number is Required!");
                    return;
                }

                toRegister2Activity();
            }
        });
    }

    private void toRegister2Activity(){
        String company = mCompName.getText().toString();
        String country = mCountryName.getText().toString();
        String address = mAddress.getText().toString();
        String phone = mPhoneNumber.getText().toString();

        Intent passRegister1 = new Intent(Register1Activity.this, Register2Activity.class);
        passRegister1.putExtra(KEY_COMPANY, company);
        passRegister1.putExtra(KEY_COUNTRY, country);
        passRegister1.putExtra(KEY_ADDRESS, address);
        passRegister1.putExtra(KEY_PHONE, phone);

        startActivity(passRegister1);
    }
}