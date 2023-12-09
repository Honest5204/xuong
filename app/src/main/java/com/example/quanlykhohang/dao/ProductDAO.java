package com.example.quanlykhohang.dao;

import android.content.ContentValues;
import android.content.Context;

import com.example.quanlykhohang.database.DbHelper;
import com.example.quanlykhohang.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private DbHelper base;

    public ProductDAO(Context context) {
        base = new DbHelper(context);
    }

    public List<Product> getALlProduct(String storage) {
        var list = new ArrayList<Product>();
        var cursor = base.getReadableDatabase().rawQuery("SELECT * FROM PRODUCT WHERE STORAGE = ?", new String[]{storage});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(new Product(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                ));
            }
            cursor.close();
        }
        return list;
    }

    public boolean insertProduct(Product product) {
        var values = new ContentValues();
        values.put("NAME", product.getName());
        values.put("QUANTITY", product.getQuantity());
        values.put("PRICE", product.getPrice());
        values.put("IMAGE", product.getPhoto());
        values.put("STORAGE", product.getStorage());
        values.put("USERID", product.getUserID());
        return base.getWritableDatabase().insert("PRODUCT", null, values) > 0;
    }
    public boolean updateProduct(Product product, int id) {
        var values = new ContentValues();
        values.put("NAME", product.getName());
        values.put("QUANTITY", product.getQuantity());
        values.put("PRICE", product.getPrice());
        values.put("IMAGE", product.getPhoto());
        values.put("USERID", product.getUserID());
        return base.getWritableDatabase().update("PRODUCT", values, "ID = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean updateProductBill(int product, int id) {
        var values = new ContentValues();
        values.put("QUANTITY", product);
        return base.getWritableDatabase().update("PRODUCT", values, "ID = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean updateProductStorage(String storage, int id) {
        var values = new ContentValues();
        values.put("STORAGE", storage);
        return base.getWritableDatabase().update("PRODUCT", values, "ID = ?", new String[]{String.valueOf(id)}) > 0;
    }
}
