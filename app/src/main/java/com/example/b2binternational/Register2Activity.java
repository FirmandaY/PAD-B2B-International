package com.example.b2binternational;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Register2Activity extends AppCompatActivity {

    private Button mButtonRegisterAccount;
    private Button mButtonBackToRegister1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        mButtonBackToRegister1 = findViewById(R.id.buttonBackToRegister1);
        mButtonBackToRegister1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}