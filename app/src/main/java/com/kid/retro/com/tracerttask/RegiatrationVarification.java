package com.kid.retro.com.tracerttask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kid.retro.com.tracerttask.AppIntro.MainActivity_Appintro;
import com.kid.retro.com.tracerttask.common.AppSettings;

public class RegiatrationVarification extends Activity {

    private EditText mEdtVarificationCode;
    private RelativeLayout mRelSubmitVarification;

    private String mVarificationNumber = "null";

    private Intent intent;

    private String otp;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#f54a4a"));
        }

        setContentView(R.layout.activity_regiatration_varification);

        intent = getIntent();

        if (intent != null) {
            otp = intent.getStringExtra("otp");
            email = intent.getStringExtra("email");
            Log.d("OTP VAR", "" + otp);
        }

        mEdtVarificationCode = (EditText) findViewById(R.id.edtVarificationCode);
        mRelSubmitVarification = (RelativeLayout) findViewById(R.id.relSendVarifiction);

        mRelSubmitVarification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mVarificationNumber = mEdtVarificationCode.getText().toString();
                if (mVarificationNumber.equals(otp)) {

                    AppSettings.setisVerified(RegiatrationVarification.this, true);
                    Intent intent = new Intent(RegiatrationVarification.this, MainActivity_Appintro.class);
                    startActivity(intent);
                    finish();
                } else {
                    AppSettings.setisVerified(RegiatrationVarification.this, false);
                    Toast.makeText(RegiatrationVarification.this, "Please, Enter valid confirmation code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
