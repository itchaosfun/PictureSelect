package com.cn.chaos.pictureselect;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CustomAlbumActivity extends AppCompatActivity {

    private static final int SCAN_OK    = 1;
    private static final int SELECT_PIC = 2;
    private GridView    mGridView;
    private ProgressBar mProgressBar;
    private Map<String, List<String>> mGruopMap = new LinkedHashMap<>();
    private CustomAlbumAdapter adapter;
    private List<String> mStringList = new ArrayList<>();
    private Handler      mHandler   = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCAN_OK:
                    mProgressBar.setVisibility(View.INVISIBLE);
                    adapter = new CustomAlbumAdapter(CustomAlbumActivity.this, subGroupOfImage());
                    mGridView.setAdapter(adapter);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_album);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        init();
    }

    private void init() {
        initView();
        initData();
    }

    private void initData() {
        getImg();
    }

    private void initView() {

        mGridView = (GridView) findViewById(R.id.gv);
        mProgressBar = (ProgressBar) findViewById(R.id.pb);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CustomAlbumActivity.this, "第" + position + "个条目", Toast.LENGTH_SHORT).show();
                ArrayList<CharSequence> lists = new ArrayList<>();
                Iterator<Map.Entry<String, List<String>>> iterator = mGruopMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, List<String>> next = iterator.next();
                    List<String> value = next.getValue();
                    String key = next.getKey();
                    if (mStringList.get(position).equals(key)) {
                        lists.addAll(value);
                        break;
                    }
                }

                Intent intent = new Intent(CustomAlbumActivity.this, AlbumActivity.class);

                intent.putCharSequenceArrayListExtra("photo", lists);

                CustomAlbumActivity.this.startActivityForResult(intent,SELECT_PIC);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK){
            return;
        }

        switch (requestCode){
            case SELECT_PIC:

                ArrayList<CharSequence> selectpic = data.getCharSequenceArrayListExtra("selectpic");

                Intent intent = new Intent(CustomAlbumActivity.this, ShowImageActivity.class);
                intent.putCharSequenceArrayListExtra("img", selectpic);
                setResult(RESULT_OK,intent);
                finish();
                break;

        }
    }

    private List<CustomAlbumImgBean> subGroupOfImage() {
        if (mGruopMap.size() == 0) {
            return null;
        }

        List<CustomAlbumImgBean> list = new ArrayList<>();
        Iterator<Map.Entry<String, List<String>>> iterator = mGruopMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<String>> entry = iterator.next();
            String key = entry.getKey();
            List<String> value = entry.getValue();
            CustomAlbumImgBean customAlbumImgBean = new CustomAlbumImgBean();
            customAlbumImgBean.setFolderName(key);
            customAlbumImgBean.setImageCounts(value.size());
            customAlbumImgBean.setTopImagePath(value.get(0));
            list.add(customAlbumImgBean);
        }

        return list;
    }

    /**
     * 获取相册的图片
     */
    private void getImg() {
        mProgressBar.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = CustomAlbumActivity.this.getContentResolver();

                //只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

                if (mCursor == null) {
                    return;
                }

                while (mCursor.moveToNext()) {
                    //获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    //获取图片的父路径
                    String parentPathName = new File(path).getParentFile().getName();
                    if (!mGruopMap.containsKey(parentPathName)) {
                        List<String> stringList = new ArrayList<>();
                        stringList.add(path);
                        mStringList.add(parentPathName);
                        mGruopMap.put(parentPathName, stringList);
                    } else {
                        mGruopMap.get(parentPathName).add(path);
                    }
                }

                mHandler.sendEmptyMessage(SCAN_OK);
                mCursor.close();
            }
        }).start();
    }
}
