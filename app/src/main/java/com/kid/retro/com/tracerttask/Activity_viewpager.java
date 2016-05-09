package com.kid.retro.com.tracerttask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Activity_viewpager extends Activity implements View.OnClickListener {

    int count = 0;
    ArrayList<Integer> al;
    int[] mResources = {R.drawable.dash, R.drawable.left_menu,
            R.drawable.lh, R.drawable.moniter, R.drawable.rp, R.drawable.sp_sc};

    private ViewPager viewPagerItemDetails = null;
    private LinearLayout count_layout = null;
    private ImageView page_text[] = null;
    private TextView txtSkip;

    private TextView mTxtSkipIntro;

    private ViewPager mViewPager;

    SharedPreferences pref;
    Editor editor;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#f54a4a"));
        }

        setContentView(R.layout.activity_viewpager);

        initialize();
        al = new ArrayList<Integer>();
        Log.i("size", "????" + mResources.length);
        for (int i = 0; i < mResources.length; i++) {
            al.add(mResources[i]);
        }
        txtSkip = (TextView) findViewById(R.id.txtSkip);
        setIndicator();

        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_viewpager.this, MainActivity.class);
                intent.putExtra("fromRegistration", "yes");
                startActivity(intent);
            }
        });

        editor.putBoolean("ischecked", true);
        editor.commit();


    }

    public void initialize() {

        // alItemListDetails = new ArrayList<>();


    }  /* * Function to set Indicator to ViewPager. * */

    public void setIndicator() {
        //  maxIndex = al.size();
        viewPagerItemDetails = (ViewPager) findViewById(R.id.viewPagerItemDetails);
        ItemDetailsAdapter id = new ItemDetailsAdapter(Activity_viewpager.this);
        viewPagerItemDetails.setAdapter(id);
        count_layout = (LinearLayout) findViewById(R.id.image_count);
        count = al.size();
        Log.i("count", "???" + count);
        page_text = new ImageView[count];
        for (int i = 0; i < count; i++) {
            page_text[i] = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(5, 0, 5, 0);
            page_text[i].setLayoutParams(lp);
            page_text[i].setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
            count_layout.addView(page_text[i]);
        }
        viewPagerItemDetails.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                try {
                    if (position == count - 1) {
                        Toast.makeText(Activity_viewpager.this, "Dekhayu", Toast.LENGTH_SHORT).show();
                        txtSkip.setVisibility(View.VISIBLE);
                    } else {
                        txtSkip.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < count; i++) {

                        page_text[i].setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selected1));
                    }
                    page_text[position].setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.selected));
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
    }  /* * handle back button of device * */


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }  /* * Below Adapter will set the Item details View to ViewFlipper. * */

    public class ItemDetailsAdapter extends PagerAdapter {
        //  RoundedImageView imgItem = new RoundedImageView(ItemDetailsActivity.this);

        private Context context = null;

        ItemDetailsAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return al.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (View) object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imgCustom;


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.custom_itemdetails, null);
            imgCustom = (ImageView) view.findViewById(R.id.imgCustom);
            imgCustom.setImageResource(al.get(position));
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
    }


}
