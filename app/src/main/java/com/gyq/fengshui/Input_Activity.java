    package com.gyq.fengshui;

    import android.app.AlertDialog;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.database.Cursor;
    import android.location.Location;
    import android.location.LocationManager;
    import android.renderscript.ScriptGroup;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.support.v7.widget.ViewUtils;
    import android.view.KeyEvent;
    import android.view.View;
    import android.widget.CursorAdapter;
    import android.widget.EditText;
    import android.widget.ImageButton;
    import android.widget.ListView;
    import android.widget.TextView;
    import android.widget.Toast;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.Map;

    public class Input_Activity extends AppCompatActivity {
        private String dbid = null;
        private MyAdapter myAdapter= null;
        private DatabaseHelper mydb = null;
        private String name ="";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.xbd_layout);
 //           final ImageButton btxmm = (ImageButton) findViewById(R.id.imgBtnMM);
            final ImageButton btxsc = (ImageButton) findViewById(R.id.imgBtnSC);
            final ImageButton btxbz = (ImageButton) findViewById(R.id.imgBtnBZ);
            final TextView textNote = (TextView)findViewById(R.id.item_note);
            final TextView textCx = (TextView)findViewById(R.id.item_cx);
            final TextView textSj = (TextView)findViewById(R.id.item_sj);
            dbid = getIntent().getStringExtra("id");
            if(dbid != null) {
//                btxmm.setVisibility(View.INVISIBLE);//.setClickable(false);
                btxsc.setVisibility(View.VISIBLE);
                name = getIntent().getStringExtra("name");
                String note = getIntent().getStringExtra("note");
                setTitle("风水大师 （" + name + "）");
                textNote.setText(note);
                String cx = getIntent().getStringExtra("cx");
                textCx.setText(cx);
                String sj = getIntent().getStringExtra("sj");
                setTitle("风水大师 （" + name + "）");
                textSj.setText(sj);
                MyListView();
            }else{
//                btxmm.setVisibility(View.VISIBLE);//.setClickable(false);
                btxsc.setVisibility(View.INVISIBLE);
 //           }
 //           btxmm.setOnClickListener(new View.OnClickListener() {
 //               @Override
  //              public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Input_Activity.this);
                builder.setTitle("请输入宝地名称：");    //设置对话框标题
                builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
                final EditText edit = new EditText(Input_Activity.this);
                builder.setView(edit);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = edit.getText().toString();
                        Toast.makeText(Input_Activity.this, "你输入的是: " + name, Toast.LENGTH_SHORT).show();
                        String[] fs1 =new String[24];
                        String[] fs2 =new String[24];
                        for(int i=0;i<24;i++) {
                            fs1[i] = "0";
                            fs2[i] = "0";
                        }
                        mydb = new DatabaseHelper(Input_Activity.this);
                        dbid = java.util.UUID.randomUUID().toString();
                        //                          String note1 = "经纬度为（" + getLngAndLat(Input_Activity.this) + "）";
                        String note1 = "经纬度为（无法获取）";
                        mydb.insert(dbid, edit.getText().toString(), note1, fs1, fs2);
                        //                          btxmm.setClickable(false);
                        setTitle("风水大师 （" + name + "）");
                        MyListView();
                        btxsc.setVisibility(View.VISIBLE);
                        textNote.setText(note1);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Input_Activity.this, "你点了取消", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
                AlertDialog dialog = builder.create();  //创建对话框
                dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
                dialog.show();
            }

   //         });
            btxbz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dbid == null)
                        return;
                    AlertDialog.Builder builder = new AlertDialog.Builder(Input_Activity.this);
                    builder.setTitle("请输入新的备注信息：");    //设置对话框标题
                    builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
                    final EditText edit = new EditText(Input_Activity.this);
                    builder.setView(edit);
                    if(dbid != null)
                        edit.setText(getDbnote(dbid));
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String note = edit.getText().toString();
                            //    Toast.makeText(Input_Activity.this, "你输入的是: " + name, Toast.LENGTH_SHORT).show();
                            mydb =new DatabaseHelper(Input_Activity.this);
                            mydb.update(dbid,"note",note);
                            textNote.setText(note);
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(Input_Activity.this, "你点了取消", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
                    AlertDialog dialog = builder.create();  //创建对话框
                    dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
                    dialog.show();
                }
            });
            textCx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(Input_Activity.this);
                    builder.setTitle("请选择朝向：");    //设置对话框标题

                    builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标

                    builder.setSingleChoiceItems(subj, 0, new DialogInterface.OnClickListener() {
                        // 第二个参数为默认选中项 在这里设为第一项
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String cx = subj[which];
                            mydb =new DatabaseHelper(Input_Activity.this);
                            mydb.update(dbid,"cx",cx);
                            Toast.makeText(Input_Activity.this, "你选择了："+cx+" 这朝向", Toast.LENGTH_SHORT).show();
                            //        cxTv.setText(cx);
                            textCx.setText(cx);
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();  //创建对话框
                    dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
                    dialog.show();
                }
            });
            textSj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dbid == null)
                        return;
                    AlertDialog.Builder builder = new AlertDialog.Builder(Input_Activity.this);
                    builder.setTitle("请输入宝地的迁入年份（公历）：");    //设置对话框标题
                    builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
                    final EditText edit = new EditText(Input_Activity.this);
                    builder.setView(edit);
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String sj = edit.getText().toString();
                            mydb =new DatabaseHelper(Input_Activity.this);
                            mydb.update(dbid,"sj",sj);
                            Toast.makeText(Input_Activity.this, "你输入的是："+sj+" 年", Toast.LENGTH_SHORT).show();
                            textSj.setText(sj);
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(Input_Activity.this, "你点了取消", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
                    AlertDialog dialog = builder.create();  //创建对话框
                    dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
                    dialog.show();
                }
            });


            btxsc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dbid != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Input_Activity.this);
                        builder.setTitle("确定要删除宝地：[" + name + "]?");    //设置对话框标题
                        builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mydb = new DatabaseHelper(Input_Activity.this);
                                mydb.delete(dbid);
                                finish();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Input_Activity.this, "你点了取消", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
                        AlertDialog dialog = builder.create();  //创建对话框
                        dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
                        dialog.show();
                    }
                }
            });
        }
        public void showMsg(String msg){
            AlertDialog.Builder dlg = new AlertDialog.Builder(Input_Activity.this);
            dlg.setTitle(msg);
            AlertDialog dlg2 = dlg.create();
            dlg2.show();
        }
        public void MyListView(){
            ArrayList<Map<String,Object>> listData = getData() ;
            //Toast.makeText(getApplicationContext(), "InputActivity MyListeView dbid="+dbid, 1).show();
            myAdapter = new MyAdapter(this,listData,dbid) ;
            ListView lv= (ListView)findViewById(R.id.list_view);
            lv.setAdapter(myAdapter) ;
        }
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
  //              startActivity(new Intent(MainActivity.this, Input_Activity.class));
                finish();
            }
            return super.onKeyDown(keyCode, event);
        }
        private ArrayList<Map<String,Object>> getData(String id){
            mydb =new DatabaseHelper(Input_Activity.this);
            String[] fn={"shan01","shan02","shan03","shan04","shan05","shan06",
                    "shan07","shan08","shan09","shan10","shan11","shan12",
                    "shan13","shan14","shan15","shan16","shan17","shan18",
                    "shan19","shan20","shan21","shan22","shan23","shan24",
                    "shui01","shui02","shui03","shui04","shui05","shui06",
                    "shui07","shui08","shui09","shui10","shui11","shui12",
                    "shui13","shui14","shui15","shui16","shui17","shui18",
                    "shui19","shui20","shui21","shui22","shui23","shui24"
            };
            Cursor c1 = mydb.select(id,fn);
            ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            while(c1.moveToNext()) {
                //String pt = "初始值：山=" + c1.getString(0) + "；水=" + c1.getString(1);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("img",R.drawable.fdj);
                map.put("id", c1.getString(0));
                map.put("name", c1.getString(1));
                map.put("note", c1.getString(2));
                map.put("ct", c1.getString(3));
                list.add(map);
            }
            return list;

        }
        private ArrayList<Map<String,Object>> getData()
        {
            final String[] pt = new String[24];
            if(dbid != null){
                mydb =new DatabaseHelper(Input_Activity.this);
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
                ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                while(c1.moveToNext()) {
                    //String pt = "初始值：山=" + c1.getString(0) + "；水=" + c1.getString(1);
                    for(int i=0;i<24;i++)
                        pt[i] = "当前值：山="+c1.getString(i)+"；水="+c1.getString(i+24);
                }
            }else {
                for (int i = 0; i < 24; i++)
                    pt[i] = "初始值：山=0；水=0";
            }
            int[] imgId = new int[]{
                    R.drawable.fs001,
                    R.drawable.fs002,
                    R.drawable.fs003,
                    R.drawable.fs004,
                    R.drawable.fs005,
                    R.drawable.fs006,
                    R.drawable.fs007,
                    R.drawable.fs008,
                    R.drawable.fs009,
                    R.drawable.fs010,
                    R.drawable.fs011,
                    R.drawable.fs012,
                    R.drawable.fs013,
                    R.drawable.fs014,
                    R.drawable.fs015,
                    R.drawable.fs016,
                    R.drawable.fs017,
                    R.drawable.fs018,
                    R.drawable.fs019,
                    R.drawable.fs020,
                    R.drawable.fs021,
                    R.drawable.fs022,
                    R.drawable.fs023,
                    R.drawable.fs024
            };
            int[] imgId2= new int[24];
            for( int i=0;i<24;i++)
                imgId2[i]=R.drawable.feng;
            int[] imgId3= new int[24];
            for( int i=0;i<24;i++)
                imgId3[i]=R.drawable.shui;
            ArrayList<Map<String,Object>> listItems = new ArrayList<Map<String, Object>>();
            for (int i=0;i<pt.length;i++){
                Map<String,Object> listItem = new HashMap<String,Object>();
                listItem.put("img1",imgId[i]);
                listItem.put("img2",imgId2[i]);
                listItem.put("img3",imgId3[i]);
                listItem.put("text",pt[i]);
                listItems.add(listItem);
            }
            return listItems;
        }
        private String getDbnote(String dbid) {
            mydb = new DatabaseHelper(Input_Activity.this);
            //showMsg("刷新getData2 step 2 {"+shan+","+shui+"}");
            String[] fn = {"note"};
            Cursor c1 = mydb.select(dbid, fn);
            //showMsg("刷新getData2 step 3");
            while (c1.moveToNext()) {
                String pt = "";
                String note1 = c1.getString(0);
                return note1;
            }
            return "";
        }
        private String getDbValue()
        {
            mydb=  new DatabaseHelper(Input_Activity.this);
            int k = myAdapter.fid + 1;
            String shan = "shan";
            if(k<10)
                shan = shan + "0" + k;
            else
                shan = shan + k;
            String shui = "shui";
            if(k<10)
                shui = shui + "0" + k;
            else
                shui = shui + k;
            String[] fn =  {shan,shui};
            //showMsg("刷新getData2 step 2 {"+shan+","+shui+"}");
            Cursor c1 = mydb.select(this.dbid,fn);
            //showMsg("刷新getData2 step 3");
            while(c1.moveToNext()) {
                String pt = "当前值：山=" + c1.getString(0) + "；水=" + c1.getString(1);
                return pt;
            }
            return "";
        }
        public DatabaseHelper getMydb(){
            return this.mydb;
        }
        public void onResume() {
            super.onResume();
            if(myAdapter != null) {
                myAdapter.setListData(getData());
                myAdapter.notifyDataSetChanged();
            }
        }
        private String getLngAndLat(Context context) {
            double latitude = 0.0;
            double longitude = 0.0;
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {  //从gps获取经纬度
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                } else {//当GPS信号弱没获取到位置的时候又从网络获取
                    return getLngAndLatWithNetwork();
                }
            } else {    //从网络获取经纬度
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
            java.text.DecimalFormat ft1 = new java.text.DecimalFormat("0.000000");
            return ft1.format(longitude) + "," + ft1.format(latitude);
        }

        //从网络获取经纬度
        public String getLngAndLatWithNetwork() {
            double latitude = 0.0;
            double longitude = 0.0;
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            java.text.DecimalFormat ft1 = new java.text.DecimalFormat("0.000000");
            return ft1.format(longitude) + "," + ft1.format(latitude);
        }

    }
