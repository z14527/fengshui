package com.gyq.fengshui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;
import static java.lang.Math.min;

public class FsChartActivity extends AppCompatActivity implements View.OnClickListener{
    private static DrawingView mDrawingView;
    private static TextView mTextView = null;
    private TextView mTvReDraw = null;
    private TextView mTvToShui = null;
    private TextView mTvShanShuiSwitch = null;
    private TextView mTvClear = null;
    private TextView mTvSave = null;
    private static int COLOR_PANEL = 0;
    private static int BRUSH = 0;
    private ImageButton mColorPanel;
    private ImageButton mBrush;
    private ImageButton mUndo;
    private ImageButton mSave;
    public static Map<String,Integer> ShanMap=new HashMap<>();
    public static Map<String,Integer> ShuiMap=new HashMap<>();
    public static String shan ="乙辰巽巳丙午丁未坤申庚酉辛戌乾亥壬子癸丑艮寅甲卯";
    public static String lshan = null;
    public static String shui ="辰巽巳丙午丁未坤申庚酉辛戌乾亥壬子癸丑艮寅甲卯乙";
    //            "申坤未丁午丙巳巽辰乙卯甲寅艮丑癸子壬亥乾戌辛酉庚";
    private static String lshui = null;
    public static int bshan = 1;
    public static AlertDialog.Builder builder = null;
    public static Context context = null;
    public static int colBlue;
    public static int colRed;
    public static int colTl;
    public static String dbid = "";
    public static String name = "";
    private static DatabaseHelper mydb = null;
    private static String fs24 = "子癸丑艮寅甲卯乙辰巽巳丙午丁未坤申庚酉辛戌乾亥壬";

