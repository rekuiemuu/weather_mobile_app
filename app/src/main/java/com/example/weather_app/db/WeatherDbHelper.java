package com.example.weather_app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherDbHelper extends SQLiteOpenHelper {

    /* ---------- META ---------- */
    public static final  String DB_NAME = "weather_history.db";
    private static final int    DB_VER  = 2;                 // ↑ увеличили

    /* ---------- TABLE & COLUMNS ---------- */
    public static final String TABLE    = "history";
    public static final String COL_ID   = "_id";             // для CursorAdapter
    public static final String COL_CITY = "city";
    public static final String COL_TEMP = "temperature";
    public static final String COL_TIME = "timestamp";

    public WeatherDbHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                COL_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CITY + " TEXT, " +
                COL_TEMP + " TEXT, " +
                COL_TIME + " INTEGER)");
    }

    /** При изменении версии пересоздаём таблицу (старые данные стираются) */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    /* ---------- CRUD ---------- */

    /** добавить запись */
    public long insert(String city, String temp, long ts) {
        ContentValues cv = new ContentValues();
        cv.put(COL_CITY, city);
        cv.put(COL_TEMP, temp);
        cv.put(COL_TIME, ts);
        return getWritableDatabase().insert(TABLE, null, cv);
    }

    /** все записи, новые сверху */
    public Cursor getAll() {
        return getReadableDatabase().query(TABLE, null, null, null,
                null, null, COL_TIME + " DESC");
    }

    /** удалить по id */
    public void delete(long id) {
        getWritableDatabase().delete(TABLE, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    /** очистить всю историю */
    public void deleteAll() {
        getWritableDatabase().delete(TABLE, null, null);
    }
}
 