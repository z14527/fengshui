package com.gyq.fengshui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FsYucheActivity extends AppCompatActivity {
    private ArrayList<Map<String, Object>> listData;
    private ListView mListView = null;
    private MyAdapter3 adapter = null;
    private DatabaseHelper mydb = null;
    private String[] bdnames = null;
    private String[] bdids = null;
    private String[] bdcxs = null;
    private String[] bdsjs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_yuche);
        String names = getIntent().getStringExtra("names");
        if(names!="")
            bdnames = names.split(",");
        String ids = getIntent().getStringExtra("ids");
        if(ids!="")
            bdids = ids.split(",");
        String cxs = getIntent().getStringExtra("cxs");
        if(cxs!="")
            bdcxs = cxs.split(",");
        String sjs = getIntent().getStringExtra("sjs");
        if(sjs!="")
            bdsjs = sjs.split(",");
        setTitle("宝地预测："+names);
        new Gongju().Log("fs.txt",names+"\n"+ids+"\n"+cxs+"\n"+sjs+"\n");
        listData = getData();
        mListView = (ListView) findViewById(R.id.FsYucheListView);
        adapter=new MyAdapter3(listData,this);
        mListView.setAdapter(adapter);
    }
    private String getPf(String dbid,String cx){
        String shan = "";
        String shui = "";
        String fuzhu = "";
        mydb = new DatabaseHelper(this);
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
        String req = "http://"+MainActivity.r_ip+":"+
                MainActivity.r_port+"/?shan=" + shan +
                "&shui=" + shui;
        new Gongju().Log("fs.txt",req+":\n");
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String info = "";
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
            while ((line = reader.readLine()) != null) {
                info += line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        } catch (ProtocolException e) {
            e.printStackTrace();
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
            if (connection != null) {//关闭连接
                connection.disconnect();
            }
        }
        if(info.length()>10){
            info = info.replaceAll("^.*?BODY>","");
            info = info.replaceAll("</BODY>","");
            //     new Gongju().Log("fs.txt","URL 返回:\n"+info+":\n");
            String[] fs = info.split("#");
            //Toast.makeText(this,"fs长度="+fs.length,Toast.LENGTH_LONG).show();
            for(String fs1:fs){
                //Toast.makeText(this,fs1,Toast.LENGTH_LONG).show();
                String[] fs2 = fs1.split("/");
                if(fs2.length>1) {
                    //  Toast.makeText(this,fs2[0],Toast.LENGTH_LONG).show();
                    //  Toast.makeText(this,fs2[1],Toast.LENGTH_LONG).show();
                    new Gongju().Log("fs.txt","fs2[0]="+fs2[0]+"\n");
                    if(fs2[0].replaceAll(" ","").contains(cx)){
                        new Gongju().Log("fs.txt","cx="+cx+"\n");
                        new Gongju().Log("fs.txt","fs2[1]="+fs2[1]+"\n");
                        String[] pf = fs2[1].split("@");
                        for(String pf1:pf){
                            new Gongju().Log("fs.txt",pf1+"\n");
                            if(!pf1.contains("=") && pf1.contains("辅")){
                                new Gongju().Log("fs.txt","pf1="+pf1+"\n");
                                fuzhu += pf1.replaceAll(" .*","")+"@";
                            }
                        }
                    }
                }
            }
        }
        if(fuzhu.length()>0)
            fuzhu = fuzhu.replaceAll("@$","");
        new Gongju().Log("fs.txt",fuzhu+"\n");
        return fuzhu;
    }
    private ArrayList<Map<String, Object>> getData() {
        if(bdids == null)
            return null;
        if(bdids.length<1)
            return null;
        String[] fuzhus = new String[bdids.length];
        for(int i=0;i<bdids.length;i++){
            new Gongju().Log("fs.txt","i="+i+":\n");
            String bdid = bdids[i];
            String cx = bdcxs[i];
            new Gongju().Log("fs.txt",bdid+"  "+cx+":\n");
            fuzhus[i] = getPf(bdid,cx);
            new Gongju().Log("fs.txt",fuzhus[i]+"\n");
        }
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int fy = year - 50;
        int ty = year + 50;
        String[] tian = {"甲","乙","丙","丁","戊","己","庚","辛","壬","癸"};
        String[] di = {"子","丑","寅","卯","辰","巳","午","未","申","酉","戌","亥"};
        for(int i = fy; i <= ty; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            int k1 = (i-4)%tian.length;
            int k2 = (i-4)%di.length;
            String nongli = tian[k1]+di[k2];
            map.put("gongli", i+"");
            map.put("nongli", nongli);
            int zy = 0;
            for(int j=0;j<fuzhus.length;j++) {
                String fuzhu = fuzhus[j];
                String[] fzs = fuzhu.split("@");
                for(int k=0;k<fzs.length;k++){
                    if(nongli.contains(fzs[k]))
                        zy++;
                }
            }
            map.put("fs1", ""+zy);
            map.put("fs2", "0");
            map.put("fs3", "0");
            list.add(map);
        }
        Collections.reverse(list);
        return list;
    }

    public class MyAdapter3 extends BaseAdapter {
        private ArrayList<Map<String,Object>> listData;
        private Context context;
        public MyAdapter3(ArrayList<Map<String,Object>> listData,Context context){
            this.listData=listData;
            this.context=context;
        }
        @Override
        public int getCount() {
            //return返回的是int类型，也就是页面要显示的数量。
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return this.listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView==null){
                //通过一个打气筒 inflate 可以把一个布局转换成一个view对象
                view=View.inflate(context,R.layout.fs_yuche_layout_temp,null);
            }else {
                view=convertView;//复用历史缓存对象
            }
            //单选按钮的文字
            TextView glTv=(TextView)view.findViewById(R.id.tv_fs_gl);
            glTv.setText(listData.get(position).get("gongli").toString());
            TextView nlTv=(TextView)view.findViewById(R.id.tv_fs_nl);
            nlTv.setText(listData.get(position).get("nongli").toString());
            TextView fs1Tv=(TextView)view.findViewById(R.id.tv_fs_fs1);
            fs1Tv.setText(listData.get(position).get("fs1").toString());
            TextView fs2Tv=(TextView)view.findViewById(R.id.tv_fs_fs2);
            fs2Tv.setText(listData.get(position).get("fs2").toString());
            TextView fs3Tv=(TextView)view.findViewById(R.id.tv_fs_fs3);
            fs3Tv.setText(listData.get(position).get("fs3").toString());
            return view;
        }
    }

}
