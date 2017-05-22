package com.cn.chaos.pictureselect;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by HSAEE on 2017/5/17.
 */

public class PtotoViewAdapter extends PagerAdapter {

    private Context mContext;
    private List<View>         mViewList   = new ArrayList<>();
    private List<CharSequence> mBitmapList = new ArrayList<>();

    public PtotoViewAdapter(Context context, List<View> viewList, ArrayList<CharSequence> bitmapList) {
        this.mContext = context;
        this.mViewList = viewList;
        this.mBitmapList = bitmapList;
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        container.addView(mViewList.get(position), 0);
        final PhotoView photoView = (PhotoView) mViewList.get(position).findViewById(R.id.pv);

        Glide.with(mContext)
                .load(new File((String) mBitmapList.get(position)))
                .into(photoView);

        return mViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub;
        PhotoView imageView = (PhotoView) ((View)object).findViewById(R.id.pv);
        if (imageView == null)
            return;
        Glide.clear(imageView);     //核心，解决OOM
        container.removeView(mViewList.get(position));
    }
}
