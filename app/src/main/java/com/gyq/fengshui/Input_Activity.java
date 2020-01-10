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
            dbid = getIntent().getStringExtra("id");
            if(dbid != null) {
//                btxmm.setVisibility(View.INVISIBLE);//.setClickable(false);
                btxsc.setVisibility(View.VISIBLE);
                name = getIntent().getStringExtra("name");
                String note = getIntent().getStringExtra("note");
                setTitle("风水大师 （" + name + "）");
                textNote.setText(note);
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
                        if (mydb == null)
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
                            if(mydb == null)
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
                                if (mydb == null)
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
            if(mydb==null)
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
                if(mydb==null)
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
            if (mydb == null)
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
            if(mydb==null)
                return "";
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
