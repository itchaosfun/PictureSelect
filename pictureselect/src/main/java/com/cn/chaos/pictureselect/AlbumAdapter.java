package com.cn.chaos.pictureselect;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by HSAEE on 2017/5/18.
 */

public class AlbumAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<CharSequence> mArrayList = new ArrayList<>();
    private ArrayList<Boolean>      mBooleen   = new ArrayList<>();

    public AlbumAdapter(Context context, ArrayList<CharSequence> arrayList) {

        this.mContext = context;
        this.mArrayList = arrayList;
        for (int i = 0; i < mArrayList.size(); i++) {
            mBooleen.add(false);
        }

    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public String getItem(int position) {
        return (String) mArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.album_view_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.tv);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.pv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mBooleen.get(position)) {
            viewHolder.mTextView.setText("已选中");
        } else {
            viewHolder.mTextView.setText("");
        }

        Glide.with(mContext).load(new File((String) mArrayList.get(position))).into(viewHolder.mImageView);

        final ViewHolder finalViewHolder2 = viewHolder;
        viewHolder.mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(android.view.View v) {
                if (mBooleen.get(position)) {
                    mBooleen.set(position, false);
                    finalViewHolder2.mTextView.setText("");
                } else {
                    finalViewHolder2.mTextView.setText("已选中");
                    mBooleen.set(position, true);
                }
                if (mOnImgSelectListener != null) {
                    mOnImgSelectListener.onImgSelect(position,mBooleen.get(position));
                }
                return true;
            }
        });

        viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PhotoViewActivity.class);
                intent.putCharSequenceArrayListExtra("images", mArrayList);
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    private class ViewHolder {
        private TextView  mTextView;
        private ImageView mImageView;

    }

    public interface OnImgSelectListener {
        void onImgSelect(int position, Boolean aBoolean);
    }

    private OnImgSelectListener mOnImgSelectListener;

    public void setOnImgSelectListener(OnImgSelectListener onImgSelectListener) {
        this.mOnImgSelectListener = onImgSelectListener;
    }
}
