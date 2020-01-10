package com.gyq.fengshui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "fengshui.db"; //数据库名称
    private static final int version = 1; //数据库版本
    private final static String TABLE_NAME = "baodi";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE_NAME + "(id varchar(36) not null," +
                "name varchar(60)," +
                "ct varchar(23)," +
                "note varchar(100)," +
                "shan01 varchar(3)," +
                "shan02 varchar(3)," +
                "shan03 varchar(3)," +
                "shan04 varchar(3)," +
                "shan05 varchar(3)," +
                "shan06 varchar(3)," +
                "shan07 varchar(3)," +
                "shan08 varchar(3)," +
                "shan09 varchar(3)," +
                "shan10 varchar(3)," +
                "shan11 varchar(3)," +
                "shan12 varchar(3)," +
                "shan13 varchar(3)," +
                "shan14 varchar(3)," +
                "shan15 varchar(3)," +
                "shan16 varchar(3)," +
                "shan17 varchar(3)," +
                "shan18 varchar(3)," +
                "shan19 varchar(3)," +
                "shan20 varchar(3)," +
                "shan21 varchar(3)," +
                "shan22 varchar(3)," +
                "shan23 varchar(3)," +
                "shan24 varchar(3)," +
                "shui01 varchar(3)," +
                "shui02 varchar(3)," +
                "shui03 varchar(3)," +
                "shui04 varchar(3)," +
                "shui05 varchar(3)," +
                "shui06 varchar(3)," +
                "shui07 varchar(3)," +
                "shui08 varchar(3)," +
                "shui09 varchar(3)," +
                "shui10 varchar(3)," +
                "shui11 varchar(3)," +
                "shui12 varchar(3)," +
                "shui13 varchar(3)," +
                "shui14 varchar(3)," +
                "shui15 varchar(3)," +
                "shui16 varchar(3)," +
                "shui17 varchar(3)," +
                "shui18 varchar(3)," +
                "shui19 varchar(3)," +
                "shui20 varchar(3)," +
                "shui21 varchar(3)," +
                "shui22 varchar(3)," +
                "shui23 varchar(3)," +
                "shui24 varchar(3))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);

    }

    public Cursor select() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db
                .query(TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    //增加操作
    public long insert(String id1, String name1, String note1, String[] shan, String[] shui) {
        SQLiteDatabase db = this.getWritableDatabase();
/* ContentValues */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
//获取当前时间
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        ContentValues cv = new ContentValues();
        cv.put("id", id1);
        cv.put("name", name1);
        cv.put("note", note1);
        Date date = new Date(System.currentTimeMillis());
        cv.put("ct", simpleDateFormat.format(date));
        for (int i = 1; i <= 24; i++) {
            if (i <= shan.length) {
                if (i < 10)
                    cv.put("shan0" + i, shan[i - 1]);
                else
                    cv.put("shan" + i, shan[i - 1]);
            } else {
                if (i < 10)
                    cv.put("shan0" + i, "0");
                else
                    cv.put("shan" + i, "0");
            }
        }
        for (int i = 1; i <= 24; i++) {
            if (i <= shui.length) {
                if (i < 10)
                    cv.put("shui0" + i, shui[i - 1]);
                else
                    cv.put("shui" + i, shui[i - 1]);
            } else {
                if (i < 10)
                    cv.put("shui0" + i, "0");
                else
                    cv.put("shui" + i, "0");
            }
        }
        long row = db.insert(TABLE_NAME, null, cv);
        return row;
    }

    //删除操作
    public void delete(String id1) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "id = ?";
        String[] whereValue = {id1};
        db.delete(TABLE_NAME, where, whereValue);
    }

    //修改操作
    public void update(String id1, String fname, String fvalue) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "id = ?";
        String[] whereValue = {id1};

        ContentValues cv = new ContentValues();
        cv.put(fname, fvalue);
        db.update(TABLE_NAME, cv, where, whereValue);
    }

    public Cursor select(String id1, String[] fn) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] whereValue = {id1};
        Cursor cursor = db.query(TABLE_NAME, fn, "id=?", whereValue, null, null, null);
        return cursor;
    }

    public Cursor select(String[] fn) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, fn, null, null, null, null, null);
        return cursor;
    }

    public Cursor lselect(String key, String[] fn) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, fn, "name like ?", new String[]{"%" + key + "%"}, null, null, null);
//        Cursor cursor = db.rawQuery("select " + fn.toString() + " from " + TABLE_NAME + " where name like ? or ct like ?", new String[]{"%" + key + "%", "%" + key + "%"});
        return cursor;
    }
    public Cursor lselect(String key, String fd, String[] fn) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, fn, fd + " like ?", new String[]{"%" + key + "%"}, null, null, null);
//        Cursor cursor = db.rawQuery("select " + fn.toString() + " from " + TABLE_NAME + " where name like ? or ct like ?", new String[]{"%" + key + "%", "%" + key + "%"});
        return cursor;
    }
}