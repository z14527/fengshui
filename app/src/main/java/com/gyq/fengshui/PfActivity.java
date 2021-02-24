package com.gyq.fengshui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Entity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.DateFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.TELEPHONY_SERVICE;

public class PfActivity extends AppCompatActivity {
    private ListView lv;
    private final ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private DatabaseHelper mydb = null;
    private PfAdapter lsAdapter = null;
    private String shan = null;
    private String shui = null;
    private String req = "";
    private Context context ;
    private Activity activity;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pf);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        shan = getIntent().getStringExtra("shan");
        shui = getIntent().getStringExtra("shui");
        String name = getIntent().getStringExtra("name");
        setTitle(name+" 评分列表");
        lv = (ListView) findViewById(R.id.pf_list_view);
        req = "http://"+MainActivity.r_ip+":"+
                MainActivity.r_port+"/?shan=" + shan +
                "&shui=" + shui;
        Toast.makeText(getApplicationContext(),req,Toast.LENGTH_SHORT).show();
        context = this;
        activity = this;
        setData();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //              startActivity(new Intent(MainActivity.this, Input_Activity.class));
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lsAdapter != null) {
            setData();
      //      lsAdapter.setListData(listData);
            lsAdapter.notifyDataSetChanged();
        }
    }

    private void setData() {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            //url
            URL url = new URL(req);
            connection = (HttpURLConnection) url.openConnection();
            //设置请求方法
            connection.setRequestMethod("GET");
            //设置连接超时时间（毫秒）
            connection.setConnectTimeout(5000);
            //设置读取超时时间（毫秒）
            connection.setReadTimeout(5000);

            //返回输入流
            InputStream in = connection.getInputStream();

            //读取输入流
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            String info = "";
            listData.clear();
            while ((line = reader.readLine()) != null) {
                info += line;
            }
            info = info.replaceAll("^.*?BODY>","");
            info = info.replaceAll("</BODY>","");
            //Toast.makeText(this,info,Toast.LENGTH_LONG).show();
       //     if(info.length()>10)
       //x         return;
            String[] fs = info.split("#");
            //Toast.makeText(this,"fs长度="+fs.length,Toast.LENGTH_LONG).show();
            for(String fs1:fs){
                //Toast.makeText(this,fs1,Toast.LENGTH_LONG).show();
                String[] fs2 = fs1.split("/");
                if(fs2.length>1) {
                  //  Toast.makeText(this,fs2[0],Toast.LENGTH_LONG).show();
                  //  Toast.makeText(this,fs2[1],Toast.LENGTH_LONG).show();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("cx", fs2[0]);
                    map.put("pf", fs2[1]);
                    int p = fs2[1].indexOf("得分=");
                    if(p>0)
                        map.put("df",fs2[1].substring(p+3));
                    else
                        map.put("df","-1");
                    listData.add(map);
             //       if(listData.size()>0)
              //          return;
                }
            }
            if (lsAdapter == null && listData.size()>1) {
                lsAdapter = new PfAdapter(this,
                        this, listData);
                lv.setAdapter(lsAdapter);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            return;
        } catch (ProtocolException e) {
            e.printStackTrace();
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            return;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (connection != null) {//关闭连接
                connection.disconnect();
            }
        }
    }
}

