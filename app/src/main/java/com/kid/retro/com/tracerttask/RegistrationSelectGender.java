package com.kid.retro.com.tracerttask;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class RegistrationSelectGender extends AppCompatActivity {

    private ImageView mImgMale;
    private ImageView mImgFemale;
    private String mGender;

    private Intent intent;

    private String email;
    private String username;
    private String phonenumber;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#1cbbb4"));
        }

        setContentView(R.layout.activity_registration_select_gender);

        mImgMale = (ImageView) findViewById(R.id.imgGenderMale);
        mImgFemale = (ImageView) findViewById(R.id.imgGenderFemale);

        intent = getIntent();

        if (intent != null) {
            email = intent.getStringExtra("email");
            password = intent.getStringExtra("password");
            username = intent.getStringExtra("username");
            phonenumber = intent.getStringExtra("phonenumber");

        }


        mImgMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGender = "male";
                mImgMale.setBackgroundResource(R.drawable.user_male_selected);
                Intent intent = new Intent(RegistrationSelectGender.this, RegistrationUDetails.class);
                intent.putExtra("gender", mGender);
                intent.putExtra("email", email);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                intent.putExtra("phonenumber", phonenumber);
                startActivity(intent);
            }
        });

        mImgFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGender = "female";
                mImgFemale.setBackgroundResource(R.drawable.user_female_selected);
                Intent intent = new Intent(RegistrationSelectGender.this, RegistrationUDetails.class);
                intent.putExtra("gender", mGender);
                intent.putExtra("email", email);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                intent.putExtra("phonenumber", phonenumber);
                startActivity(intent);
            }
        });

    }
}
