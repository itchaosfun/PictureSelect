package com.cn.chaos.pictureselect;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HSAEE on 2017/5/18.
 */
public class CustomAlbumAdapter extends BaseAdapter {

    private Context mContext;
    private List<CustomAlbumImgBean> mImageBeen = new ArrayList<>();

    public CustomAlbumAdapter(Context context, List<CustomAlbumImgBean> imageBeen) {

        this.mContext = context;
        this.mImageBeen = imageBeen;

    }

    @Override
    public int getCount() {
        return mImageBeen.size();
    }

    @Override
    public CustomAlbumImgBean getItem(int position) {
        return mImageBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.custom_album_view_item, null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv);
        TextView textView = (TextView) convertView.findViewById(R.id.tv);

        CustomAlbumImgBean customAlbumImgBean = mImageBeen.get(position);
        String topImagePath = customAlbumImgBean.getTopImagePath();

        textView.setText(customAlbumImgBean.getImageCounts() + "张图片");

        Glide.with(mContext).load(new File(topImagePath)).into(imageView);

        return convertView;
    }
}
