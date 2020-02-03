package com.gyq.fengshui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class DiejiaViewActivity extends AppCompatActivity {

    ImageView imageView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diejia_view);
        imageView = (ImageView) findViewById(R.id.pic2);
        if(getIntent()==null) {
            return;
        }
        String path = getIntent().getStringExtra("path");
        String name = getIntent().getStringExtra("name");
        setTitle("风水大师 （" + name + "）");
        Bitmap cBitmap = BitmapFactory.decodeFile(path);
        imageView.setImageBitmap(cBitmap);
    }
}
