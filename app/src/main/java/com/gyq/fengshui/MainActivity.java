package com.gyq.fengshui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.LauncherActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import android.telephony.TelephonyManager;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;

import com.tbruyelle.rxpermissions2.RxPermissions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
//import org.rosuda.REngine.REXPMismatchException;
//import org.rosuda.REngine.REngineException;
//import org.rosuda.REngine.Rserve.RConnection;
//import org.rosuda.REngine.Rserve.RserveException;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity  {
    public static String up_url = "";
    public static String listdb_url = "";
    public static String data_url = "";
    public static String pf_url = "";
    public static String r_ip = "127.0.0.1";
    public static String r_port = "9091";
    public static String r_ip_1 = "127.0.0.1";
    public static String r_port_1 = "9091";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("风水大师（远程）");
       // new Gongju().LogClear("fs.txt");
        up_url = getString(R.string.up_url);
        listdb_url = getString(R.string.listdb_url);
        data_url = getString(R.string.data_url);
        pf_url = getString(R.string.pf_url);
        String[] permissionsGroup=new String[]{Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_NUMBERS,
                Manifest.permission.READ_PHONE_STATE,
              //  Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.REORDER_TASKS
        };
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .requestEach(permissionsGroup)
                .subscribe(permission -> {
                    if(!permission.granted)
                        Toast.makeText(this,"权限名称:"+permission.name+",申请结果:"+permission.granted, LENGTH_LONG).show();
                });
        final ImageButton btxbd = (ImageButton) findViewById(R.id.imgBtnXBD);
        btxbd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                makeText(MainActivity.this,getString(R.string.main_activity_button2_name), LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,Input_Activity.class);
                startActivity(intent);
            }
        });
     //   btxbd.setVisibility(View.INVISIBLE);
        final ImageButton btlsbd = (ImageButton) findViewById(R.id.imgBtnLSBD);
        btlsbd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                makeText(MainActivity.this,getString(R.string.main_activity_button3_name), LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,LishiActivity.class);
                startActivity(intent);
            }
        });
   //     btlsbd.setVisibility(View.INVISIBLE);
        ImageButton btlpc = (ImageButton) findViewById(R.id.imgBtnLPC);
        btlpc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddLpViewActivity.class);
                String name = "";
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                name = "风水叠加" + year + month + day + hour;
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
        final ImageButton btlnfyc = (ImageButton) findViewById(R.id.imgBtnNFYC);
        btlnfyc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
           //     makeText(MainActivity.this,getString(R.string.main_activity_button3_name), LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,FsSelectActivity.class);
                startActivity(intent);
            }
        });

//        if(notHasLightSensorManager()) {
//            btxbd.setEnabled(false);
//            btlsbd.setEnabled(false);
//        }
    }
    public Boolean notHasLightSensorManager() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor8 = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); //光
        if (null == sensor8) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.local_setting) {
            up_url = getString(R.string.local_up_url);
            listdb_url = getString(R.string.local_listdb_url);
            data_url = getString(R.string.local_data_url);
            pf_url = getString(R.string.local_pf_url);
            Toast.makeText(this,"已经设置为本地服务器模式！",
                    Toast.LENGTH_LONG).show();
            setTitle("风水大师（本地）");
        }
        if (id == R.id.test_setting) {
            up_url = getString(R.string.test_up_url);
            listdb_url = getString(R.string.test_listdb_url);
            data_url = getString(R.string.test_data_url);
            pf_url = getString(R.string.test_pf_url);
            Toast.makeText(this,"已经设置为本地测试模式！",
                    Toast.LENGTH_LONG).show();
            setTitle("风水大师（测试）");
        }
        if (id == R.id.custom_setting) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("请输入服务器地址：");    //设置对话框标题
            builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
            final EditText edit = new EditText(this);
            builder.setView(edit);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String ip = edit.getText().toString();
                    String[] rpp = ip.split(":");
                    r_ip = rpp[0];
                    if(rpp.length>1)
                        r_port = rpp[1];
                    up_url = "http://" + ip + "/up.php";
                    listdb_url = "http://" + ip + "/listdb.php";
                    data_url = "http://" + ip + "/data/";
                    pf_url = "http://" + ip + "/pf.php";
                    Toast.makeText(getApplicationContext(),"服务器已经自定义设置！", Toast.LENGTH_LONG).show();
                    setTitle("风水大师（" + ip + "自定义）");
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "你点了取消", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
            AlertDialog dialog = builder.create();  //创建对话框
            dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
