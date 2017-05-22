package com.cn.chaos.pictureselect;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PhotoViewActivity extends AppCompatActivity {

    private ViewPager        mViewPager;
    private PtotoViewAdapter mPtotoViewAdapter;
    private List<View> mViewList = new ArrayList<>();
    private int        mPosition = 0;
    private ArrayList<CharSequence> mImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        mViewPager = (ViewPager) findViewById(R.id.vp);

        mImages = getIntent().getCharSequenceArrayListExtra("images");
        mPosition = getIntent().getIntExtra("position", 0);
        initData();

    }

    private void initData() {
        initViewList();
        setViewPage();
    }

    private void initViewList() {
        mViewList.clear();
        for (int i = 0; i < mImages.size(); i++) {
            View view = View.inflate(this, R.layout.photo_view_list, null);

            mViewList.add(view);
        }
    }

    private void setViewPage() {
        mPtotoViewAdapter = new PtotoViewAdapter(this, mViewList, mImages);
        mViewPager.setAdapter(mPtotoViewAdapter);
        mViewPager.setCurrentItem(mPosition);
    }
}
