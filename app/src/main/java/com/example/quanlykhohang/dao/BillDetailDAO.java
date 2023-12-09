package com.example.quanlykhohang.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlykhohang.database.DbHelper;
import com.example.quanlykhohang.model.BillDetail;

import java.util.ArrayList;
import java.util.List;

public class BillDetailDAO {
    private DbHelper base;

    public BillDetailDAO(Context context) {
        base = new DbHelper(context);
    }

    public List<BillDetail> getALlBillDetail(int id) {
        var list = new ArrayList<BillDetail>();
        var sql = "SELECT BD.CREATEDATE,PD.NAME,BD.QUANTITY,BD.PRICE FROM BILLDETAIL BD\n" +
                "                        JOIN BILL B ON BD.BILLID = B.ID\n" +
                "                        JOIN PRODUCT PD ON BD.IDPRODUCT = PD.ID\n" +
                "                        WHERE B.ID = ?";
        var cursor = base.getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(id)});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(new BillDetail(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                ));
            }
            cursor.close();
        }
        return list;
    }

    public List<BillDetail> getALlBillDetailX(int id) {
        var list = new ArrayList<BillDetail>();
        var sql = "SELECT BD.CREATEDATE,PD.NAME,BD.QUANTITY,BD.PRICEXUAT FROM BILLDETAIL BD\n" +
                "                        JOIN BILL B ON BD.BILLID = B.ID\n" +
                "                        JOIN PRODUCT PD ON BD.IDPRODUCT = PD.ID\n" +
                "                        WHERE B.ID = ?";
        var cursor = base.getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(id)});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(new BillDetail(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3)
                ));
            }
            cursor.close();
        }
        return list;
    }

    public boolean addBillDetail(BillDetail billDetail) {
        var values = new ContentValues();
        values.put("IDPRODUCT", billDetail.getIdProduct());
        values.put("BILLID", billDetail.getIdBill());
        values.put("QUANTITY", billDetail.getQuantity());
        values.put("PRICE", billDetail.getPrice());
        values.put("PRICEXUAT", billDetail.getPriceXuat());
        values.put("CREATEDATE", billDetail.getCreatedDate());
        return base.getWritableDatabase().insert("BILLDETAIL", null, values) > 0;
    }

    public List<BillDetail> getBillDetailsForMonth(String monthYear) {
        SQLiteDatabase db = base.getReadableDatabase();
        List<BillDetail> billDetailsList = new ArrayList<>();

        // Truy vấn cơ sở dữ liệu để lấy thông tin chi tiết hóa đơn cho tháng
        String query = "SELECT BD.CREATEDATE, PD.NAME, BD.QUANTITY, BD.PRICE " +
                "FROM BILLDETAIL BD " +
                "INNER JOIN BILL B ON BD.BILLID = B.ID " +
                "INNER JOIN PRODUCT PD ON BD.IDPRODUCT = PD.ID " +
                "WHERE SUBSTR(B.CREATEDATE, 4, 7) = ? AND  B.QUANTITY = '0'";

        Cursor cursor = db.rawQuery(query, new String[]{monthYear});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String createdDate = cursor.getString(cursor.getColumnIndex("CREATEDATE"));
                @SuppressLint("Range") String productName = cursor.getString(cursor.getColumnIndex("NAME"));
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex("QUANTITY"));
                @SuppressLint("Range") int price = cursor.getInt(cursor.getColumnIndex("PRICE"));

                BillDetail billDetailInfo = new BillDetail(createdDate, productName, String.valueOf(quantity), String.valueOf(price));
                billDetailsList.add(billDetailInfo);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return billDetailsList;
    }

    public List<BillDetail> getBillDetailsForMonthX(String monthYear) {
        SQLiteDatabase db = base.getReadableDatabase();
        List<BillDetail> billDetailsList = new ArrayList<>();

        // Truy vấn cơ sở dữ liệu để lấy thông tin chi tiết hóa đơn cho tháng
        String query = "SELECT BD.CREATEDATE, PD.NAME, BD.QUANTITY, BD.PRICEXUAT " +
                "FROM BILLDETAIL BD " +
                "INNER JOIN BILL B ON BD.BILLID = B.ID " +
                "INNER JOIN PRODUCT PD ON BD.IDPRODUCT = PD.ID " +
                "WHERE SUBSTR(B.CREATEDATE, 4, 7) = ? AND  B.QUANTITY = '1'";

        Cursor cursor = db.rawQuery(query, new String[]{monthYear});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String createdDate = cursor.getString(cursor.getColumnIndex("CREATEDATE"));
                @SuppressLint("Range") String productName = cursor.getString(cursor.getColumnIndex("NAME"));
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex("QUANTITY"));
                @SuppressLint("Range") int price = cursor.getInt(cursor.getColumnIndex("PRICEXUAT"));

                BillDetail billDetailInfo = new BillDetail(createdDate, productName, String.valueOf(quantity), price);
                billDetailsList.add(billDetailInfo);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return billDetailsList;
    }

}