class Gongju {
    public String getNumber(Activity at) {
        return "13683559392";
//        TelephonyManager tm = (TelephonyManager) at.getSystemService(TELEPHONY_SERVICE);
//        String phoneNumber1 = "";
//        try {
//            phoneNumber1 = tm.getLine1Number();
//        }catch(SecurityException e1){
//            e1.printStackTrace();
//        }
//     //   Toast.makeText(at.getApplicationContext(), "手机号码:" + phoneNumber1, Toast.LENGTH_SHORT).show();
//        if (phoneNumber1.equals("")) {
//            try {
//                phoneNumber1 = tm.getImei();
//            }catch(SecurityException e1){
//                e1.printStackTrace();
//            }
//        }
//        return phoneNumber1;
    }
    public void ShowMsg(Context context,String title, String msg,boolean cancel,int type){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);    //设置对话框标题
        builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
        final TextView tv = new TextView(context);
        //tv.setBackgroundResource(R.drawable.fengmian);
        tv.setTextSize(25);
        tv.setTextColor(Color.BLACK);
        tv.setText(msg);
        tv.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
        builder.setView(tv);
        tv.setTextColor(Color.RED);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(type==0)
                    FsChartActivity.shan2shui();
                if(type==1)
                    FsChartActivity.reDraw();
                if(type==2)
                    FsChartActivity.saveData();
            }
        });
        if(cancel)
            builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
        AlertDialog dialog = builder.create();  //创建对话框
        dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
        dialog.show();
    }
    public void Log(String filename,String info){
        try {
            String strFilePath = Environment.getExternalStorageDirectory().getPath() + "/" + filename;
            File f1 = new File(strFilePath);
            FileOutputStream fos = new FileOutputStream(strFilePath,true);
            if(f1.length()>1000*100)
                fos = new FileOutputStream(strFilePath);
            byte []szBuf = info.getBytes();
            fos.write(szBuf);
            fos.close();
        } catch (Exception e) {
        }
    }
    public void LogClear(String filename){
        try {
            String strFilePath = Environment.getExternalStorageDirectory().getPath() + "/" + filename;
            FileOutputStream fos = new FileOutputStream(strFilePath);
            fos.close();
        } catch (Exception e) {
        }
    }
    public void copyFile(String source,String target){
        Path sourcePath = Paths.get(source);
        Path destinationPath = Paths.get(target);
        try {
            Files.copy(sourcePath, destinationPath,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void ToastMsg(Context context, String msg){
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        LinearLayout linearLayout = (LinearLayout) toast.getView();
        TextView messageTextView = (TextView) linearLayout.getChildAt(0);
        messageTextView.setTextSize(25);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
class RealPathFromUriUtils {
    /**
     * 根据Uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 19) { // api >= 19
            return getRealPathFromUriAboveApi19(context, uri);
        } else { // api < 19
            return getRealPathFromUriBelowAPI19(context, uri);
        }
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
}
class SystemHelper {
    /**
     * 判断本地是否已经安装好了指定的应用程序包
     *
     * @param packageNameTarget ：待判断的 App 包名，如 微博 com.sina.weibo
     * @return 已安装时返回 true,不存在时返回 false
     */
    public static boolean appIsExist(Context context, String packageNameTarget) {
        if (!"".equals(packageNameTarget.trim())) {
            PackageManager packageManager = context.getPackageManager();
            List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES);
            for (PackageInfo packageInfo : packageInfoList) {
                String packageNameSource = packageInfo.packageName;
                if (packageNameSource.equals(packageNameTarget)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将本应用置顶到最前端
     * 当本应用位于后台时，则将它切换到最前端
     *
     * @param context
     */
    public static void setTopApp(Context context) {
        if (!isRunningForeground(context)) {
            /**获取ActivityManager*/
            ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);

            /**获得当前运行的task(任务)*/
            List<ActivityManager.RunningTaskInfo> taskInfoList = activityManager.getRunningTasks(100);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                /**找到本应用的 task，并将它切换到前台*/
                if (taskInfo.topActivity.getPackageName().equals(context.getPackageName())) {
                    activityManager.moveTaskToFront(taskInfo.id, 0);
                    break;
                }
            }
        }
    }

    /**
     * 判断本应用是否已经位于最前端
     *
     * @param context
     * @return 本应用已经位于最前端时，返回 true；否则返回 false
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();
        /**枚举进程*/
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfoList) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (appProcessInfo.processName.equals(context.getApplicationInfo().processName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void startLocalApp(Context context,String packageNameTarget) {
        if (SystemHelper.appIsExist(context, packageNameTarget)) {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(packageNameTarget);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);

            /**android.intent.action.MAIN：打开另一程序
             */
            intent.setAction("android.intent.action.MAIN");
            /**
             * FLAG_ACTIVITY_SINGLE_TOP:
             * 如果当前栈顶的activity就是要启动的activity,则不会再启动一个新的activity
             */
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);
        } else {
            Toast.makeText(context.getApplicationContext(), "被启动的 APP 未安装", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isHttpOpen(String ip, int port)
    {
        if(isEmpty(ip))
        {
            return false;
        }
        Socket socket = null;        // socket链接
        OutputStream os = null;      // 输出流
        BufferedReader br = null;    // 输入流
        boolean flag = false;        // 是否开启了HTTP服务
        try
        {
            socket = new Socket(ip, port);
            os = socket.getOutputStream();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // 创建HTTP请求报文信息
            String reqInfo =  "HEAD / HTTP/1.0"
                    + "\r\n"
                    + "Host: " + ip + ":" + port
                    + "\r\n\r\n";
            System.out.println("请求报文 : \r\n" + reqInfo);
            // 发送请求消息
            os.write(reqInfo.getBytes());
            os.flush();
            // 接收响应消息
            String lineStr = null;
            System.out.println("响应报文 : ");
            while((lineStr = br.readLine()) != null)
            {
                // 读取到了响应信息，表示该ip的port端口提供了HTTP服务
                System.out.println(lineStr);
                flag = true;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(br != null)
                    br.close();
                if(os != null)
                    os.close();
                if(socket != null)
                    socket.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return flag;
    }

    private static boolean isEmpty(String str)
    {
        if(str == null || str.length() <= 0 || str.trim() == null || str.trim().length() <= 0)
        {
            return true;
        }
        return false;
    }
}
