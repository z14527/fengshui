package com.gyq.fengshui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.gyq.fengshui.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import static java.lang.Integer.parseInt;
import static java.lang.Math.min;
//import org.apache.http.message.BasicNameValuePair;


public class PfAdapter extends BaseAdapter {

    private LayoutInflater mInflater ;
    private ArrayList<Map<String,Object>> listData ;
    private Context context ;
    private Activity activity;
    private String name = "";

    public PfAdapter(Context context,Activity activity,ArrayList<Map<String,Object>> listData)
    {
        mInflater = LayoutInflater.from(context) ;
        this.listData = listData ;
        this.context = context ;
        this.activity = activity;
    }
    public void setListData(ArrayList<Map<String,Object>> listData1){
        this.listData=listData1;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listData.size() ;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return this.listData.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    //这里arg0被定义为final是java语法规范（Cannot refer to a non-final variable arg0 inside an inner class defined in a different method）
    @Override
    public View getView(final int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder holder = null ;
        holder = new ViewHolder() ;

        arg1 = mInflater.inflate(R.layout.pf_layout_temp, null);
        //需要为每个控件指定内容，如指定textView的显示文字（这就是引用的listData作用）
        holder.textView1 = (TextView)arg1.findViewById(R.id.item_cx) ;
        holder.textView1.setText(listData.get(arg0).get("cx").toString()) ;
        holder.textView2 = (TextView)arg1.findViewById(R.id.item_df) ;
        holder.textView2.setText(listData.get(arg0).get("df").toString()) ;
        holder.imageViewChakan = (ImageView) arg1.findViewById(R.id.image_view_chakan2);
        holder.imageViewChakan.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Map<String, Object> map = listData.get(arg0);
                String info = map.get("pf").toString();
                info = info.replaceAll("@","\n");
                new Gongju().ShowMsg(v.getContext(),
                        "评分具体情况",info,false,
                        -1);
            }});
        return arg1;
    }
     protected Context getApplicationContext() {
        // TODO Auto-generated method stub
        return context;
    }

    public class ViewHolder
    {
        public ImageView imageViewChakan ;
                //      public ImageView imageView6 ;
        public TextView textView1 ;
        public TextView textView2 ;
    }
}

