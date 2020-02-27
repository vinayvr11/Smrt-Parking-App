package org.tensorflow.lite.examples.detection;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {


    public static String DATABASE_NAME = "parkingData.db";
    public static String TABLE_NAME = "parkingInfo";
    public static String COL_0 = "parkingId";
    public static String COL_1 = "parkingName";
    public static String COL_2 = "parkingRent";
    public static String COL_3 = "parkingCapacity";


    public static String query = "create table "+TABLE_NAME+"( parkingName VARCHAR(100),parkingRent int,parkingCapacity int)";

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String parkingName,String parkingRent,String parkingCapacity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,parkingName);
        contentValues.put(COL_2,parkingRent);
        contentValues.put(COL_3,parkingCapacity);

        Long take = db.insert(TABLE_NAME,null,contentValues);
        if(take == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select  * from "+TABLE_NAME +" where ParkingName = 'Noida parking' ",null);
        return res;
    }



}
