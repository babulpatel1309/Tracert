package com.kid.retro.com.tracerttask.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kid.retro.com.tracerttask.NavigationDrawerFragment;
import com.kid.retro.com.tracerttask.R;
import com.kid.retro.com.tracerttask.common.AppSettings;
import com.kid.retro.com.tracerttask.common.Common;
import com.soundcloud.android.crop.Crop;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kid on 1/10/2016.
 */
public class UserFragment extends Fragment {
    private ImageView mImgProfile;
    private Uri mImageCaptureUri;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    private Button mBtnGallery, mBtnCamera;
    private String mTempFilePath = null;
    private String tempFilepath = null;
    File imagePath;
    private static String MyPREFERANCE = "MyPrefs";
    private SharedPreferences sharedpreferences;

    private TextView getUserName;
    private TextView getEmailid;
    private TextView getContactNumber;
    private TextView getFirstName;
    private TextView getLastName;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private SharedPreferences.Editor editor;
    private String picturePath;
    private String result;
    private String userEmail;
    private String username;
    private String email;
    private String phoneNumber;
    private String otp;
    private String firstname;
    private String lastname;
    private String resultFinal;
    private Bitmap resize;
    private ProgressDialog progress;

    private static final int PICK_FROM_GALLERY = 2;
    private String mainResult;
    private String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_user1, container, false);

        init(rootview);

        mImgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mImgProfile.setImageDrawable(null);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, Crop.REQUEST_PICK);


            }
        });
        return rootview;
    }

    private void init(View rootview) {

        mImgProfile = (ImageView) rootview.findViewById(R.id.imgUProfile);

        getUserName = (TextView) rootview.findViewById(R.id.getUserName);
        getEmailid = (TextView) rootview.findViewById(R.id.getEmailId);
        getContactNumber = (TextView) rootview.findViewById(R.id.getContactNumber);
        //getFirstName = (TextView) rootview.findViewById(R.id.getFName);
        //getLastName = (TextView) rootview.findViewById(R.id.getLName);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);


        picturePath = AppSettings.getProfileIMG(getActivity());

        username = AppSettings.getUserName(getActivity());

        userEmail = AppSettings.getEmail(getActivity());


        if (userEmail != null) {
            if (Common.isOnline(getActivity())) {
                new Async_RegistrationTask().execute();
            } else {
                Common.displayToast(getActivity(), "Please check internet connectivity");
            }
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == Crop.REQUEST_PICK && resultCode == getActivity().RESULT_OK) {
                beginCrop(data.getData());
            }
            if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(resultCode, data);
            }
        }

    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
        Log.i("destination", "" + destination);
        Crop.of(source, destination).asSquare().start(getActivity(), this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == getActivity().RESULT_OK) {
            mImgProfile.setImageURI(Crop.getOutput(result));

            Uri uri = Crop.getOutput(result);
            //    mImgProfile.setImageBitmap(photo);
            //    String filapeth = getRealPathFromURI(uri);


            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArrayImage = baos.toByteArray();
            String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
            if (Common.isOnline(getActivity())) {

                //mProgressDialog.show();

                new Async_PictureUpload(byteArrayImage, encodedImage).execute();

            } else {
                Common.displayToast(getActivity(), "Please check internet connectivity");
            }

            Log.i("uri", "" + uri);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getActivity(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public byte[] convertImageToByte(Uri uri) {
        byte[] data = null;
        try {
            ContentResolver cr = getActivity().getBaseContext().getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    private String getRealPathFromURI(Uri tempUri) {
        Cursor cursor = getActivity().getContentResolver().query(tempUri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private Uri getImageUri(Activity activity, Bitmap photo) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), photo, "Title", null);
        return Uri.parse(path);
    }


    public class Async_RegistrationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            progress = new ProgressDialog(getActivity());
            progress.setMessage("Loading . . .");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Register();

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
            if (resultFinal != null) {
                getEmailid.setText(email);
                //getFirstName.setText(firstname);
                //getLastName.setText(lastname);
                getUserName.setText(username);
                getContactNumber.setText(phoneNumber);


                if (resize != null) {
                    mImgProfile.setImageBitmap(resize);
                    if (username != null) {
                        mNavigationDrawerFragment.setUserData(username, "johndoe@doe.com", resize);
                    } else {
                        mNavigationDrawerFragment.setUserData(username, "johndoe@doe.com", resize);
                    }
                } else {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
                    resize = Bitmap.createScaledBitmap(bitmap, 250, 250, false);
                    mNavigationDrawerFragment.setUserData(username, "johndoe@doe.com", bitmap);
                }
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

            progress = new ProgressDialog(getActivity());
            progress.setMessage("Loading . . .");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub


       /*     RequestBody requestBody = null;
            MultipartBuilder multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
            multipartBuilder.addFormDataPart("email_id", AppSettings.getEmail(getActivity())).addFormDataPart("photo_data", "123.jpg", RequestBody.create(MediaType.parse("image*//*"), imagePath));
            requestBody = multipartBuilder.build();*/


            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"email_id\""),
                            RequestBody.create(null, email))
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

            if (picturePath != null) {
                if (resize != null) {

                    resize = Bitmap.createScaledBitmap(resize, 200, 200, false);
                    mImgProfile.setImageBitmap(resize);
                    if (username != null) {
                        mNavigationDrawerFragment.setUserData(username, "johndoe@doe.com", resize);
                    } else {
                        mNavigationDrawerFragment.setUserData(username, "johndoe@doe.com", resize);
                    }
                }
            } else {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
                Bitmap resize = Bitmap.createScaledBitmap(bitmap, 250, 250, false);

                mNavigationDrawerFragment.setUserData(username, "johndoe@doe.com", bitmap);
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

    public void Register() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url("http://tracert.retroinfotech.com/get_user_data_api.php").post(new FormEncodingBuilder().add("email_id", userEmail).build()).build();


            //  Request request = new Request.Builder().url("http://tracert.retroinfotech.com/user_location_data_api.php").post(new FormEncodingBuilder().add("email_id", email).add("latlong_method", "latlong_post").add("on_date", "29-01-2016").add("on_time", "08:50:00 PM").add("latitude", "22.60000").add("longitude", "73.30000").build()).build();

            Response response = client.newCall(request).execute();


            //resultFinal = response.body().string();


            if (!response.isSuccessful()) {
//                Toast.makeText(RegistrationActivity.this, "Please Enter Correct email and password", Toast.LENGTH_SHORT).show();
            } else {
                resultFinal = response.body().string();


                JSONObject mainObj = new JSONObject(resultFinal);

                if (mainObj.has("user_data")) {

                    JSONArray jsonArray = mainObj.getJSONArray("user_data");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.has("username")) {
                            username = jsonObject.getString("username");
                        }

                        if (jsonObject.has("email_id")) {
                            email = jsonObject.getString("email_id");
                        }

                        if (jsonObject.has("first_name")) {
                            firstname = jsonObject.getString("first_name");
                        }

                        if (jsonObject.has("mobile_no")) {
                            phoneNumber = jsonObject.getString("mobile_no");

                            // AppSettings.setContactNumber(getActivity(),phoneNumber);
                        }

                        if (jsonObject.has("last_name")) {
                            lastname = jsonObject.getString("last_name");
                        }

                    }


                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
