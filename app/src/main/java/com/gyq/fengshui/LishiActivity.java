package com.gyq.fengshui;

import android.Manifest;
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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.TELEPHONY_SERVICE;

public class LishiActivity extends AppCompatActivity {
    private ListView lv;
    private ArrayList<Map<String, Object>> listData;
    private DatabaseHelper mydb = null;
    private LishiAdapter lsAdapter = null;
    private String fd = null;
    private String key = null;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lishi_activity);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        lv = (ListView) findViewById(R.id.lishi_list_view);
        listData = getData();
        //Toast.makeText(getApplicationContext(), "InputActivity MyListeView dbid="+dbid, 1).show();
        if (lsAdapter == null)
            lsAdapter = new LishiAdapter(this,this,listData);
        lv.setAdapter(lsAdapter);
        ImageButton btxcz1 = (ImageButton) findViewById(R.id.imgBtnCZ1);
        btxcz1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LishiActivity.this);
                builder.setTitle("请输入要查找的宝地的名称的关键词：");    //设置对话框标题
                builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
                final EditText edit = new EditText(LishiActivity.this);
                builder.setView(edit);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        key = edit.getText().toString();
                        fd = "name";
                        if (lsAdapter != null) {
                            listData = getData();
                            lsAdapter.setListData(listData);
                            lsAdapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(LishiActivity.this, "你点了取消", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
                AlertDialog dialog = builder.create();  //创建对话框
                dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
                dialog.show();
            }
        });
        ImageButton btxcz2 = (ImageButton) findViewById(R.id.imgBtnCZ2);
        btxcz2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LishiActivity.this);
                builder.setTitle("请输入要查找的宝地的创建时间的相关信息：");    //设置对话框标题
                builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
                final EditText edit = new EditText(LishiActivity.this);
                builder.setView(edit);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        key = edit.getText().toString();
                        fd = "ct";
                        if (lsAdapter != null) {
                            listData = getData();
                            lsAdapter.setListData(listData);
                            lsAdapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(LishiActivity.this, "你点了取消", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
                AlertDialog dialog = builder.create();  //创建对话框
                dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
                dialog.show();
            }
        });
        ImageButton btxcz3 = (ImageButton) findViewById(R.id.imgBtnCZ3);
        btxcz3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key = null;
                fd = null;
                if (lsAdapter != null) {
                    listData = getData();
                    lsAdapter.setListData(listData);
                    lsAdapter.notifyDataSetChanged();
                }
            }
        });
        ImageButton btxsz = (ImageButton) findViewById(R.id.imgBtnSC);
        btxsz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upLoadDBfile();
            }
        });
        ImageButton btxxz = (ImageButton) findViewById(R.id.imgBtnXZ);
        btxxz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "xz1", Toast.LENGTH_SHORT).show();
                dwonloadDBfile();
            }
        });
    }

    private ArrayList<Map<String, Object>> getData() {
        //图片资源
        mydb = new DatabaseHelper(LishiActivity.this);
        String[] fn = {"id", "name", "note", "ct"};
        Cursor c1 = null;
        if (key != null)
            c1 = mydb.lselect(key, fd, fn);
        else
            c1 = mydb.select(fn);
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        while (c1.moveToNext()) {
            //String pt = "初始值：山=" + c1.getString(0) + "；水=" + c1.getString(1);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", R.drawable.fdj);
            map.put("id", c1.getString(0));
            map.put("name", c1.getString(1));
            map.put("note", c1.getString(2));
            map.put("ct", c1.getString(3));
            list.add(map);
        }
        Collections.reverse(list);
        return list;
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
            listData = getData();
            lsAdapter.setListData(listData);
            lsAdapter.notifyDataSetChanged();
        }
    }

    private void upLoadDBfile() {
        final String phone = new Gongju().getNumber(this);
  //      final String phone = "13683559392";
        final String dbs = getApplicationContext().getDatabasePath("fengshui.db").getAbsolutePath();
        if(MainActivity.up_url.contains("127.0.0.1")){
            String dbs2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()+"/data";
            File dbf2 = new File(dbs2);
            if(!dbf2.exists())
                dbf2.mkdir();
            String dbs3 = dbs2+"/13683559392_"+System.currentTimeMillis()+".db3";
            new Gongju().copyFile(dbs,dbs3);
            new Gongju().ToastMsg(getApplicationContext(), "成功保存到本机数据库！");
            return;
        }
        //用HttpClient发送请求，分为五步
//第一步：创建HttpClient对象
        HttpClient client = new DefaultHttpClient();//创建一个发送请求的客户端
 //       HttpPost post = new HttpPost(getString(R.string.up_url));//创建Post请求对象
        HttpPost post = new HttpPost(MainActivity.up_url);//创建Post请求对象
        //----------------------------以JSON方式请求-----------------------
        JSONObject jsonObj = new JSONObject();//创建一个json对象
        try {
            jsonObj.put("user", phone);//把name值放入json对象中
        } catch (JSONException e) {
        }

        String res = "";
        try {
            FileInputStream fin = new FileInputStream(dbs);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = Base64.encodeToString(buffer, Base64.DEFAULT);
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonObj.put("data", res);
        } catch (JSONException e) {
        }
        try {
            StringEntity s = new StringEntity(jsonObj.toString(), "utf-8");
            s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(s);
        } catch (Exception e) {
        }
//第二步：创建代表请求的对象,参数是访问的服务器地址
        try {
//第三步：执行请求，获取服务器发还的相应对象
            HttpResponse httpResponse = client.execute(post);
            InputStream inStream = httpResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
            StringBuilder strber = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
                strber.append(line + "\n");
            inStream.close();
            String ret = "保存宝地信息成功!";
            if (strber.toString().indexOf("0") >= 0)
                ret = "无法保存宝地信息，请直接导出到电脑！";
            AlertDialog.Builder builder = new AlertDialog.Builder(LishiActivity.this);
            builder.setTitle("返回信息");    //设置对话框标题
            builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
            final TextView tv = new TextView(LishiActivity.this);
            builder.setView(tv);
            tv.setText(ret);
            tv.setVerticalScrollBarEnabled(true);
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
            AlertDialog dialog = builder.create();  //创建对话框
            dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
            dialog.show();
        } catch (Exception e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void dwonloadDBfile() {
        if(MainActivity.up_url.contains("127.0.0.1")){
            String ret = "";
            try {
                String dbs2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()+"/data";
                File dbf2 = new File(dbs2);
                if(!dbf2.exists())
                    dbf2.mkdir();
                File file = new File(dbs2);
                File[] files = file.listFiles();
                if(files.length>0) {
                    for (File file2 : files) {
                        if (file2.getName().contains("db3")) {
                            if (ret.length() > 1)
                                ret = ret + "%" + file2.getName();
                            else
                                ret = file2.getName();
                        }
                    }
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
//            if(ret.length()>1)
//                return;
            final AlertDialog.Builder builder = new AlertDialog.Builder(LishiActivity.this);
            //builder.setIcon(R.drawable.ic_launcher);
            builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
            builder.setTitle("选择一个本机历史数据库");
            if (ret.length() > 1) {
                final String[] dbs = ret.split("%");
                //    设置一个下拉的列表选择项
                if (dbs.length >= 1) {
                    builder.setItems(dbs, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Gongju().ToastMsg(getApplicationContext(), "选择的历史数据库为：" + dbs[which]);
                            String db1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()+"/data/" + dbs[which];
                            String db2 = getApplicationContext().getDatabasePath("fengshui.db").getAbsolutePath();
                            new Gongju().copyFile(db1,db2);
                            new Gongju().ToastMsg(getApplicationContext(), "下载本机数据库成功！请退出重新进入历史宝地刷新数据！");
                        }
                    });
                }
            } else {
                builder.setMessage("无可下载的本机历史数据库");
            }
            AlertDialog dialog = builder.create();  //创建对话框
            dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
            dialog.show();
            return;
        }
        //用HttpClient发送请求，分为五步
        //第一步：创建HttpClient对象
        HttpClient httpCient = new DefaultHttpClient();
        //第二步：创建代表请求的对象,参数是访问的服务器地址
//        HttpGet httpGet = new HttpGet(getString(R.string.listdb_url) + "?p=" + (new Gongju().getNumber(this)));
        HttpGet httpGet = new HttpGet(MainActivity.listdb_url + "?p=" + (new Gongju().getNumber(this)));
        ArrayList<NameValuePair> headerList = new ArrayList<NameValuePair>();
        headerList.add(new BasicNameValuePair("Content-Type",
                "text/html; charset=utf-8"));
        try {
            //第三步：执行请求，获取服务器发还的相应对象
            for (int i = 0; i < headerList.size(); i++) {
                httpGet.addHeader(headerList.get(i).getName(),
                        headerList.get(i).getValue());
            }
            HttpResponse httpResponse = httpCient.execute(httpGet);
            //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                //第五步：从相应对象当中取出数据，放到entity当中
                HttpEntity entity = httpResponse.getEntity();
                String ret = EntityUtils.toString(entity, "utf-8");//将entity当中的数据转换为字符串
                //Toast.makeText(getApplicationContext(), ret, Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder builder = new AlertDialog.Builder(LishiActivity.this);
                //builder.setIcon(R.drawable.ic_launcher);
                builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
                builder.setTitle("选择一个历史数据库");
                if (ret.length() > 1) {
                    final String[] dbs = ret.split("%");
                    //    设置一个下拉的列表选择项
                    if (dbs.length >= 1) {
                        builder.setItems(dbs, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "选择的历史数据库为：" + dbs[which], Toast.LENGTH_SHORT).show();
//                                if (getDownloadFile2Cache(getString(R.string.data_url) + dbs[which],
                                if (getDownloadFile2Cache(MainActivity.data_url + dbs[which],
                                        getApplicationContext().getDatabasePath("fengshui.db").getAbsolutePath()))
                                    Toast.makeText(getApplicationContext(), "下载成功！请退出重新进入历史宝地刷新数据！", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getApplicationContext(), "下载失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    builder.setMessage("无可下载的历史数据库");
                }
                AlertDialog dialog = builder.create();  //创建对话框
                dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
                dialog.show();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public Boolean getDownloadFile2Cache(String url, String filePath) {
        Boolean ret = true;
        try {
            HttpGet httpRequest = new HttpGet(url);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
            HttpEntity entity = response.getEntity();
            BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(entity);
            InputStream is = bufferedHttpEntity.getContent();
            String fileName = null;
            fileName = url.substring(url.lastIndexOf('/') + 1);
            FileOutputStream fos = new FileOutputStream(filePath);
            byte buf[] = new byte[1024];
            int numread;
            while ((numread = is.read(buf)) != -1) {
                fos.write(buf, 0, numread);
            }
            fos.close();
            is.close();
        } catch (IOException e) {
            ret = false;
            e.printStackTrace();
        } catch (Exception e) {
            ret = false;
            e.printStackTrace();
        }
        return ret;
    }

}

