package com.kid.retro.com.tracerttask;

import android.app.Application;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

/**
 * Created by Harsh on 5/5/2016.
 */

@ReportsCrashes(
        formUri = "https://babulpatel1309.cloudant.com/acra-babul/_design/acra-storage/_update/report",
//      formUri = "https://babulpatel1309.cloudant.com/acra-babul/_design/acra-storage/_update/report",

        reportType = HttpSender.Type.JSON,
        httpMethod = HttpSender.Method.POST,
//        mailTo = "babulpatel1309@gmail.com",
        formUriBasicAuthLogin = "ghtinarrossinlyinellskin",
        formUriBasicAuthPassword = "aa39f636873f26f30a54054587a4f28a1dc43bed",
//        formKey = "", // This is required for backward compatibility but not used
        customReportContent = {
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.PACKAGE_NAME,
                ReportField.REPORT_ID,
                ReportField.BUILD,
                ReportField.STACK_TRACE
        }
        ,
        mode = ReportingInteractionMode.TOAST
//        resToastText = R.string.toast_crash
)

public class CrashReport extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
//            ACRA.init(this);
        }catch (Exception e){

        }
    }

}
