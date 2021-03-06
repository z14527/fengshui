package com.gyq.fengshui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private String[] bdcx2s = null;
    private String[] bdsjs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_yuche);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String names = getIntent().getStringExtra("names");
        if(names!="")
            bdnames = names.split(",");
        String ids = getIntent().getStringExtra("ids");
        if(ids!="")
            bdids = ids.split(",");
        String cxs = getIntent().getStringExtra("cxs");
        if(cxs!="")
            bdcxs = cxs.split(",");
        String cx2s = getIntent().getStringExtra("cx2s");
        if(cx2s!="")
            bdcx2s = cx2s.split(",");
        String sjs = getIntent().getStringExtra("sjs");
        if(sjs!="")
            bdsjs = sjs.split(",");
        setTitle("宝地预测："+names);
        new Gongju().Log("fs.txt",names+"\n"+ids+"\n"+cxs+"\n"+cx2s+"\n"+sjs+"\n");
        listData = getData();
        mListView = (ListView) findViewById(R.id.FsYucheListView);
        adapter=new MyAdapter3(listData,this);
        mListView.setAdapter(adapter);
        mListView.setSelection(mListView.getCount()/2-1);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                Map<String, Object> map = listData.get(arg2);
                String bdn = map.get("fn").toString();
                if(bdn.contains("-1") || bdn.contains("-2")) {
                    String info = map.get("info").toString();
                    info = info.replaceAll("@", "\n");
                    new Gongju().ShowMsg(arg1.getContext(),
                            "评分具体情况（" + bdn + "）", info, false,
                            -1);
                }

            }
        });


    }
    private ArrayList<String> getPf(String dbid,String cx,String cx2){
        ArrayList<String> pfInfo = new ArrayList<String>();
        String shan = "";
        String shui = "";
        String fuzhu = "";
        String tan = "";
        String ju = "";
        String wu = "";
        String detail0 = "";
        String detail1 = "";
        String detail2 = "";
        String detail3 = "";
        String detail4 = "";
        String detail5 = "";
        String detail6 = "";
        String detail7 = "";
        String detail8 = "";
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
            boolean findfuzhu = false;
            for(String fs1:fs){
                //Toast.makeText(this,fs1,Toast.LENGTH_LONG).show();
                String[] fs2 = fs1.split("/");
                if(fs2.length>1) {
                    //  Toast.makeText(this,fs2[0],Toast.LENGTH_LONG).show();
                    //  Toast.makeText(this,fs2[1],Toast.LENGTH_LONG).show();
                    new Gongju().Log("fs.txt","fs2[0]="+fs2[0]+"\n");
                    if(fs2[0].replaceAll(" ","").contains(cx2)){
                        new Gongju().Log("fs.txt","cx2="+cx2+"\n");
                        new Gongju().Log("fs.txt","fs2[1]="+fs2[1]+"\n");
                        detail0 = fs2[1];
                        String[] pf = fs2[1].split("@");
                        for(String pf1:pf){
                            new Gongju().Log("fs.txt",pf1+"\n");
                            if(!pf1.contains("=") && pf1.contains("辅")){
                                new Gongju().Log("fs.txt","pf1="+pf1+"\n");
                                fuzhu += pf1.replaceAll(" .*","")+"@";
                                findfuzhu = true;
                            }
                        }
                    }
                }
            }
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
                        detail0 = fs2[1];
                        String[] pf = fs2[1].split("@");
                        //  boolean findfuzhu = false;
                        for(String pf1:pf){
                            if(findfuzhu) {
                                if (pf1.contains("贪*"))
                                    tan += pf1.replaceAll(".*=", "")
                                            .replaceAll("", "") + "@";
                                if (pf1.contains("巨-"))
                                    ju += pf1.replaceAll(".*=", "")
                                            .replaceAll("", "") + "@";
                                if (pf1.contains("武-"))
                                    wu += pf1.replaceAll(".*=", "")
                                            .replaceAll("", "") + "@";
                            }
                            if(!pf1.contains("-") && !pf1.contains("sum")) {
                                if (pf1.contains("=") && pf1.contains("贪")) {
                                    detail1 = pf1.replaceAll(".*=", "");
                                }
                                if (pf1.contains("=") && pf1.contains("巨")) {
                                    detail2 = pf1.replaceAll(".*=", "");
                                }
                                if (pf1.contains("=") && pf1.contains("禄")) {
                                    detail3 = pf1.replaceAll(".*=", "");
                                }
                                if (pf1.contains("=") && pf1.contains("文")) {
                                    detail4 = pf1.replaceAll(".*=", "");
                                }
                                if (pf1.contains("=") && pf1.contains("廉")) {
                                    detail5 = pf1.replaceAll(".*=", "");
                                }
                                if (pf1.contains("=") && pf1.contains("武")) {
                                    detail6 = pf1.replaceAll(".*=", "");
                                }
                                if (pf1.contains("=") && pf1.contains("破")) {
                                    detail7 = pf1.replaceAll(".*=", "");
                                }
                                if (pf1.contains("=") && pf1.contains("辅")) {
                                    detail8 = pf1.replaceAll(".*=", "");
                                }
                            }
                        }
                    }
                }
            }
        }
        if(fuzhu.length()>0)
            fuzhu = fuzhu.replaceAll("@$","").replaceAll(" ","");
        if(tan.length()>0)
            tan = tan.replaceAll("@$","").replaceAll(" ","");
        if(ju.length()>0)
            ju = ju.replaceAll("@$","").replaceAll(" ","");
        if(wu.length()>0)
            wu = wu.replaceAll("@$","").replaceAll(" ","");
        new Gongju().Log("fs.txt",fuzhu+"\n");
        new Gongju().Log("fs.txt","贪="+tan+"\n");
        new Gongju().Log("fs.txt","巨="+ju+"\n");
        new Gongju().Log("fs.txt","武="+wu+"\n");
        pfInfo.add(fuzhu);
        pfInfo.add(tan);
        pfInfo.add(ju);
        pfInfo.add(wu);
        pfInfo.add(detail0);
        pfInfo.add(detail1);
        pfInfo.add(detail2);
        pfInfo.add(detail3);
        pfInfo.add(detail4);
        pfInfo.add(detail5);
        pfInfo.add(detail6);
        pfInfo.add(detail7);
        pfInfo.add(detail8);
        return pfInfo;
    }
    private ArrayList<Map<String, Object>> getData() {
        if(bdids == null)
            return null;
        if(bdids.length<1)
            return null;
        ArrayList<String>[] pfInfo = new ArrayList[bdids.length];
        for(int i=0;i<bdids.length;i++){
            new Gongju().Log("fs.txt","i="+i+":\n");
            String bdid = bdids[i];
            String cx = bdcxs[i];
            String cx2 = bdcx2s[i];
            new Gongju().Log("fs.txt",bdid+"  "+cx+"  "+cx2+":\n");
            pfInfo[i] = getPf(bdid,cx,cx2);
    //        new Gongju().Log("fs.txt",fuzhus[i]+"\n");
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
            float tan = 0;
            float ju = 0;
            float wu = 0;
            for(int j=0;j<pfInfo.length;j++) {
                String fuzhu = pfInfo[j].get(0);
                new Gongju().Log("fs.txt","i="+i+",fuzhu="+fuzhu+"\n");
                String[] fzs = fuzhu.split("@");
                for(int k=0;k<fzs.length;k++){
                    if(nongli.contains(fzs[k])) {
                        new Gongju().Log("fs.txt",
                                "j="+j+",tan="+pfInfo[j].get(1)+"\n");
                        new Gongju().Log("fs.txt",
                                "j="+j+",ju="+pfInfo[j].get(2)+"\n");
                        new Gongju().Log("fs.txt",
                                "j="+j+",wu="+pfInfo[j].get(3)+"\n");
                        zy++;
                    }
                 }
                tan += Float.valueOf(pfInfo[j].get(1));
                ju += Float.valueOf(pfInfo[j].get(2));
                wu += Float.valueOf(pfInfo[j].get(3));
            }
            map.put("fn", ""+zy);
            map.put("fs1", ""+Math.round(tan));
            map.put("fs2", ""+Math.round(ju));
            map.put("fs3", ""+Math.round(wu));
            map.put("sum", ""+Math.round(tan+ju+wu));
            map.put("cx2","");
            map.put("info","");
            list.add(map);
            for(int j=0;j<pfInfo.length;j++) {
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("gongli", i+"");
                map2.put("nongli", nongli);
                map2.put("fn", bdnames[j] + "-1");
                map2.put("fs1", pfInfo[j].get(5));
                map2.put("fs2", pfInfo[j].get(6));
                map2.put("fs3", pfInfo[j].get(7));
                map2.put("sum", pfInfo[j].get(8));
                map2.put("cx2",bdcx2s[j]);
                map2.put("info",pfInfo[j].get(4));
                list.add(map2);
                Map<String, Object> map3 = new HashMap<String, Object>();
                map3.put("gongli", i+"");
                map3.put("nongli", nongli);
                map3.put("fn", bdnames[j] + "-2");
                map3.put("fs1", pfInfo[j].get(9));
                map3.put("fs2", pfInfo[j].get(10));
                map3.put("fs3", pfInfo[j].get(11));
                map3.put("sum", pfInfo[j].get(12));
                map3.put("cx2",bdcx2s[j]);
                map3.put("info",pfInfo[j].get(4));
                list.add(map3);
            }
//            new Gongju().Log("fs.txt",
//                    "gongli="+i+"\n");
//            new Gongju().Log("fs.txt",
//                    "nongli="+nongli+"\n");
//            new Gongju().Log("fs.txt",
//                    "i="+i+",tan="+Math.round(tan)+"\n");
//            new Gongju().Log("fs.txt",
//                    "i="+i+",ju="+Math.round(ju)+"\n");
//            new Gongju().Log("fs.txt",
//                    "i="+i+",wu="+Math.round(wu)+"\n");
//            new Gongju().Log("fs.txt",
//                    "i="+i+",sum="+Math.round(tan+ju+wu)+"\n");
        }
  //      Collections.reverse(list);
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
            TextView fnTv=(TextView)view.findViewById(R.id.tv_fs_fn);
            fnTv.setText(listData.get(position).get("fn").toString());
            String fn = listData.get(position).get("fn").toString();
            if(!fn.equals("0"))
                fnTv.setTextColor(Color.RED);
            else
                fnTv.setTextColor(nlTv.getCurrentTextColor());
            TextView fs1Tv=(TextView)view.findViewById(R.id.tv_fs_fs1);
            TextView fs2Tv=(TextView)view.findViewById(R.id.tv_fs_fs2);
            TextView fs3Tv=(TextView)view.findViewById(R.id.tv_fs_fs3);
            TextView fsSumTv=(TextView)view.findViewById(R.id.tv_fs_sum);
            if(!fn.contains("-1") && !fn.contains("-2")){
                fs1Tv.setTextColor(nlTv.getCurrentTextColor());
                fs2Tv.setTextColor(nlTv.getCurrentTextColor());
                fs3Tv.setTextColor(nlTv.getCurrentTextColor());
                fsSumTv.setTextColor(nlTv.getCurrentTextColor());
            }
            fs1Tv.setText(listData.get(position).get("fs1").toString());
            fs2Tv.setText(listData.get(position).get("fs2").toString());
            fs3Tv.setText(listData.get(position).get("fs3").toString());
            fsSumTv.setText(listData.get(position).get("sum").toString());
            String nongli = listData.get(position).get("nongli").toString();
            String info = listData.get(position).get("info").toString();
            if(fn.contains("-1") || fn.contains("-2")){
                glTv.setText("");
                nlTv.setText("");
                int[] pg = pinggu(nongli,info);
                if(pg[0]>0 && fn.contains("-1"))
                    fs1Tv.setTextColor(Color.RED);
                if(pg[1]>0 && fn.contains("-1"))
                    fs2Tv.setTextColor(Color.RED);
                if(pg[2]>0 && fn.contains("-1"))
                    fs3Tv.setTextColor(Color.RED);
                if(pg[3]>0 && fn.contains("-1"))
                    fsSumTv.setTextColor(Color.RED);
                if(pg[4]>0 && fn.contains("-2"))
                    fs1Tv.setTextColor(Color.RED);
                if(pg[5]>0 && fn.contains("-2"))
                    fs2Tv.setTextColor(Color.RED);
                if(pg[6]>0 && fn.contains("-2"))
                    fs3Tv.setTextColor(Color.RED);
                if(pg[7]>0 && fn.contains("-2"))
                    fsSumTv.setTextColor(Color.RED);
            }
            return view;
        }
    }
    public int[] pinggu(String nongli, String info){
        int[] ret = {0,0,0,0,0,0,0,0};
        if(info.equals(""))
            return ret;
        String fs24 = "子癸丑艮寅甲卯乙辰巽巳丙午丁未坤申庚酉辛戌乾亥壬子癸丑艮寅甲卯乙辰巽巳丙午丁未坤申庚酉辛戌乾亥壬";
        String ns = nongli+nongli;
        String ps = "贪巨禄文廉武破辅贪巨禄文廉武破辅";
        for(int i = 0;i < 8;i++){
            String fs1 = fs24.substring(i,i+1);
            String fs2 = fs24.substring(i+8,i+8+1);
            String fs3 = fs24.substring(i+16,i+16+1);
            for(int j = 0;j < 2;j++) {
                String ns1 = ns.substring(j,j+1);
                new Gongju().Log("fs.txt","i="+i+
                        " j="+j+
                        " fsi="+fs1+
                        " fsi8="+fs2+
                        " fsi16="+fs3+
                        " nsj="+ns1+"\n");
                if(fs1.equals(ns1) || fs2.equals(ns1) || fs3.equals(ns1)){
                    for(int k = 0;k < 8;k++) {
                        String ps1 = ps.substring(k,k+1);
                        if (info.contains(fs1 + " " + ps1))
                            ret[k] = 1;
                        if (info.contains(fs2 + " " + ps1))
                            ret[k] = 1;
                        if (info.contains(fs3 + " " + ps1))
                            ret[k] = 1;
                    }
                }
            }
        }
        return ret;
    }


}
