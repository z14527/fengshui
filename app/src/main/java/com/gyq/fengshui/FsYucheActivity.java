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

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_yuche);
        setTitle("宝地预测：");
        listData = getData();
        mListView = (ListView) findViewById(R.id.FsYucheListView);
        adapter=new MyAdapter3(listData,this);
        mListView.setAdapter(adapter);
    }
    private ArrayList<Map<String, Object>> getData() {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int fy = year - 50;
        int ty = year + 50;
        String[] tian = {"甲","乙","丙","丁","戊","己","庚","辛","壬","癸"};
        String[] di = {"子","丑","寅","卯","辰","巳","午","未","申","酉","戌","亥"};
        for(int i = fy; i <= ty; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            int k1 = (i-2)%tian.length;
            int k2 = (i-2)%di.length;
            String nongli = tian[k1]+di[k2];
            map.put("gongli", i+"");
            map.put("nongli", nongli);
            map.put("fs1", "0");
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
