package com.cn.chaos.pictureselect;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AlbumActivity extends AppCompatActivity {

    private GridView     mGridView;
    private AlbumAdapter mAdapter;

    private TextView mSubmit;
    private List<Boolean>           mPicSelect     = new ArrayList<>();
    private ArrayList<CharSequence> mArrayList     = new ArrayList<>();
    private ArrayList<CharSequence> mCharSequences = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
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
        mArrayList = getIntent().getCharSequenceArrayListExtra("photo");

        for (int i = 0; i < mArrayList.size(); i++) {
            mPicSelect.add(false);
        }

        mAdapter = new AlbumAdapter(AlbumActivity.this, mArrayList);
        mGridView.setAdapter(mAdapter);

        mAdapter.setOnImgSelectListener(new AlbumAdapter.OnImgSelectListener() {
            @Override
            public void onImgSelect(int position, Boolean aBoolean) {
                if (mPicSelect.get(position)) {
                    mPicSelect.set(position,false);
                }else {
                    mPicSelect.set(position,true);
                }
                //选择具体的上传的图片
                selectPic();
            }
        });
    }

    private void selectPic() {
        mCharSequences.clear();
        for (int i = 0; i < mPicSelect.size(); i++) {
            if(mPicSelect.get(i)){
                mCharSequences.add(mArrayList.get(i));
            }
        }
    }

    private void initView() {
        mGridView = (GridView) findViewById(R.id.gv_photo);
        mSubmit = (TextView) findViewById(R.id.submit);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mCharSequences.size() == 0){
                    Toast.makeText(AlbumActivity.this,"请选择图片!",Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(AlbumActivity.this, ShowImageActivity.class);
                intent.putCharSequenceArrayListExtra("selectpic", mCharSequences);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
