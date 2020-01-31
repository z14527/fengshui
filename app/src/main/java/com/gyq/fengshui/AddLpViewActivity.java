package com.gyq.fengshui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
public class AddLpViewActivity extends AppCompatActivity {
    ImageView imageView = null;
    //  private ImageView mImageView;
//    Bitmap oBitmap = null;
    private TextView tvUp = null, tvDown = null,
            tvLeft = null,tvRight = null,tvInc = null,
            tvDec = null;
    private int top = 0,left = 0,alpha = 35;
    private Bitmap bitmap1 = null ,bitmap2 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lp_view);
        imageView = (ImageView)findViewById(R.id.pic);
        bitmap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.fqd);
        bitmap2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.lp);
        Bitmap cBitmap = mergeBitmap(bitmap1, bitmap2);
        imageView.setImageBitmap(cBitmap);
        tvUp = (TextView)findViewById(R.id.tv_up);
        tvUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                top = top - 10;
                Bitmap cBitmap = mergeBitmap(bitmap1, bitmap2);
                imageView.setImageBitmap(cBitmap);
            }
        });
        tvDown = (TextView)findViewById(R.id.tv_down);
        tvDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                top = top + 10;
                Bitmap cBitmap = mergeBitmap(bitmap1, bitmap2);
                imageView.setImageBitmap(cBitmap);
            }
        });
        tvLeft = (TextView)findViewById(R.id.tv_left);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                left = left - 10;
                Bitmap cBitmap = mergeBitmap(bitmap1, bitmap2);
                imageView.setImageBitmap(cBitmap);
            }
        });
        tvRight = (TextView)findViewById(R.id.tv_right);
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                left = left + 10;
                Bitmap cBitmap = mergeBitmap(bitmap1, bitmap2);
                imageView.setImageBitmap(cBitmap);
            }
        });
        tvInc = (TextView)findViewById(R.id.tv_inc);
        tvInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                alpha = alpha + 5;
                Bitmap cBitmap = mergeBitmap(bitmap1, bitmap2);
                imageView.setImageBitmap(cBitmap);
            }
        });
        tvDec = (TextView)findViewById(R.id.tv_dec);
        tvDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                alpha = alpha - 5;
                Bitmap cBitmap = mergeBitmap(bitmap1, bitmap2);
                imageView.setImageBitmap(cBitmap);
            }
        });

//可以使用任何ImageView支持的方式设置图片
        //   imageView.setImageResource(R.drawable.my_pic);
//or...
        //  imageView.setImageBitmap(bitmap);
        //initViews();
        //startMerge();
    }
    private Bitmap mergeBitmap(Bitmap bitmap1, Bitmap bitmap2) {
        Bitmap mergedBitmap = null;
        int w, h = 0;
        h = Math.max(bitmap1.getHeight(),bitmap2.getHeight());
        w = Math.max(bitmap1.getWidth(), bitmap2.getWidth());
        mergedBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Paint vPaint = new Paint();
        vPaint .setStyle( Paint.Style.STROKE );   //空心
        vPaint .setAlpha( alpha );   //
        Canvas canvas = new Canvas(mergedBitmap);
        canvas.drawBitmap(bitmap1, 0, 0, null);
        canvas.drawBitmap(bitmap2, left, top, vPaint);
        return mergedBitmap;
    }
}
