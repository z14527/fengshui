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
import android.content.pm.PackageManager;
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


public class LishiAdapter extends BaseAdapter {

    private LayoutInflater mInflater ;
    private ArrayList<Map<String,Object>> listData ;
    private Context context ;
    private Activity activity;
    private String name = "";
    private String dbid = "";
    private DatabaseHelper mydb = null;
    private String fs24 = "子癸丑艮寅甲卯乙辰巽巳丙午丁未坤申庚酉辛戌乾亥壬";

    public LishiAdapter(Context context,Activity activity,ArrayList<Map<String,Object>> listData)
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

        arg1 = mInflater.inflate(R.layout.lishi_layout_temp, null);
        //需要为每个控件指定内容，如指定textView的显示文字（这就是引用的listData作用）
        holder.textView1 = (TextView)arg1.findViewById(R.id.item_title) ;
        holder.textView1.setText(listData.get(arg0).get("name").toString()) ;
        holder.textView2 = (TextView)arg1.findViewById(R.id.item_content) ;
        holder.textView2.setText(listData.get(arg0).get("note").toString()) ;
        holder.textView3 = (TextView)arg1.findViewById(R.id.item_date) ;
        holder.textView3.setText(listData.get(arg0).get("ct").toString()) ;
        holder.imageViewFdj = (ImageView) arg1.findViewById(R.id.image_view_fdj);
        holder.imageViewLp = (ImageView) arg1.findViewById(R.id.image_view_lp);
        holder.imageViewCross = (ImageView) arg1.findViewById(R.id.image_view_cross);
        holder.imageViewDiejia = (ImageView) arg1.findViewById(R.id.image_view_diejia);
        holder.imageViewChakan = (ImageView) arg1.findViewById(R.id.image_view_chakan);
        holder.imageViewXiugai = (ImageView) arg1.findViewById(R.id.image_view_xiugai);
        holder.imageViewPingfen = (ImageView) arg1.findViewById(R.id.image_view_pingfen);
        holder.imageViewFdj.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO 查找
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("请输入要查找的宝地的名称的关键词：");    //设置对话框标题
                builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
                final EditText edit = new EditText(context);
                builder.setView(edit);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String key = edit.getText().toString();
                        Intent intent = new Intent(context,LishiActivity.class);
                        intent.putExtra("key",key);
                        context.startActivity(intent);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "你点了取消", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
                AlertDialog dialog = builder.create();  //创建对话框
                dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
                dialog.show();
            }});
        holder.imageViewChakan.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Map<String, Object> map = listData.get(arg0);
                dbid = map.get("id").toString();
                if(!checkid(dbid))
                    return;
                String str = "";
                str += map.get("name") + "\n";
                str += map.get("note") + "\n";
                str += map.get("ct") + "\n\n";
                new Gongju().ShowMsg(v.getContext(),
                        "宝地具体情况",
                        str + getDbValue(dbid).toString(),
                        false,
                        -1);
            }});
        holder.imageViewXiugai.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO 修改
                Map<String, Object> map = listData.get(arg0);
                dbid = map.get("id").toString();
                if(!checkid(dbid))
                    return;
                name = map.get("name").toString();
                String note = map.get("note").toString();
                Intent intent = new Intent(context,Input_Activity.class);
                intent.putExtra("id",dbid);
                intent.putExtra("name",name);
                intent.putExtra("note",note);
                context.startActivity(intent);
            }});
        holder.imageViewPingfen.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO 评分
                String shan = "";
                String shui = "";
                Map<String, Object> map = listData.get(arg0);
                dbid = map.get("id").toString();
                if(!checkid(dbid))
                    return;
                name = map.get("name").toString();
                mydb = new DatabaseHelper(context);
                String[] fn={"shan01","shan02","shan03","shan04","shan05","shan06",
                        "shan07","shan08","shan09","shan10","shan11","shan12",
                        "shan13","shan14","shan15","shan16","shan17","shan18",
                        "shan19","shan20","shan21","shan22","shan23","shan24",
                        "shui01","shui02","shui03","shui04","shui05","shui06",
                        "shui07","shui08","shui09","shui10","shui11","shui12",
                        "shui13","shui14","shui15","shui16","shui17","shui18",
                        "shui19","shui20","shui21","shui22","shui23","shui24"
                };
                Cursor c1 = mydb.select(dbid,fn);
                while(c1.moveToNext()) {
                    for(int i=0;i<24;i++) {
                        shan = shan + c1.getString(i) + ",";
                        shui = shui + c1.getString(i + 24) + ",";
                    }
                    break;
                }
                shan = shan.substring(0,shan.length()-1);
                shui = shui.substring(0,shui.length()-1);
                try {
                    SystemHelper.startLocalApp(getApplicationContext(),"com.termux");
                    int i = 0;
                    while(!SystemHelper.isHttpOpen(MainActivity.r_ip_1,Integer.parseInt(MainActivity.r_port_1))
                            && i<20) {
                        Thread.sleep(100);
                        i++;
                    }
                    Thread.sleep(100);
                    /**最后将被挤压到后台的本应用重新置顶到最前端
                     * 当自己的应用在后台时，将它切换到前台来*/
                    SystemHelper.setTopApp(getApplicationContext());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context,PfActivity.class);
                intent.putExtra("shan",shan);
                intent.putExtra("shui",shui);
                intent.putExtra("name",name);
                getApplicationContext().startActivity(intent);
            }
        });
        holder.imageViewLp.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO 罗盘
                Map<String, Object> map = listData.get(arg0);
                dbid = map.get("id").toString();
                if(!checkid(dbid))
                    return;
                name = map.get("name").toString();
                FsChartActivity.dbid = dbid;
                FsChartActivity.name = name;
                FsChartActivity.context = v.getContext();
                Intent intent = new Intent(context,FsChartActivity.class);
                initMap(dbid);
                v.getContext().startActivity(intent);
            }
        });
        holder.imageViewCross.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO 罗盘多层叠加
                Intent intent = new Intent(context,AddLpViewActivity.class);
                Map<String, Object> map = listData.get(arg0);
                dbid = map.get("id").toString();
                if(!checkid(dbid))
                    return;
                name = map.get("name").toString();
                intent.putExtra("name",name);
                v.getContext().startActivity(intent);
            }
        });
        holder.imageViewDiejia.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO 罗盘叠加查看
                Map<String, Object> map = listData.get(arg0);
                dbid = map.get("id").toString();
                if(!checkid(dbid))
                    return;
                name = map.get("name").toString();
                String filePath = Environment.getExternalStorageDirectory().getPath()+"/download/";
                String filename  = name + ".png";
                File file = new File(filePath+filename);
                if(!file.exists()){
                    Toast.makeText(v.getContext(),"本地的罗盘叠加图不存在！",Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(context,DiejiaViewActivity.class);
                intent.putExtra("path",filePath+filename);
                intent.putExtra("name",name);
                v.getContext().startActivity(intent);
            }
        });
       return arg1;
    }
    private String getDbValue(String dbid)
    {
        mydb =new DatabaseHelper(context);
        String[] fn = new String[48];
        for(int i=0;i<24;i++) {
            int k = i + 1;
            String shan1 = "shan";
            if (k < 10)
                shan1 = shan1 + "0" + k;
            else
                shan1 = shan1 + k;
            String shui1 = "shui";
            if (k < 10)
                shui1 = shui1 + "0" + k;
            else
                shui1 = shui1 + k;
            fn[i] = shan1;
            fn[i+24] = shui1;
        }
        //showMsg("刷新getData2 step 2 {"+shan+","+shui+"}");
        Cursor c1 = mydb.select(dbid,fn);
        //showMsg("刷新getData2 step 3");
        while(c1.moveToNext()) {
            String pt = "";
            for(int i=0;i<24;i++)
                pt += fs24.substring(i,i+1)+": 山=" + c1.getString(i) + "；水=" + c1.getString(i+24)+"\n";
            return pt;
        }
        return "";
    }
    private boolean checkid(String dbid)
    {
        mydb =new DatabaseHelper(context);
        String[] fn = {"shan01"};
        Cursor c1 = mydb.select(dbid,fn);
        //showMsg("刷新getData2 step 3");
        if(c1.getCount()<=0)
            return false;
        return true;
    }
    private void initMap(String dbid){
        mydb =new DatabaseHelper(context);
        String[] fn = new String[48];
        for(int i=0;i<24;i++) {
            int k = i + 1;
            String shan1 = "shan";
            if (k < 10)
                shan1 = shan1 + "0" + k;
            else
                shan1 = shan1 + k;
            String shui1 = "shui";
            if (k < 10)
                shui1 = shui1 + "0" + k;
            else
                shui1 = shui1 + k;
            fn[i] = shan1;
            fn[i+24] = shui1;
        }
        //showMsg("刷新getData2 step 2 {"+shan+","+shui+"}");
        Cursor c1 = mydb.select(dbid,fn);
        //showMsg("刷新getData2 step 3");
        while(c1.moveToNext()) {
            for(int i=0;i<24;i++) {
                String fs1 = fs24.substring(i, i + 1);
                int r1 = 0;
                int r2 = 0;
                try{
                    r1 = Integer.parseInt(c1.getString(i));
                    r2 = Integer.parseInt(c1.getString(i + 24));
                }catch (Exception e1){
                    e1.printStackTrace();
                }
                FsChartActivity.ShanMap.put(fs1,r1);
                FsChartActivity.ShuiMap.put(fs1,r2);
            }
            return;
        }
    }


    protected Context getApplicationContext() {
        // TODO Auto-generated method stub
        return context;
    }

    public class ViewHolder
    {
        public ImageView imageViewFdj ;
        public ImageView imageViewLp ;
        public ImageView imageViewCross ;
        public ImageView imageViewDiejia;
        public ImageView imageViewChakan ;
        public ImageView imageViewXiugai ;
        public ImageView imageViewPingfen ;


  //      public ImageView imageView6 ;
        public TextView textView1 ;
        public TextView textView2 ;
        public TextView textView3 ;
    }


}

