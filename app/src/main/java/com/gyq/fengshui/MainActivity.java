package com.gyq.fengshui;

import android.Manifest;
import android.app.Activity;
import android.app.LauncherActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.provider.ContactsContract;
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
//        Permission permission = new Permission(permissionString, granted, shouldShowRequestPermissionRationale);
//        when(rxPermissions.requestEach(permissionString)).thenReturn(Observable.just(permission));
//        // test
      //  rxPermissions.requestEach(permissionString).test().assertNoErrors().assertValue(permission);

 //       RxPermissions rxPermissions = new RxPermissions(this);
//        rxPermissions
//                .request(Manifest.permission.CAMERA)
//                .subscribe(new Consumer<Permission>() {
//                               @Override
//                               public void accept(Permission permission) {
//                              //     Log.i(TAG, "Permission result " + permission);
//                                   if (permission.granted) {
//                                             } else if (permission.shouldShowRequestPermissionRationale) {
//                                       // Denied permission without ask never again
//                                       Toast.makeText(MainActivity.this,
//                                               "Denied permission without ask never again",
//                                               Toast.LENGTH_SHORT).show();
//                                   } else {
//                                       // Denied permission with ask never again
//                                       // Need to go to the settings
//                                       Toast.makeText(MainActivity.this,
//                                               "Permission denied, can't enable the camera",
//                                               Toast.LENGTH_SHORT).show();
//                                   }
//                               }
//                           },
//                new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable t) {
//              //          Log.e(TAG, "onError", t);
//                    }
//                },
//                new Action() {
//                    @Override
//                    public void run() {
//              //          Log.i(TAG, "OnComplete");
//                    }
//                });
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
                                    }
                                });
//                String phoneData = "";
//                ContentResolver contentResolver = getContentResolver();
//// 获得所有的联系人
//                Cursor cursor = contentResolver.query(
//                        ContactsContract.Contacts.CONTENT_URI, null, null,
//                        null, null);
//// 循环遍历
//                if (cursor.moveToFirst()) {
//                    int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
//                    int displayNameColumn = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//                    do {
//// 获得联系人的ID号
//                        String contactId = cursor.getString(idColumn);
//// 获得联系人姓名
//                        String disPlayName = cursor.getString(displayNameColumn);
//// 查看该联系人有多少个电话号码。如果没有这返回值为0
//                        int phoneCount = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
////在联系人数量不为空的情况下执行
//                        if (phoneCount > 0) {
//// 获得联系人的电话号码列表
//                            Cursor phonesCursor = getContentResolver()
//                                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                                            null,
//                                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID  + " = " + contactId, null,
//                                            null);
//                            if (phonesCursor.moveToFirst()) {
//                                do {
//// 遍历所有的电话号码
//                                    String phoneNumber = phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                                    Toast.makeText(MainActivity.this,
//                                            "联系人姓名：" + disPlayName+"\n联系人电话：" + phoneNumber,
//                                            Toast.LENGTH_LONG).show();
//                                } while (phonesCursor.moveToNext());
//                            }
//                        }
//                    } while (cursor.moveToNext());
//                }
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
