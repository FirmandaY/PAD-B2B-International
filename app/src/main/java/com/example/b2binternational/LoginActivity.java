package com.example.b2binternational;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private Button mRegisterActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mRegisterActivity = findViewById(R.id.buttonRegister);
        mRegisterActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toRegister1Activity();
            }
        });
    }

    private  void toRegister1Activity(){
        Intent intent = new Intent(this, Register1Activity.class);
        startActivity(intent);
    }
}