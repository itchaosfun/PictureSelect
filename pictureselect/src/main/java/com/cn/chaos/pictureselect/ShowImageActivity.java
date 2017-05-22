package com.cn.chaos.pictureselect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ShowImageActivity extends AppCompatActivity {

    private TextView takephoto;
    private TextView select;
    private String   mFileName;
    private Uri      mUri;
    public static final int TAKE_PHOTO = 100;
    public static final int TO_ALBUM = 101;
    private GridView         mGridView;
    private ShowImageAdapter adapter;
    private File             mOutputImg;
    private ArrayList<CharSequence> mCharSequenceArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        init();
    }

    private void init() {
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        takephoto = (TextView) findViewById(R.id.takephoto);
        select = (TextView) findViewById(R.id.select);
        mGridView = (GridView) findViewById(R.id.gridview);
        setListener();
    }

    private void setListener() {
        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takephoto();
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(ShowImageActivity.this, CustomAlbumActivity.class),TO_ALBUM);
            }
        });
    }

    /**
     * 拍照
     */
    private void takephoto() {
        setFile();

        //将file文件转化为uri,启动相机
        mUri = Uri.fromFile(mOutputImg);
        //照相
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        //指定图片输出地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);

        startActivityForResult(intent, TAKE_PHOTO);

    }

    private void setFile() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);

        Date date = new Date(System.currentTimeMillis());
        mFileName = sdf.format(date);

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);


        mOutputImg = new File(path, mFileName + ".jpg");

        if (mOutputImg.exists()) {
            mOutputImg.delete();
        }

        try {
            mOutputImg.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case TAKE_PHOTO:

                mCharSequenceArrayList.add(mOutputImg.getAbsolutePath());
                setGridviewAdapter(mCharSequenceArrayList);
                //将刚拍照的相片在相册中显示
                Uri localUri = Uri.fromFile(new File(mOutputImg.getAbsolutePath()));
                Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
                sendBroadcast(localIntent);

                break;
            case TO_ALBUM:
                ArrayList<CharSequence> charSequenceArrayList = data.getCharSequenceArrayListExtra("img");
                if (charSequenceArrayList == null || charSequenceArrayList.size() == 0) {
                    return;
                }

                mCharSequenceArrayList.addAll(charSequenceArrayList);
                setGridviewAdapter(mCharSequenceArrayList);
                break;

        }
    }

    private void setGridviewAdapter(ArrayList<CharSequence> path) {

        if (adapter == null) {
            adapter = new ShowImageAdapter(path, ShowImageActivity.this);
            mGridView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
