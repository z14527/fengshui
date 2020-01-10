package com.gyq.fengshui;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
