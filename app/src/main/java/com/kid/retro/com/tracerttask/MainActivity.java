package com.kid.retro.com.tracerttask;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kid.retro.com.tracerttask.Fragment.AboutTrracertFragment;
import com.kid.retro.com.tracerttask.Fragment.ChatFragment;
import com.kid.retro.com.tracerttask.Fragment.Faq_Fragment;
import com.kid.retro.com.tracerttask.Fragment.FollowFragment;
import com.kid.retro.com.tracerttask.Fragment.HomeFragment;
import com.kid.retro.com.tracerttask.Fragment.MonitorFragment;
import com.kid.retro.com.tracerttask.Fragment.RoutePlanningFragment;
import com.kid.retro.com.tracerttask.Fragment.ScheduleFragment;
import com.kid.retro.com.tracerttask.Fragment.SetDestinationFragemnt;
import com.kid.retro.com.tracerttask.Fragment.ShowLocation;
import com.kid.retro.com.tracerttask.Fragment.TermsAndConditionFragment;
import com.kid.retro.com.tracerttask.Fragment.UserFragment;
import com.kid.retro.com.tracerttask.chat.MainActivity_Chat;
import com.kid.retro.com.tracerttask.common.AppSettings;
import com.kid.retro.com.tracerttask.common.Common;
import com.kid.retro.com.tracerttask.common.MyAlarmService;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private static Toolbar mToolbar;
    public static Bitmap bitmap;
    private SharedPreferences sharedpreferences;
    private static String MyPREFERANCE = "MyPrefs";
    private String picturePath = null;
    private String username;
    private Bitmap resize;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private ProgressDialog progress;
    private String url;

    private int id = 0;
    private String mainResult;
    private String fromReg = "no";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#f54a4a"));
        }

        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Dashboard");
        sharedpreferences = getSharedPreferences(MyPREFERANCE, Context.MODE_PRIVATE);

        Intent intent = new Intent(MainActivity.this, MyAlarmService.class);
        startService(intent);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        // populate the navigation drawer

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);

        picturePath = AppSettings.getProfileIMG(MainActivity.this);


        if (picturePath != null) {
            if (Common.isOnline(MainActivity.this)) {
                new Async_RegistrationTask().execute();
            } else {
                Common.displayToast(MainActivity.this, "Please check internet connectivity");
            }

        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
            username = AppSettings.getUserName(MainActivity.this);
            if (username != null) {
                mNavigationDrawerFragment.setUserData(username, "johndoe@doe.com", bitmap);
            } else {
                mNavigationDrawerFragment.setUserData(username, "johndoe@doe.com", bitmap);
            }
        }
        mNavigationDrawerFragment.closeDrawer();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        //  if(intentFromReg!=null){

        //}
    }

    private void uploadProfilePicFromReg() {
        Log.e("Uploding", "DP");
        Bitmap bitmap = null;

        try {
            Log.e("Uploading", "Bitnmap" + bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArrayImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        if (Common.isOnline(MainActivity.this)) {

            //mProgressDialog.show();

            new Async_PictureUpload(byteArrayImage, encodedImage).execute();

        } else {
            Common.displayToast(MainActivity.this, "Please check internet connectivity");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public class Async_RegistrationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Loading . . .");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            url = "http://tracert.retroinfotech.com/" + picturePath;


            resize = getBitmapFromURL(url);
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (progress.isShowing()) {
                progress.dismiss();
            }


            if (resize != null) {
                username = AppSettings.getUserName(MainActivity.this);
                resize = Bitmap.createScaledBitmap(resize, 200, 200, false);
                if (username != null) {
                    mNavigationDrawerFragment.setUserData(username, "johndoe@doe.com", resize);
                } else {
                    mNavigationDrawerFragment.setUserData(username, "johndoe@doe.com", resize);
                }
            } else {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
                resize = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
                mNavigationDrawerFragment.setUserData(username, "johndoe@doe.com", bitmap);
            }
        }
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Intent intent2 = getIntent();
        if (intent2.hasExtra("id")) {
            id = intent2.getIntExtra("id", 0);
            if (id == 1) {
                Common.latitude = intent2.getStringExtra("latitude");
                Common.longitude = intent2.getStringExtra("longitude");
                Common.id = id;
            }
        }
        if (Common.id == 1) {
            position = 3;
        } else if (Common.id == 2) {
            position = 9;
        }

        showView(position);
    }


    Fragment fragment = null;

    public void showView(int position) {
        switch (position) {

            case 0:
                fragment = new HomeFragment();
                changeTitle("Dashboard");
                break;

            case 1:
                fragment = new SetDestinationFragemnt();
                changeTitle("SetDestination");
                break;

            case 2:
                fragment = new ShowLocation();
                changeTitle("Location History");
                break;

            case 3:
                fragment = new ScheduleFragment();
                changeTitle("Reminders");
                break;

            case 4:
                fragment = new RoutePlanningFragment();
                changeTitle("Route Planning");
                break;

            case 5:
                fragment = new MonitorFragment();
                changeTitle("Live");
                break;

            case 6:
                fragment = new ChatFragment();
                changeTitle("Contacts");
                break;

            case 7:
                fragment = new AboutTrracertFragment();
                changeTitle("About Us");
                break;

            case 8:
                fragment = new Faq_Fragment();
                changeTitle("FAQs");
                break;

            case 9:
                fragment = new TermsAndConditionFragment();
                changeTitle("Terms");
                break;

            case 10:

                if (fragment!=null && fragment instanceof HomeFragment) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Do you want to Logout ?");
                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            // Write your code here to invoke YES event

                            AppSettings.setisLoggedIn(MainActivity.this, false);
                            AppSettings.setProfileIMG(MainActivity.this, null);
                            AppSettings.setUserName(MainActivity.this, null);
                            AppSettings.setEmail(MainActivity.this, null);
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                } else {
                    fragment = new HomeFragment();
                    changeTitle("Dashboard");
                }

                break;

            default:
                break;
        }


        if (fragment != null) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
        }

        if (fragment != null && fragment instanceof HomeFragment) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    MainActivity.this);

            alertDialogBuilder
                    .setMessage("Do you want to exit ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            showView(0);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.userProfie:
                fragment = new UserFragment();
                FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
                transaction1.replace(R.id.container, fragment);
                transaction1.commit();
                /*Fragment fragment = null;
                fragment = new UserFragment();*/
                Toast.makeText(MainActivity.this, "User Profile", Toast.LENGTH_SHORT).show();
                changeTitle("Profile");
                if (mNavigationDrawerFragment.isDrawerOpen()) {
                    mNavigationDrawerFragment.closeDrawer();
                }
                return true;
            case R.id.add_contacts:
                fragment = new FollowFragment();
                FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
                transaction2.replace(R.id.container, fragment);
                transaction2.commit();
                Toast.makeText(MainActivity.this, "GPS", Toast.LENGTH_SHORT).show();
                changeTitle("Follow");
                if (mNavigationDrawerFragment.isDrawerOpen()) {
                    mNavigationDrawerFragment.closeDrawer();
                }
                return true;
            case R.id.chat:

                Intent i = new Intent(MainActivity.this, MainActivity_Chat.class);
                startActivity(i);
//                Toast.makeText(MainActivity.this, "CHAT", Toast.LENGTH_SHORT).show();
                if (mNavigationDrawerFragment.isDrawerOpen()) {
                    mNavigationDrawerFragment.closeDrawer();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.kid.retro.com.tracerttask/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.kid.retro.com.tracerttask/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public class Async_PictureUpload extends AsyncTask<Void, Void, Void> {


        private String imagePath;
        private byte[] byteA;
        private String str;

        public Async_PictureUpload(byte[] byteArray, String encodedImage) {
            byteA = byteArray;
            str = encodedImage;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Loading . . .");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub


            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"email_id\""),
                            RequestBody.create(null, AppSettings.getEmail(MainActivity.this)))
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"photo_data\""),
                            RequestBody.create(null, str))
                    //RequestBody.create(MEDIA_TYPE_JPG, new File("\"profile_picture")))
                    //.addPart(requestBodyImg)
                    .build();


            try {


                //      Request request = new Request.Builder().url("http://tracert.retroinfotech.com/upload_dp_api.php").post(requestBody).build();


                //Response response = client.newCall(request).execute();

                // if (response.isSuccessful()) {

                mainResult = postRun(requestBody);
                Log.e("RESPONSE", "MAIN" + mainResult);
                JSONObject jsonObject = new JSONObject(mainResult);

                if (jsonObject.has("img_path")) {
                    imagePath = jsonObject.getString("img_path");
                }

                url = "http://tracert.retroinfotech.com/" + imagePath;
                resize = getBitmapFromURL(url);


                //}


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (progress.isShowing()) {
                progress.dismiss();
            }

        }
    }

    public String postRun(RequestBody formBody) throws IOException {

        OkHttpClient client = new OkHttpClient();
        String str = "";
        Request request = new Request.Builder()
                .url("http://tracert.retroinfotech.com/upload_dp_api.php")
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static void changeTitle(String name) {
        if (mToolbar != null) {
            mToolbar.setTitle(name);
        }
    }
}