    public static Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            switch (what) {
                case 3:
                    String mt2 = msg.getData().getString("send");
                    String[] mt3 = mt2.split(":");
                    if(mt3.length>=2){
                        int k = Integer.parseInt(mt3[0]);
                        final int k1 = k;
                        int r = Integer.parseInt(mt3[1]);
                        if(k>=0 && k<24) {
                            if(bshan==1) {
                                String fs1 = shan.substring(k, k + 1);
                                if(r>=0){
                                    if (fs1.equals(lshan))
                                        ShanMap.put(fs1, Math.max(ShanMap.get(fs1), r));
                                    else
                                        ShanMap.put(fs1, r);
                                    lshan = fs1;
                                    String info = "";
                                    for (int j = 0; j < 24; j++) {
                                        info = info + shan.substring(j, j + 1) + ": " +
                                                String.format("%04d",ShanMap.get(shan.substring(j, j + 1)))
                                                + "\t\t\t\t";
                                        if (j % 3 == 2)
                                            info = info + "\n";
                                    }
                                    info = info.replaceAll(": 000",":     ");
                                    info = info.replaceAll(": 00",":   ");
                                    info = info.replaceAll(": 0",": ");
                                    info = info+"\n"+"(x0,y0)=("+mDrawingView.getX0()+","+mDrawingView.gety0()+
                                            ")\n(x1,y1)=("+mDrawingView.getX1()+","+mDrawingView.gety1()+
                                            ")\nr="+mDrawingView.getr();
                                    mTextView.setTextColor(colRed);
                                    mTextView.setBackgroundColor(colBlue);
                                    mTextView.setText(info);
                                }else{
                                    builder = new AlertDialog.Builder(context);
                                    builder.setTitle("请输入 "+fs1+" 的山的数值(-100 - 100)：");    //设置对话框标题
                                    builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
                                    final EditText edit = new EditText(context);
                                    builder.setView(edit);
                                    builder.setPositiveButton("修改山", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String shan1 = edit.getText().toString();
                                            int r1 = 0;
                                            try {
                                                r1 = Integer.parseInt(shan1);
                                            }catch (Exception e1)
                                            {
                                                e1.printStackTrace();
                                            }
                                            Toast.makeText(context,"输入的数值为："+shan1, LENGTH_LONG).show();
                                            if (fs1.equals(lshan))
                                                ShanMap.put(fs1, Math.max(ShanMap.get(fs1), r1));
                                            else
                                                ShanMap.put(fs1, r1);
                                            mDrawingView.ReDrawImage();
                                        }
                                    });
                                    builder.setNeutralButton("修改山以及对应的水",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String shan1 = edit.getText().toString();
                                            int r1 = 0;
                                            try {
                                                r1 = Integer.parseInt(shan1);
                                            }catch (Exception e1)
                                            {
                                                e1.printStackTrace();
                                            }
                                            Toast.makeText(context,"输入的数值为："+shan1, LENGTH_LONG).show();
                                            String fs2 = "";
                                            if(k1>0)
                                                fs2 = shan.substring(k1-1, k1);
                                            else
                                                fs2 = shan.substring(23,24);
                                            if (fs1.equals(lshan)) {
                                                ShanMap.put(fs1, Math.max(ShanMap.get(fs1), r1));
                                                ShuiMap.put(fs2, Math.max(ShanMap.get(fs1), r1));
                                            }
                                            else {
                                                ShanMap.put(fs1, r1);
                                                ShuiMap.put(fs2, r1);
                                            }
                                            mDrawingView.ReDrawImage();
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
                                }
                            }else{
                                k = k - 1;
                                if(k < 0 )
                                    k = k + 24;
                                String fs1 = shui.substring(k, k + 1);
                                if(r>=0) {
                                    if (fs1.equals(lshui))
                                        ShuiMap.put(fs1, Math.max(ShuiMap.get(fs1), r));
                                    else
                                        ShuiMap.put(fs1, r);
                                    lshui = fs1;
                                    String info = "";
                                    for (int j = 0; j < 24; j++) {
                                        info = info + shui.substring(j, j + 1) + ": " +
                                                String.format("%04d",ShuiMap.get(shui.substring(j, j + 1)))+
                                                "\t\t\t\t";
                                        if (j % 3 == 2)
                                            info = info + "\n";
                                    }
                                    info = info.replaceAll(": 000",":     ");
                                    info = info.replaceAll(": 00",":   ");
                                    info = info.replaceAll(": 0",": ");
                                    info = info+"\n"+"(x0,y0)=("+mDrawingView.getX0()+","+mDrawingView.gety0()+
                                            ")\n(x1,y1)=("+mDrawingView.getX1()+","+mDrawingView.gety1()+
                                            ")\nr="+mDrawingView.getr();
                                    mTextView.setTextColor(colBlue);
                                    mTextView.setBackgroundColor(colTl);
                                    mTextView.setText(info);
                                }else{
                                    builder = new AlertDialog.Builder(context);
                                    builder.setTitle("请输入 "+fs1+" 的水的数值(-100 - 100)：");    //设置对话框标题
                                    builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
                                    final EditText edit = new EditText(context);
                                    builder.setView(edit);
                                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String shui1 = edit.getText().toString();
                                            int r1 = 0;
                                            try {
                                                r1 = Integer.parseInt(shui1);
                                            }catch (Exception e1)
                                            {
                                                e1.printStackTrace();
                                            }
                                            Toast.makeText(context,"输入的数值为："+shui1, LENGTH_LONG).show();
                                            if (fs1.equals(lshui))
                                                ShuiMap.put(fs1, Math.max(ShuiMap.get(fs1), r1));
                                            else
                                                ShuiMap.put(fs1, r1);
                                            mDrawingView.ReDrawImage();
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
                                }
                            }
                        }
                    }
                    break;
                case 4:
                    String mt4 = msg.getData().getString("send");
                    mTextView.append(mt4+"\n");
                    break;
                case 5:
                    if(bshan==1) {
                        String info = "";
                        for (int j = 0; j < 24; j++) {
                            info = info + shan.substring(j, j + 1) + ": " +
                                    String.format("%04d",ShanMap.get(shan.substring(j, j + 1))) +
                                    "\t\t\t\t";
                            if (j % 3 == 2)
                                info = info + "\n";
                        }
                        info = info.replaceAll(": 000",":     ");
                        info = info.replaceAll(": 00",":   ");
                        info = info.replaceAll(": 0",": ");
                        info = info+"\n"+"(x0,y0)=("+mDrawingView.getX0()+","+mDrawingView.gety0()+
                                ")\n(x1,y1)=("+mDrawingView.getX1()+","+mDrawingView.gety1()+
                                ")\nr="+mDrawingView.getr();
                        mTextView.setTextColor(colRed);
                        mTextView.setBackgroundColor(colBlue);
                        mTextView.setText(info);
                    }else{
                        String info = "";
                        for (int j = 0; j < 24; j++) {
                            info = info + shui.substring(j, j + 1) + ": " +
                                    String.format("%04d",ShuiMap.get(shui.substring(j, j + 1))) +
                                    "\t\t\t\t";
                            if (j % 3 == 2)
                                info = info + "\n";
                        }
                        info = info.replaceAll(": 000",":     ");
                        info = info.replaceAll(": 00",":   ");
                        info = info.replaceAll(": 0",": ");
                        info = info+"\n"+"(x0,y0)=("+mDrawingView.getX0()+","+mDrawingView.gety0()+
                                ")\n(x1,y1)=("+mDrawingView.getX1()+","+mDrawingView.gety1()+
                                ")\nr="+mDrawingView.getr();
                        mTextView.setTextColor(colBlue);
                        mTextView.setBackgroundColor(colTl);
                        mTextView.setText(info);
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fschart);
        initViews();
        initPaintMode();
        loadImage();
        bshan = 1;
        mDrawingView.setPenColor(colRed);
        mDrawingView.ReDrawImage();
    }
    private void initViews() {
        setTitle("风水大师 - " + name);
        mDrawingView = (DrawingView) findViewById(R.id.img_screenshot);
//        WindowManager manager = (WindowManager) context
//                .getSystemService(Context.WINDOW_SERVICE);
//        Display display = manager.getDefaultDisplay();
//        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) mDrawingView.getLayoutParams(); //取控件textView当前的布局参数
//        linearParams.height = display.getHeight();
        mTextView = (TextView)findViewById(R.id.tvmessage);
        mTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new Gongju().ShowMsg(arg0.getContext(),"",mTextView.getText().toString(),false,-1);
            }
        });
        mTvReDraw = (TextView)findViewById(R.id.tvredraw);
        mTvReDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mDrawingView.ReDrawImage();
            }
        });
        mTvClear = (TextView)findViewById(R.id.tvclear);
        mTvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new Gongju().ShowMsg(arg0.getContext(),
                        "提示",
                        "是否要清除所有山水数据？",
                        true,
                        1);
            }
        });
        mTvToShui = (TextView)findViewById(R.id.tv_to_shui);
        mTvToShui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mDrawingView.setPenColor(getColor(R.color.blue));
                Gongju gj = new Gongju();
                gj.ShowMsg(arg0.getContext(),"山->水","是否要清除原有水数据？",true,0);
            }
        });
        mTvShanShuiSwitch = (TextView)findViewById(R.id.tv_shan_shui_switch);
        mTvShanShuiSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(bshan == 1) {
                    mDrawingView.setPenColor(getColor(R.color.blue));
                    bshan = 0;
                }
                else{
                    mDrawingView.setPenColor(getColor(R.color.red));
                    bshan = 1;
                }
                mDrawingView.ReDrawImage();
            }
        });
        mTvSave = (TextView)findViewById(R.id.tvsave);
        mTvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new Gongju().ShowMsg(arg0.getContext(),
                        "提示",
                        "是否要将新数据保存到工程 "+name+" 中？",
                        true,2);
            }
        });
        mBrush = (ImageButton) findViewById(R.id.brush);
        mColorPanel = (ImageButton) findViewById(R.id.color_panel);
        mUndo = (ImageButton) findViewById(R.id.undo);
        mSave = (ImageButton) findViewById(R.id.save);
        mBrush.setOnClickListener(this);
        mColorPanel.setOnClickListener(this);
        mUndo.setOnClickListener(this);
        mSave.setOnClickListener(this);
        initPaintMode();
        if(dbid.equals("")) {
            for (int i = 0; i < 24; i++) {
                ShanMap.put(shan.substring(i, i + 1), 0);
                ShuiMap.put(shui.substring(i, i + 1), 0);
            }
        }
    }

    private void initPaintMode() {
        mDrawingView.initializePen();
        mDrawingView.setPenSize(10);
        colBlue = getColor(R.color.blue);
        colRed = getColor(R.color.red);
        colTl = getColor(R.color.translucent);
        mDrawingView.setPenColor(colRed);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.brush:
                mBrush.setImageResource(BRUSH == 0 ? R.drawable.ic_brush : R.drawable.ic_pen);
//                if(mDrawingView.getDrawMode())
//                    mDrawingView.setDrawMode(false);
//                else
//                    mDrawingView.setDrawMode(true);
                mDrawingView.setPenSize(BRUSH == 0 ? 40 : 10);
                BRUSH = 1 - BRUSH;
                break;
            case R.id.color_panel:
                mColorPanel.setImageResource(COLOR_PANEL == 0 ? R.drawable.ic_color_blue : R.drawable.ic_color_red);
                mDrawingView.setPenColor(COLOR_PANEL == 0 ? getColor(R.color.blue) : getColor(R.color.red));
                COLOR_PANEL = 1 - COLOR_PANEL;
                break;
            case R.id.undo:
                mDrawingView.undo();
                break;
            case R.id.save:
                String sdcardPath = Environment.getExternalStorageDirectory().toString();
                if (mDrawingView.saveImage(sdcardPath, "DrawImg", Bitmap.CompressFormat.PNG, 100)){
                    Toast.makeText(this, "Save Success", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    public void loadImage() {
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        float xdpi = getResources().getDisplayMetrics().xdpi;
        float ydpi = getResources().getDisplayMetrics().ydpi;
        // 这样可以计算屏幕的物理尺寸
        float width2 = (width / xdpi)*(width / xdpi);
        float height2 = (height / ydpi)*(width / xdpi);
     //   Toast.makeText(this, "w="+width2+",h="+height2, Toast.LENGTH_LONG).show();
        Bitmap bitmap;
        if(min(width2,height2)>10)
            bitmap = BitmapFactory.decodeResource(getResources(), R.raw.fs02);
        else
            bitmap = BitmapFactory.decodeResource(getResources(), R.raw.fs01);
        mDrawingView.loadImage(bitmap);
    }
    public static void shan2shui(){
        for(int i=0;i<24;i++) {
            String fs1 = shui.substring(i, i + 1);
            String fs2 = "";
            if(i>0)
                fs2 = shui.substring(i-1, i);
            else
                fs2 = shui.substring(23,24);
            int r1 = ShanMap.get(fs1);
            ShuiMap.put(fs2, r1);
        }
        mDrawingView.setPenColor(colBlue);
        bshan = 0;
        mDrawingView.ReDrawImage();
    }
    public static void reDraw(){
        for (int i = 0; i < 24; i++) {
            ShanMap.put(shan.substring(i, i + 1), 0);
            ShuiMap.put(shui.substring(i, i + 1), 0);
        }
        mDrawingView.ReDrawImage();
    }
    public static void saveData(){
        mydb = new DatabaseHelper(context);
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
        for(int i=0;i<24;i++) {
            String fs1 = fs24.substring(i,i+1);
            mydb.update(dbid,fn[i],""+ShanMap.get(fs1));
            mydb.update(dbid,fn[i+24],""+ShuiMap.get(fs1));
        }
        Toast.makeText(context.getApplicationContext(),"已经保存!",Toast.LENGTH_LONG).show();
    }

}
