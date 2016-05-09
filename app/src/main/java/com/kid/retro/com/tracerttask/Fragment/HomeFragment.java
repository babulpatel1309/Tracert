package com.kid.retro.com.tracerttask.Fragment;


import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kid.retro.com.tracerttask.AppIntro.MainActivity_Appintro;
import com.kid.retro.com.tracerttask.MainActivity;
import com.kid.retro.com.tracerttask.Model.ItemDetailsModel;
import com.kid.retro.com.tracerttask.R;
import com.kid.retro.com.tracerttask.common.AppSettings;
import com.kid.retro.com.tracerttask.common.GPSTracker;
import com.viewpagerindicator.CirclePageIndicator;
import com.vistrav.ask.Ask;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.google.android.gms.internal.zzir.runOnUiThread;

/**
 * Created by Kid on 1/8/2016.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "Home";
    int count = 0;
    ItemDetailsModel it;
    int Image[] = {R.drawable.map_0, R.drawable.map_1, R.drawable.map_2, R.drawable.map_3, R.drawable.map_4};
    Timer timer;
    int page = 0;
    private ArrayList<ItemDetailsModel> alItemListDetails = null;
    private ViewPager viewPagerItemDetails = null;
    private ImageButton mimgBtnBack = null;
    private TextView txtHeadder = null;
    private ImageView imgDisplay;
    private int currentIndex = 0;
    private int maxIndex = 0;
    private MapView mapView;
    private GoogleMap map;
    private TextView mTxtKm, mTxtOfKm, percKm;
    private TextView txtNumFriends;
    private ImageView imgResetKm;
    private Button btnGenerate;
    private LinearLayout count_layout = null;
    private ImageView page_text[] = null;
    private ViewPagerArrowIndicator viewPagerArrowIndicator = null;
    private GPSTracker gpsTracker;
    private TextView totalkm, noReq;

    private static DecimalFormat REAL_FORMATTER = new DecimalFormat("#0.00");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);

        try {
            findViews(rootview);
            initialize(rootview);

            gpsTracker = new GPSTracker(getActivity());
            MapsInitializer.initialize(getActivity());

            mapView = (MapView) rootview.findViewById(R.id.mapviewShowHome);

            mapView.onCreate(savedInstanceState);

            map = mapView.getMap();
            map.getUiSettings().setMyLocationButtonEnabled(true);
            Ask.on(getActivity())
                    .forPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION
                            , android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                            , Manifest.permission.READ_CONTACTS) //one or more permissions
                    .withRationales("Location permission need for map to work properly",
                            "In order to save file you will need to grant storage permission",
                            "For invitation we need a permission for contacts.") //optional
                    .when(new Ask.Permission() {
                        @Override
                        public void granted(List<String> permissions) {
                            Log.i(TAG, "granted :: " + permissions);
                            map.setMyLocationEnabled(true);
                        }

                        @Override
                        public void denied(List<String> permissions) {
                            Log.i(TAG, "denied :: " + permissions);
                            map.setMyLocationEnabled(true);
                        }
                    }).go();



            if (gpsTracker.canGetLocation()) {

                //latLng = new LatLng(lat, lng);
                LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                map.addMarker(new MarkerOptions().position(latLng));

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            }else{
                Toast.makeText(getActivity(),"Please turn on Location from Setting.",Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootview;
    }

    private void findViews(View rootview) {

        totalkm = (TextView) rootview.findViewById(R.id.totalkm);
        noReq = (TextView) rootview.findViewById(R.id.noReq);
        txtNumFriends = (TextView) rootview.findViewById(R.id.txtNumFriends);

        if (AppSettings.getNumberRequest(getActivity()) != null && !AppSettings.getNumberFriends(getActivity()).equals("")) {
            noReq.setText(AppSettings.getNumberFriends(getActivity()));
        } else {
            noReq.setText("00");
//            Toast.makeText(getActivity(), "Null App Settings", Toast.LENGTH_SHORT).show();
        }


        if (AppSettings.getNumberFriends(getActivity()) != null && !AppSettings.getNumberFriends(getActivity()).equals("")) {
            txtNumFriends.setText(AppSettings.getNumberFriends(getActivity()));
        } else {
            txtNumFriends.setText("00");
//            Toast.makeText(getActivity(), "Null App Settings", Toast.LENGTH_SHORT).show();
        }


        if (AppSettings.getKm(getActivity()) != null && !AppSettings.getKm(getActivity()).equals("")) {

            String kms = AppSettings.getKm(getActivity());
            double kmsd=Double.parseDouble(kms);


            totalkm.setText("" + REAL_FORMATTER.format(kmsd) + " km");
        } else {
            totalkm.setText("0.00 km");
        }
    }

    CirclePageIndicator titleIndicator;

    private void initialize(View rootview) {

        alItemListDetails = new ArrayList<>();
        for (int i = 0; i < Image.length; i++) {
            it = new ItemDetailsModel();
            it.setStrItemName(String.valueOf(i));
            it.setStrItemImage(Image[i]);
            alItemListDetails.add(it);
        }
        String strBoxName = getActivity().getIntent().getStringExtra("ScannedBoxName");
        //txtHeadder.setText(strBoxName);
        maxIndex = alItemListDetails.size();
        viewPagerItemDetails = (ViewPager) rootview.findViewById(R.id.viewPagerItemDetails);
        //count_layout = (LinearLayout) rootview.findViewById(R.id.image_count);
        ItemDetailsAdapter id = new ItemDetailsAdapter((MainActivity) getActivity());
        viewPagerItemDetails.setAdapter(id);
        titleIndicator  = (CirclePageIndicator) rootview.findViewById(R.id.titles);



        setIndicator();

        titleIndicator.setViewPager(viewPagerItemDetails);
        titleIndicator.setCurrentItem(0);
        titleIndicator.setSnap(true);
    }

    private void setIndicator() {


        count = alItemListDetails.size();
        page_text = new ImageView[count];

        for (int i = 0; i < count; i++) {
            page_text[i] = new ImageView(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(5, 0, 5, 0);
            page_text[i].setLayoutParams(lp);
            page_text[i].setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pagination_default));
            //  count_layout.addView(page_text[i]);
        }
        pageSwitcher(3);


        viewPagerItemDetails.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                try {
                    for (int i = 0; i < count; i++) {
               /*         page_text[i]
                                .setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pagination_default));
                 */
                    }

                    page_text[position]
                            .setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pagination_selected));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    public void pageSwitcher(int seconds) {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 2000); // delay
        // in
        // milliseconds
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /*
    * Below Adapter will set the Item details View to ViewFlipper.
    * */
    public class ItemDetailsAdapter extends PagerAdapter {
        //RoundedImageView imgItem = new RoundedImageView(ItemDetailsActivity.this);
        private TextView txtItemName = null;
        private TextView txtItemDescription = null;
        private Context context = null;

        ItemDetailsAdapter(MainActivity context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return alItemListDetails.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (View) object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Bitmap placeBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.schedual), 100, 100, false);
            Drawable d = new BitmapDrawable(context.getResources(), placeBitmap);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.inflate, null);

            //txtItemName = (TextView) view.findViewById(R.id.tv);
            //txtItemName.setText(alItemListDetails.get(position).getStrItemName());
            imgDisplay = (ImageView) view.findViewById(R.id.imgInflate);
            imgDisplay.setImageResource(alItemListDetails.get(position).getStrItemImage());
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            runOnUiThread(new Runnable() {
                public void run() {

                    if (page > 4) { // In my case the number of pages are 5
                        //timer.cancel();
                        // Showing a toast for just testing purpose
                        page = 0;
                        viewPagerItemDetails.setCurrentItem(page++);
                    } else {
                        viewPagerItemDetails.setCurrentItem(page++);
                    }
                }
            });

        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(menuVisible)
            MainActivity.changeTitle("Dashboard");
    }
}
