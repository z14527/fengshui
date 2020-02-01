package com.gyq.fengshui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LauncherActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import android.telephony.TelephonyManager;
import static android.content.Context.TELEPHONY_SERVICE;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.Observable;

import io.reactivex.functions.Action;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] permissionsGroup=new String[]{Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_NUMBERS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE
        };
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(permissionsGroup)
                .subscribe(permission -> {
                    Toast.makeText(this,"权限名称:"+permission.name+",申请结果:"+permission.granted, LENGTH_LONG).show();
                });
        final ImageButton btxbd = (ImageButton) findViewById(R.id.imgBtnXBD);
        btxbd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                makeText(MainActivity.this,getString(R.string.main_activity_button2_name), LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,Input_Activity.class);
                startActivity(intent);
            }
        });
     //   btxbd.setVisibility(View.INVISIBLE);
        final ImageButton btlsbd = (ImageButton) findViewById(R.id.imgBtnLSBD);
        btlsbd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                makeText(MainActivity.this,getString(R.string.main_activity_button3_name), LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,LishiActivity.class);
                startActivity(intent);
            }
        });
   //     btlsbd.setVisibility(View.INVISIBLE);
        ImageButton btdl = (ImageButton) findViewById(R.id.imgBtnDL);
        btdl.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this,AddLpViewActivity.class);
                                        startActivity(intent);
                                    }
                                });
        if(notHasLightSensorManager()) {
           btxbd.setEnabled(false);
           btlsbd.setEnabled(false);
        }
    }
    public Boolean notHasLightSensorManager() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor8 = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); //光
        if (null == sensor8) {
            return true;
        } else {
            return false;
        }
    }
}
class Gongju {
    public String getNumber(Activity at) {
        TelephonyManager tm = (TelephonyManager) at.getSystemService(TELEPHONY_SERVICE);
        String phoneNumber1 = "";
        try {
            phoneNumber1 = tm.getLine1Number();
        }catch(SecurityException e1){
            e1.printStackTrace();
        }
     //   Toast.makeText(at.getApplicationContext(), "手机号码:" + phoneNumber1, Toast.LENGTH_SHORT).show();
        if (phoneNumber1.equals(""))
            phoneNumber1 = "13683559392";
        return phoneNumber1;
    }
    public void ShowMsg(Context context,String title, String msg,boolean cancel,int type){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);    //设置对话框标题
        builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
        final TextView tv = new TextView(context);
        //tv.setBackgroundResource(R.drawable.fengmian);
        tv.setTextSize(25);
        tv.setTextColor(Color.RED);
        tv.setText(msg);
        tv.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
        builder.setView(tv);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(type==0)
                    FsChartActivity.shan2shui();
                if(type==1)
                    FsChartActivity.reDraw();
                if(type==2)
                    FsChartActivity.saveData();
            }
        });
        if(cancel)
            builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
        AlertDialog dialog = builder.create();  //创建对话框
        dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
        dialog.show();
    }
}
class RealPathFromUriUtils {
    /**
     * 根据Uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 19) { // api >= 19
            return getRealPathFromUriAboveApi19(context, uri);
        } else { // api < 19
            return getRealPathFromUriBelowAPI19(context, uri);
        }
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
}
