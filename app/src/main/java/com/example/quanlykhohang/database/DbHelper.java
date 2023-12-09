package com.example.quanlykhohang.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context) {
        super(context, "warehouse", null, 12);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE USER ( ID TEXT PRIMARY KEY," +
                           " NAME TEXT NOT NULL," +
                           " PASSWORD TEXT NOT NULL," +
                           " POSITION TEXT NOT NULL," +
                           " LASTLOGIN TEXT NOT NULL," +
                           " CREATEDATE TEXT NOT NULL," +
                           " LASTACTION TEXT NOT NULL," +
                           "IMAGE TEXT NOT NULL," +
                           "HOMETOWN TEXT NOT NULL)");
        db.execSQL("CREATE TABLE PRODUCT ( ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                           " NAME TEXT NOT NULL," +
                           " PRICE INTEGER NOT NULL," +
                           " QUANTITY INTEGER NOT NULL," +
                           "IMAGE TEXT NOT NULL," +
                           "STORAGE TEXT NOT NULL," +
                           "USERID TEXT REFERENCES USER(ID))");
        db.execSQL("CREATE TABLE BILL ( ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                           " CREATEBYUSER TEXT REFERENCES USER(ID)," +
                           " QUANTITY TEXT NOT NULL," +
                           " CREATEDATE TEXT NOT NULL," +
                           "NOTE TEXT)");
        db.execSQL("CREATE TABLE BILLDETAIL ( IDPRODUCT INTEGER REFERENCES PRODUCT(ID)," +
                           " BILLID INTEGER REFERENCES BILL(ID)," +
                           " QUANTITY TEXT NOT NULL," +
                           " PRICE TEXT NOT NULL," +
                           "PRICEXUAT INTEGER NOT NULL," +
                           " CREATEDATE TEXT NOT NULL)");
        db.execSQL("INSERT INTO USER VALUES('admin','thuc','admin','admin','12-12-2020','12-12-2020','12-12-2020','','Hà Nội')");
        db.execSQL("INSERT INTO PRODUCT VALUES(1,'Sữa tắm',10000,100,'','1', 'admin')," +
                           "(2,'Sữa rửa mặt',20000,100,'','1', 'admin')," +
                           "(3,'Sữa dưỡng da',5000,100,'','1', 'admin')," +
                           "(4,'Sữa bò',5000,100,'','1','admin')");
        db.execSQL("INSERT INTO BILL VALUES(14,'admin','0','08-11-2023','')," +
                           "(13,'admin','1','08-11-2023','')," +
                           "(12,'admin','1','07-11-2023','')," +
                           "(11,'admin','0','07-11-2023','')," +
                           "(10,'admin','0','06-11-2023','')," +
                           "(9,'admin','1','06-11-2023','')," +
                           "(8,'admin','0','05-11-2023','')," +
                           "(7,'admin','1','05-11-2023','')," +
                           "(6,'admin','0','04-11-2023','')," +
                           "(5,'admin','1','04-11-2023','')," +
                           "(4,'admin','1','03-11-2023','')," +
                           "(3,'admin','0','03-11-2023','')," +
                           "(2,'admin','1','02-11-2023','')," +
                           "(1,'admin','0','02-11-2023','')");
        db.execSQL("INSERT INTO BILLDETAIL VALUES(1,14,'1','10000',11000,'08-11-2023')," +
                           "(1,13,'4','10000',11000,'08-11-2023')," +
                           "(2,12,'2','20000',18000,'07-11-2023')," +
                           "(2,11,'2','20000',18000,'07-11-2023')," +
                           "(3,10,'3','5000',7000,'06-11-2023')," +
                           "(3,9,'3','5000',7000,'06-11-2023')," +
                           "(4,8,'4','5000',8000,'05-11-2023')," +
                           "(4,7,'4','5000',8000,'05-11-2023')," +
                           "(1,6,'1','10000',10000,'04-11-2023')," +
                           "(1,5,'1','10000',10000,'04-11-2023')," +
                           "(2,4,'2','20000',18000,'03-11-2023')," +
                           "(2,3,'2','20000',18000,'03-11-2023')," +
                           "(3,2,'3','5000',7000,'02-11-2023')," +
                           "(3,1,'3','5000',7000,'02-11-2023')");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS USER");
            db.execSQL("DROP TABLE IF EXISTS PRODUCT");
            db.execSQL("DROP TABLE IF EXISTS BILL");
            db.execSQL("DROP TABLE IF EXISTS BILLDETAIL");
            onCreate(db);
        }
    }

    @SuppressLint("SimpleDateFormat")
    public void updateLastAction(String userId) {
        var db = this.getWritableDatabase();
        var values = new ContentValues();
        var sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        var currentDateandTime = sdf.format(new Date());
        values.put("LASTACTION", currentDateandTime);
        db.update("USER", values, "ID = ?", new String[]{userId});
        db.close();
    }

    @SuppressLint("SimpleDateFormat")
    public void updateLasLogin(String userId) {
        var db = this.getWritableDatabase();
        var values = new ContentValues();
        var sdfDate = new SimpleDateFormat("dd-MM-yyyy");
        var currentDate = sdfDate.format(new Date());
        values.put("LASTLOGIN", currentDate);
        db.update("USER", values, "ID = ?", new String[]{userId});
        db.close();
    }
}
