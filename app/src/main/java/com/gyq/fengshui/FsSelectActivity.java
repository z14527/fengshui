package com.gyq.fengshui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FsSelectActivity extends AppCompatActivity {
    private ArrayList<Map<String, Object>> listData;
    private ListView mListView = null;
    private static MyAdapter2 adapter = null;
    private DatabaseHelper mydb = null;
    private TextView tvUnSelectAll = null,tvSelectOk = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_select);
        setTitle("选择要评价的宝地（可以多选）：");
        listData = getData();
        mListView = (ListView) findViewById(R.id.FsSelectListView);
        adapter=new MyAdapter2(listData,this);
        mListView.setAdapter(adapter);
        tvUnSelectAll = (TextView)findViewById(R.id.tv_unselect_all);
        tvUnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                adapter.UnCheckAll();
                adapter.notifyDataSetChanged();
            }
        });
        tvSelectOk = (TextView)findViewById(R.id.tv_select_ok);
        tvSelectOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                adapter.checkOk();
           //     adapter.notifyDataSetChanged();
            }
        });

    }
    private ArrayList<Map<String, Object>> getData() {
        //图片资源
        mydb = new DatabaseHelper(FsSelectActivity.this);
        String[] fn = {"name","cx","sj"};
        Cursor c1 = null;
        c1 = mydb.select(fn);
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        while (c1.moveToNext()) {
            //String pt = "初始值：山=" + c1.getString(0) + "；水=" + c1.getString(1);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", c1.getString(0));
            map.put("cx", c1.getString(1));
            map.put("sj", c1.getString(2));
            list.add(map);
        }
        Collections.reverse(list);
        return list;
    }
    public static void ycChoice(){
        if(adapter != null){
            adapter.ycChoice();
        }
    }
    public class MyAdapter2 extends BaseAdapter {
        private ArrayList<Map<String,Object>> listData,listData2 ;
        private Context context;
        private Map<Integer,Boolean> map=new HashMap<>();
        public MyAdapter2(ArrayList<Map<String,Object>> listData,Context context){
            this.listData=listData;
            this.listData2=new ArrayList<Map<String,Object>>();
            for(int i=0;i<listData.size();i++)
                listData2.add(listData.get(i));
            this.context=context;
        }
        public void UnCheckAll(){
            map.clear();
            listData.clear();
            for(int i=0;i<listData2.size();i++)
                listData.add(listData2.get(i));
           }
        public void checkOk(){
            String info = "你选择了：\n";
            for(int i=0;i<listData.size();i++) {
                if (map.containsKey(i))
                    info += listData.get(i).get("name")+"\n";
            }
            new Gongju().ShowMsg(context,"",info,
                    true,3);
         }
        public void ycChoice(){
            for (int i = listData.size() - 1; i > -1; i--) {
                if (!map.containsKey(i)) {
                    listData.remove(i);
                }
            }
            notifyDataSetChanged();
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
                view=View.inflate(context,R.layout.fs_select_layout_temp,null);
            }else {
                view=convertView;//复用历史缓存对象
            }
            //单选按钮的文字
            TextView nameTv=(TextView)view.findViewById(R.id.tv_fs_name);
            nameTv.setText(listData.get(position).get("name").toString());
            TextView cxTv=(TextView)view.findViewById(R.id.tv_fs_cx);
            cxTv.setText(listData.get(position).get("cx").toString());
            TextView sjTv=(TextView)view.findViewById(R.id.tv_fs_sj);
            sjTv.setText(listData.get(position).get("sj").toString());
            //单选按钮
            final CheckBox checkBox=(CheckBox)view.findViewById(R.id.rb_check_button);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()){
                        if(map.containsKey(position))
                            map.remove(position);
                        map.put(position,true);

                    }else {
                        map.remove(position);
                    }
                }
            });
            if(map!=null&&map.containsKey(position)){
                checkBox.setChecked(true);
            }else {
                checkBox.setChecked(false);
            }
            return view;
        }
    }

}
