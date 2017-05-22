package com.cn.chaos.pictureselect;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by HSAEE on 2017/5/17.
 */
public class ShowImageAdapter extends BaseAdapter {

    private ArrayList<CharSequence> mBitmapList = new ArrayList<>();
    private Context mContext;

    public ShowImageAdapter(ArrayList<CharSequence> bitmapList, Context context) {
        this.mBitmapList = bitmapList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mBitmapList.size();
    }

    @Override
    public String getItem(int position) {
        return (String) mBitmapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.show_image_view_item, null);
        }

        final ImageView imgView = (ImageView) convertView.findViewById(R.id.iv);
        Glide.with(mContext)
                .load(mBitmapList.get(position))
                .into(imgView);

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PhotoViewActivity.class);
                intent.putCharSequenceArrayListExtra("images", mBitmapList);
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });

        imgView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mBitmapList.remove(position);
                notifyDataSetChanged();
                return false;
            }
        });

        return convertView;
    }
}
