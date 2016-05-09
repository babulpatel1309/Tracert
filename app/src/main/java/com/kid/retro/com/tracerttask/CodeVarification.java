package com.kid.retro.com.tracerttask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kid.retro.com.tracerttask.common.AppSettings;

public class CodeVarification extends Activity {

    private EditText mEdtVarificationCode;
    private RelativeLayout mRelSubmitVarification;

    private String mVarificationNumber = "null";

    private Intent intent;

    private String otp;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiatration_varification);

        intent = getIntent();

        if (intent != null) {
            otp = intent.getStringExtra("otp");
            email = intent.getStringExtra("email");
        }

        mEdtVarificationCode = (EditText) findViewById(R.id.edtVarificationCode);
        mRelSubmitVarification = (RelativeLayout) findViewById(R.id.relSendVarifiction);




        mRelSubmitVarification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mVarificationNumber = mEdtVarificationCode.getText().toString();
                if (mVarificationNumber.equals(otp)) {

                    Intent intent = new Intent(CodeVarification.this, ResetPassword.class);
                    intent.putExtra("email",email);
                    startActivity(intent);
                    finish();
                } else {
                    AppSettings.setisVerified(CodeVarification.this, false);
                    Toast.makeText(CodeVarification.this, "Please, Enter valid confirmation code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
