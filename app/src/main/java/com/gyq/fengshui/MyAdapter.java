package com.gyq.fengshui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gyq.fengshui.R;

public class MyAdapter extends BaseAdapter {

    private LayoutInflater mInflater ;
    private ArrayList<Map<String,Object>> listData ;
    private Context context ;
    private String dbid;
    private String fs24 = "子癸丑艮寅甲卯乙辰巽巳丙午丁未坤申庚酉辛戌乾亥壬";
    public int fid = 0;
    public MyAdapter(Context context,ArrayList<Map<String,Object>> listData,String id1)
    {
        mInflater = LayoutInflater.from(context) ;
        this.listData = listData ;
        this.context = context ;
        this.dbid = id1;
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

        arg1 = mInflater.inflate(R.layout.list_view_temp, null);
        //需要为每个控件指定内容，如指定textView的显示文字（这就是引用的listData作用）
        holder.textView1 = (TextView)arg1.findViewById(R.id.text_view) ;
        holder.textView1.setText(listData.get(arg0).get("text").toString()) ;
        holder.imageView1 = (ImageView) arg1.findViewById(R.id.image_view1);
        holder.imageView1.setImageResource((Integer)listData.get(arg0).get("img1")) ;
        holder.imageView2 = (ImageView) arg1.findViewById(R.id.image_view2);
        holder.imageView2.setImageResource((Integer)listData.get(arg0).get("img2")) ;
        holder.imageView3 = (ImageView) arg1.findViewById(R.id.image_view3);
        holder.imageView3.setImageResource((Integer)listData.get(arg0).get("img3")) ;

        holder.imageView1.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(getApplicationContext(), "点击的是："+fs24.substring(arg0,arg0+1)+" id="+dbid, 1).show();
                fid =  arg0;
            }});
        holder.imageView2.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(getApplicationContext(), "点击的是："+fs24.substring(arg0,arg0+1)+"的山"+" id="+dbid, 1).show();
                fid = arg0;
                Intent intent = new Intent(getApplicationContext(),DataActivity.class);
                intent.putExtra("id",dbid);
                intent.putExtra("fs24",arg0);
                intent.putExtra("type","山");
                getApplicationContext().startActivity(intent);
            }});
        holder.imageView3.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(getApplicationContext(), "点击的是："+fs24.substring(arg0,arg0+1)+"的水"+" id="+dbid, 1).show();
                fid = arg0;
                Intent intent = new Intent(getApplicationContext(),DataActivity.class);
                intent.putExtra("id",dbid);
                intent.putExtra("fs24",arg0);
                intent.putExtra("type","水");
                getApplicationContext().startActivity(intent);
            }});
        return arg1;
    }

    protected Context getApplicationContext() {
        // TODO Auto-generated method stub
        return context;
    }

    public class ViewHolder
    {
        public TextView textView1 ;
        public ImageView imageView1 ;
        public ImageView imageView2 ;
        public ImageView imageView3 ;
    }

    public void update(int index,String text1,ListView lv){
        View arg1 = lv.getChildAt(index);
        //需要为每个控件指定内容，如指定textView的显示文字（这就是引用的listData作用）
        TextView textView1 = (TextView)arg1.findViewById(R.id.text_view) ;
        textView1.setText(text1);
    }
    public ArrayList<Map<String,Object>> getListData(){
        return this.listData;
    }
}
