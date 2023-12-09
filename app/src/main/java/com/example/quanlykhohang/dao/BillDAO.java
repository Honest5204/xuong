package com.example.quanlykhohang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlykhohang.database.DbHelper;
import com.example.quanlykhohang.model.Bill;

import java.util.ArrayList;
import java.util.List;

public class BillDAO {
    private DbHelper base;

    public BillDAO(Context context) {
        base = new DbHelper(context);
    }

    public ArrayList<Bill> getALlBill() {
        var list = new ArrayList<Bill>();
        var sql = "SELECT * FROM BILL WHERE QUANTITY = '0'";
        var cursor = base.getReadableDatabase().rawQuery(sql, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(new Bill(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            }
            cursor.close();
        }
        return list;
    }
    public ArrayList<Bill> getALlBillX() {
        var list = new ArrayList<Bill>();
        var sql = "SELECT * FROM BILL WHERE QUANTITY = '1'";
        var cursor = base.getReadableDatabase().rawQuery(sql, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(new Bill(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            }
            cursor.close();
        }
        return list;
    }

    public int getLatestBillId() {
        int latestBillId = -1;  // Giá trị mặc định nếu không tìm thấy Bill nào
        SQLiteDatabase db = base.getReadableDatabase();
        String query = "SELECT MAX(ID) FROM BILL";  // Truy vấn để lấy ID Bill lớn nhất
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            latestBillId = cursor.getInt(0);  // Lấy giá trị ID Bill lớn nhất
        }
        cursor.close();
        return latestBillId;
    }

    public boolean insertBill(Bill bill) {
        var values = new ContentValues();
        values.put("QUANTITY", bill.getQuantity());
        values.put("CREATEBYUSER", bill.getCreatedByUser());
        values.put("CREATEDATE", bill.getCreatedDate());
        values.put("NOTE", bill.getNote());
        return base.getWritableDatabase().insert("BILL", null, values) > 0;
    }
    public boolean deleteBill(int id){
        var values = new ContentValues();
        values.put("ID",id);
        return base.getWritableDatabase().delete("BILL","ID = ?",new String[]{String.valueOf(id)}) > 0;
    }
    public boolean updateBill(Bill bill){
        var values = new ContentValues();
        values.put("ID",bill.getId());
        values.put("QUANTITY",bill.getQuantity());
        values.put("CREATEBYUSER",bill.getCreatedByUser());
        values.put("CREATEDATE",bill.getCreatedDate());
        values.put("NOTE",bill.getNote());
        return base.getWritableDatabase().update("BILL",values,"ID = ?",new String[]{String.valueOf(bill.getId())}) > 0;
    }
}
