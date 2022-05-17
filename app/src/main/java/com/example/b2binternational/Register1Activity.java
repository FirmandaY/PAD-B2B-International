package com.example.b2binternational;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Register1Activity extends AppCompatActivity {

    //private Button mButtonBackToLogin;
    private Button mButtonNextToRegister2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        mButtonNextToRegister2 = findViewById(R.id.buttonToRegister2);
        mButtonNextToRegister2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toRegister2Activity();
            }
        });
    }

    private void toRegister2Activity(){
        Intent intent = new Intent(this, Register2Activity.class);
        startActivity(intent);
    }
}