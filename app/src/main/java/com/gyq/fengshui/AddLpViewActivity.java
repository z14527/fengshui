package com.gyq.fengshui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
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
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import com.zhy.base.fileprovider.FileProvider7;

public class AddLpViewActivity extends AppCompatActivity {
    ImageView imageView = null;
    //  private ImageView mImageView;
//    Bitmap oBitmap = null;
    private TextView tvUp = null, tvDown = null,
            tvLeft = null, tvRight = null, tvInc = null,
            tvDec = null, tvRotateRight = null,
            tvRotateLeft = null, tvLpCross = null,
            tvPic = null, tvSave = null;
    private int top1 = 0, top2 =0,
            left1 = 0, left2 = 0,
            alpha1 = 35, alpha2 = 35;
    private float angle1 = 0, angle2 = 0;
    private Bitmap bitmap1 = null, bitmap2 = null, bitmap3 = null;
    private boolean bCross = false;
    public static final int REQUEST_PICK_IMAGE = 11101;
    private int w = 0, h = 0;
    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lp_view);
        String name = getIntent().getStringExtra("name");
        setTitle("风水大师 （" + name + "）");
        imageView = (ImageView) findViewById(R.id.pic);
        bitmap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.fqd);
        bitmap2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.lp);
        bitmap3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.cross);
        h = Math.max(bitmap1.getHeight(), bitmap2.getHeight());
        w = Math.max(bitmap1.getWidth(), bitmap2.getWidth());
        Bitmap cBitmap = mergeBitmap();
        imageView.setImageBitmap(cBitmap);
        tvUp = (TextView) findViewById(R.id.tv_up);
        tvUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(bCross)
                    top2 = top2 - 10;
                else
                    top1 = top1 - 10;
                Bitmap cBitmap = mergeBitmap();
                imageView.setImageBitmap(cBitmap);
            }
        });
        tvDown = (TextView) findViewById(R.id.tv_down);
        tvDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(bCross)
                    top2 = top2 + 10;
                else
                    top1 = top1 + 10;
                Bitmap cBitmap = mergeBitmap();
                imageView.setImageBitmap(cBitmap);
            }
        });
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(bCross)
                    left2 = left2 - 10;
                else
                    left1 = left1 - 10;
                Bitmap cBitmap = mergeBitmap();
                imageView.setImageBitmap(cBitmap);
            }
        });
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(bCross)
                    left2 = left2 + 10;
                else
                    left1 = left1 + 10;
                Bitmap cBitmap = mergeBitmap();
                imageView.setImageBitmap(cBitmap);
            }
        });
        tvInc = (TextView) findViewById(R.id.tv_inc);
        tvInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(bCross)
                    alpha2 = alpha2 + 5;
                else
                    alpha1 = alpha1 + 5;
                Bitmap cBitmap = mergeBitmap();
                imageView.setImageBitmap(cBitmap);
            }
        });
        tvDec = (TextView) findViewById(R.id.tv_dec);
        tvDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(bCross)
                    alpha2 = alpha2 - 5;
                else
                    alpha1 = alpha1 - 5;
                Bitmap cBitmap = mergeBitmap();
                imageView.setImageBitmap(cBitmap);
            }
        });
        tvRotateRight = (TextView) findViewById(R.id.tv_rotate_right);
        tvRotateRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(bCross)
                    angle2 = angle2 + 15/2;
                else
                    angle1 = angle1 + 15/2;
                Bitmap cBitmap = mergeBitmap();
                imageView.setImageBitmap(cBitmap);
            }
        });
        tvRotateLeft = (TextView) findViewById(R.id.tv_rotate_left);
        tvRotateLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(bCross)
                    angle2 = angle2 - 15/2;
                else
                    angle1 = angle1 - 15/2;
                Bitmap cBitmap = mergeBitmap();
                imageView.setImageBitmap(cBitmap);
            }
        });
        tvLpCross = (TextView) findViewById(R.id.tv_lp_cross);
        tvLpCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(bCross) {
                    bCross = false;
                    tvLpCross.setText("0/+");
                }
                else {
                    bCross = true;
                    tvLpCross.setText("+/O");
                }
                Bitmap cBitmap = mergeBitmap();
                imageView.setImageBitmap(cBitmap);
            }
        });
        tvPic = (TextView) findViewById(R.id.tv_pic);
        tvPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                getImage();
            }
        });
        tvSave = (TextView) findViewById(R.id.tv_save);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String name = getIntent().getStringExtra("name");
                File file;
                String filePath = Environment.getExternalStorageDirectory().getPath()+"/download/";
                String filename  = name + ".png";
                FileOutputStream out = null;
                try {
                    file = new File(filePath, filename);
                    out = new FileOutputStream(file);
                    mergeBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
                    Toast.makeText(arg0.getContext(),"保存到文件："+ name +" 成功！",Toast.LENGTH_LONG).show();
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        });
//可以使用任何ImageView支持的方式设置图片
    //   imageView.setImageResource(R.drawable.my_pic);
//or...
    //  imageView.setImageBitmap(bitmap);
    //initViews();
    //startMerge();
    }

    private Bitmap mergeBitmap() {
        Bitmap mergedBitmap = null;
        mergedBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Paint vPaint1 = new Paint();
        vPaint1.setStyle(Paint.Style.STROKE);   //空心
        vPaint1.setAlpha(alpha1);//
        Paint vPaint2 = new Paint();
        vPaint2.setStyle(Paint.Style.STROKE);   //空心
        vPaint2.setAlpha(alpha2);//
        Canvas canvas = new Canvas(mergedBitmap);
        canvas.drawBitmap(bitmap1, (w-bitmap1.getWidth())/2, (h-bitmap1.getHeight())/2, null);
        canvas.drawBitmap(rotateBitmap(bitmap2, angle1),
                left1,top1,vPaint1);
        if(bCross)
            canvas.drawBitmap(rotateBitmap(bitmap3, angle2),
                    left2, top2, vPaint2);
        return mergedBitmap;
    }
    private Bitmap rotateBitmap(Bitmap origin,
                                float angle11) {
        if (origin == null) {
            return null;
        }
        if(angle11 == 0)
            return origin;
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(angle11);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0,
                0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
       // origin.recycle();
        return newBM;
    }
    private void getImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    REQUEST_PICK_IMAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_PICK_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_IMAGE:
                    if (data != null) {
                        RealPathFromUriUtils realPathFromUriUtils = new RealPathFromUriUtils();
                        String realPathFromUri = realPathFromUriUtils.getRealPathFromUri(this, data.getData());
                        showImg(realPathFromUri);
                    } else {
                        Toast.makeText(this, "图片损坏，请重新选择", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    public void showImg(String path) {
        bitmap1 = BitmapFactory.decodeFile(path);
        Bitmap cBitmap = mergeBitmap();
        imageView.setImageBitmap(cBitmap);
    }
    public void setName(String name1){
        name = name1;
    }
}

