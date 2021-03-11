package com.gyq.fengshui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView tvUnSelectAll = null,
            tvSelectOk = null,tvYuche = null;
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
        tvYuche = (TextView)findViewById(R.id.tv_select_yuche);
        tvYuche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(FsSelectActivity.this,FsYucheActivity.class);
                startActivity(intent);
            }
        });
    }
    private ArrayList<Map<String, Object>> getData() {
        //图片资源
        mydb = new DatabaseHelper(FsSelectActivity.this);
        String[] fn = {"id","name","cx","sj","note"};
        Cursor c1 = null;
        c1 = mydb.select(fn);
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        while (c1.moveToNext()) {
            //String pt = "初始值：山=" + c1.getString(0) + "；水=" + c1.getString(1);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", c1.getString(0));
            map.put("name", c1.getString(1));
            map.put("cx", c1.getString(2));
            map.put("sj", c1.getString(3));
            map.put("note", c1.getString(4));
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
            for (int i = 0; i < listData.size(); i++) {
                map.put(i,true);
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
            String name = listData.get(position).get("name").toString();
            nameTv.setText(name);
            nameTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String note = listData.get(position).get("note").toString();
                    new Gongju().ShowMsg(context,name+" 备注信息",
                            note,false,-1);
                }
            });

            TextView cxTv=(TextView)view.findViewById(R.id.tv_fs_cx);
            String cx = listData.get(position).get("cx").toString();
            if(cx.equals(""))
                cxTv.setText("  ...  ");
            else
                cxTv.setText(cx);
            cxTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dbid = listData.get(position).get("id").toString();
                    if(dbid == null)
                        return;
                    String[] subj ={"坐[子癸]朝[午丁]",
                            "坐[癸丑]朝[丁未]",
                            "坐[丑艮]朝[未坤]",
                            "坐[艮寅]朝[坤申]",
                            "坐[寅甲]朝[申庚]",
                            "坐[甲卯]朝[庚酉]",
                            "坐[卯乙]朝[酉辛]",
                            "坐[乙辰]朝[辛戌]",
                            "坐[辰巽]朝[戌乾]",
                            "坐[巽巳]朝[乾亥]",
                            "坐[巳丙]朝[亥壬]",
                            "坐[丙午]朝[壬子]",
                            "坐[午丁]朝[子癸]",
                            "坐[丁未]朝[癸丑]",
                            "坐[未坤]朝[丑艮]",
                            "坐[坤申]朝[艮寅]",
                            "坐[申庚]朝[寅甲]",
                            "坐[庚酉]朝[甲卯]",
                            "坐[酉辛]朝[卯乙]",
                            "坐[辛戌]朝[乙辰]",
                            "坐[戌乾]朝[辰巽]",
                            "坐[乾亥]朝[巽巳]",
                            "坐[亥壬]朝[巳丙]",
                            "坐[壬子]朝[丙午]",
                            "坐[子]朝[午]",
                            "坐[癸]朝[丁]",
                            "坐[丑]朝[未]",
                            "坐[艮]朝[坤]",
                            "坐[寅]朝[申]",
                            "坐[甲]朝[庚]",
                            "坐[卯]朝[酉]",
                            "坐[乙]朝[辛]",
                            "坐[辰]朝[戌]",
                            "坐[巽]朝[乾]",
                            "坐[巳]朝[亥]",
                            "坐[丙]朝[壬]",
                            "坐[午]朝[子]",
                            "坐[丁]朝[癸]",
                            "坐[未]朝[丑]",
                            "坐[坤]朝[艮]",
                            "坐[申]朝[寅]",
                            "坐[庚]朝[甲]",
                            "坐[酉]朝[卯]",
                            "坐[辛]朝[乙]",
                            "坐[戌]朝[辰]",
                            "坐[乾]朝[巽]",
                            "坐[亥]朝[巳]",
                            "坐[壬]朝[丙]",
                            ""};
                    AlertDialog.Builder builder = new AlertDialog.Builder(FsSelectActivity.this);
                    builder.setTitle("请选择朝向：");    //设置对话框标题

                    builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标

                    builder.setSingleChoiceItems(subj, 0, new DialogInterface.OnClickListener() {
                        // 第二个参数为默认选中项 在这里设为第一项
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String cx = subj[which];
                            mydb =new DatabaseHelper(FsSelectActivity.this);
                            mydb.update(dbid,"cx",cx);
                            Toast.makeText(FsSelectActivity.this, "你选择了："+cx+" 这朝向", Toast.LENGTH_SHORT).show();
                    //        cxTv.setText(cx);
                            listData.get(position).put("cx",cx);
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();  //创建对话框
                    dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
                    dialog.show();
                }
            });

            TextView sjTv=(TextView)view.findViewById(R.id.tv_fs_sj);
            String sj = listData.get(position).get("sj").toString();
            if(sj.equals(""))
                sjTv.setText("  ...  ");
            else
                sjTv.setText(sj);
            sjTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dbid = listData.get(position).get("id").toString();
                    if(dbid == null)
                        return;
                    AlertDialog.Builder builder = new AlertDialog.Builder(FsSelectActivity.this);
                    builder.setTitle("请输入宝地的迁入年份（公历）：");    //设置对话框标题
                    builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
                    final EditText edit = new EditText(FsSelectActivity.this);
                    builder.setView(edit);
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String sj = edit.getText().toString();
                            mydb =new DatabaseHelper(FsSelectActivity.this);
                            mydb.update(dbid,"sj",sj);
                            Toast.makeText(FsSelectActivity.this, "你输入的是："+sj+" 年", Toast.LENGTH_SHORT).show();
                            listData.get(position).put("sj",sj);
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(FsSelectActivity.this, "你点了取消", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
                    AlertDialog dialog = builder.create();  //创建对话框
                    dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
                    dialog.show();
                }
            });
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
