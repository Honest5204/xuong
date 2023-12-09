package com.example.quanlykhohang.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlykhohang.database.DbHelper;
import com.example.quanlykhohang.model.ThongKe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ThongKeDAO {
    private DbHelper base;

    public ThongKeDAO(Context context) {
        base = new DbHelper(context);
    }

    public List<ThongKe> getDailyStatsWithinLast7Days() {
        List<ThongKe> dailyStatsList = new ArrayList<>();
        SQLiteDatabase db = base.getReadableDatabase();

        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Tạo đối tượng SimpleDateFormat để định dạng ngày tháng
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        for (int i = 0; i < 7; i++) {
            // Lấy ngày trong khoảng 7 ngày gần đây
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_YEAR, -i);
            Date date = calendar.getTime();

            // Định dạng ngày thành chuỗi
            String dateStr = sdf.format(date);

            // Truy vấn cơ sở dữ liệu để lấy tổng lượng hàng nhập và xuất ra cho ngày hiện tại
            int totalIncoming = getDailyTotalIncoming(db, dateStr);
            int totalOutgoing = getDailyTotalOutgoing(db, dateStr);

            // Tạo đối tượng DailyInventoryStats và thêm vào danh sách
            ThongKe dailyStats = new ThongKe(dateStr, totalIncoming, totalOutgoing);
            dailyStatsList.add(dailyStats);
        }

        db.close();

        return dailyStatsList;
    }

    @SuppressLint("Range")
    private int getDailyTotalIncoming(SQLiteDatabase db, String date) {
        // Truy vấn cơ sở dữ liệu để lấy tổng lượng hàng nhập cho ngày hiện tại
        String query = "SELECT SUM(BD.QUANTITY) AS TOTAL_INCOMING " +
                "FROM BILLDETAIL BD " +
                "INNER JOIN BILL B ON BD.BILLID = B.ID " +
                "WHERE B.CREATEDATE = ? AND B.QUANTITY = '0'";

        Cursor cursor = db.rawQuery(query, new String[]{date});

        int totalIncoming = 0;
        if (cursor.moveToFirst()) {
            totalIncoming = cursor.getInt(cursor.getColumnIndex("TOTAL_INCOMING"));
        }

        cursor.close();

        return totalIncoming;
    }

    @SuppressLint("Range")
    private int getDailyTotalOutgoing(SQLiteDatabase db, String date) {
        // Truy vấn cơ sở dữ liệu để lấy tổng lượng hàng xuất ra cho ngày hiện tại
        String query = "SELECT SUM(BD.QUANTITY) AS TOTAL_OUTGOING " +
                "FROM BILLDETAIL BD " +
                "INNER JOIN BILL B ON BD.BILLID = B.ID " +
                "WHERE B.CREATEDATE = ? AND B.QUANTITY = '1'";

        Cursor cursor = db.rawQuery(query, new String[]{date});

        int totalOutgoing = 0;
        if (cursor.moveToFirst()) {
            totalOutgoing = cursor.getInt(cursor.getColumnIndex("TOTAL_OUTGOING"));
        }

        cursor.close();

        return totalOutgoing;
    }

    public List<ThongKe> getRevenueWithinLast7Days() {
        List<ThongKe> revenueList = new ArrayList<>();
        SQLiteDatabase db = base.getReadableDatabase();

        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Tạo đối tượng SimpleDateFormat để định dạng ngày tháng
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        for (int i = 0; i < 7; i++) {
            // Lấy ngày trong khoảng 7 ngày gần đây
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_YEAR, -i);
            Date date = calendar.getTime();

            // Định dạng ngày thành chuỗi
            String dateStr = sdf.format(date);
            int totalIncoming = (int) getDailyRevenue(db, dateStr);

            // Truy vấn cơ sở dữ liệu để lấy doanh thu cho ngày hiện tại
            ThongKe revenue = new ThongKe(totalIncoming);
            revenueList.add(revenue);
        }

        db.close();

        return revenueList;
    }

    @SuppressLint("Range")
    private double getDailyRevenue(SQLiteDatabase db, String date) {
        // Truy vấn cơ sở dữ liệu để lấy doanh thu cho ngày hiện tại
        String query = "SELECT SUM((BD.PRICEXUAT - P.PRICE) * BD.QUANTITY) AS DAILY_REVENUE " +
                "FROM BILLDETAIL BD " +
                "INNER JOIN BILL B ON BD.BILLID = B.ID " +
                "INNER JOIN PRODUCT P ON BD.IDPRODUCT = P.ID " +
                "WHERE B.CREATEDATE = ? AND B.QUANTITY = '1'";

        Cursor cursor = db.rawQuery(query, new String[]{date});

        double dailyRevenue = 0;
        if (cursor.moveToFirst()) {
            dailyRevenue = cursor.getDouble(cursor.getColumnIndex("DAILY_REVENUE"));
        }

        cursor.close();

        return dailyRevenue;
    }

    public double getTotalRevenueWithinLast7Days() {
        SQLiteDatabase db = base.getReadableDatabase();
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        double totalRevenue = 0.0;

        for (int i = 0; i < 7; i++) {
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_YEAR, -i);
            Date date = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String dateStr = sdf.format(date);
            double dailyRevenue = getDailyRevenue(db, dateStr);
            totalRevenue += dailyRevenue;
        }

        db.close();

        return totalRevenue;
    }

    public List<String> getAllDates() {
        List<String> dateList = new ArrayList<>();
        SQLiteDatabase db = base.getReadableDatabase();

        // Truy vấn cơ sở dữ liệu để lấy tất cả các ngày đã có trong cơ sở dữ liệu
        String query = "SELECT DISTINCT CREATEDATE FROM BILL";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("CREATEDATE"));
                dateList.add(date);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return dateList;
    }

    public List<ThongKe> getStatsBetweenDates(String startDate, String endDate) {
        List<ThongKe> statsList = new ArrayList<>();
        SQLiteDatabase db = base.getReadableDatabase();

        // Truy vấn cơ sở dữ liệu để lấy dữ liệu trong khoảng ngày bắt đầu và ngày kết thúc
        String query = "SELECT B.CREATEDATE AS DATE, " +
                "SUM(CASE WHEN B.QUANTITY = '0' THEN BD.QUANTITY ELSE 0 END) AS TOTAL_INCOMING, " +
                "SUM(CASE WHEN B.QUANTITY = '1' THEN BD.QUANTITY ELSE 0 END) AS TOTAL_OUTGOING, " +
                "SUM(CASE WHEN B.QUANTITY = '1' THEN (BD.PRICEXUAT - P.PRICE) * BD.QUANTITY ELSE 0 END) AS DAILY_REVENUE " +
                "FROM BILL B " +
                "LEFT JOIN BILLDETAIL BD ON B.ID = BD.BILLID " +
                "LEFT JOIN PRODUCT P ON BD.IDPRODUCT = P.ID " +
                "WHERE B.CREATEDATE BETWEEN ? AND ? " +
                "GROUP BY B.CREATEDATE";

        Cursor cursor = db.rawQuery(query, new String[]{startDate, endDate});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("DATE"));
                @SuppressLint("Range") int totalIncoming = cursor.getInt(cursor.getColumnIndex("TOTAL_INCOMING"));
                @SuppressLint("Range") int totalOutgoing = cursor.getInt(cursor.getColumnIndex("TOTAL_OUTGOING"));
                @SuppressLint("Range") double dailyRevenue = cursor.getDouble(cursor.getColumnIndex("DAILY_REVENUE"));
                ThongKe stats = new ThongKe(date, totalIncoming, totalOutgoing, (int) dailyRevenue);
                statsList.add(stats);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return statsList;
    }


    public List<ThongKe> getAllStats() {
        List<ThongKe> statsList = new ArrayList<>();
        SQLiteDatabase db = base.getReadableDatabase();

        // Truy vấn cơ sở dữ liệu để lấy dữ liệu cho tất cả các ngày
        String query = "SELECT B.CREATEDATE AS CREATEDATE, " +
                "SUM(CASE WHEN B.QUANTITY = '0' THEN BD.QUANTITY ELSE 0 END) AS TOTAL_INCOMING, " +
                "SUM(CASE WHEN B.QUANTITY = '1' THEN BD.QUANTITY ELSE 0 END) AS TOTAL_OUTGOING, " +
                "SUM(CASE WHEN B.QUANTITY = '1' THEN (BD.PRICEXUAT - P.PRICE) * BD.QUANTITY ELSE 0 END) AS DAILY_REVENUE " +
                "FROM BILL B " +
                "LEFT JOIN BILLDETAIL BD ON B.ID = BD.BILLID " +
                "LEFT JOIN PRODUCT P ON BD.IDPRODUCT = P.ID " +
                "GROUP BY B.CREATEDATE";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String createdDate = cursor.getString(cursor.getColumnIndex("CREATEDATE"));
                @SuppressLint("Range") int totalIncoming = cursor.getInt(cursor.getColumnIndex("TOTAL_INCOMING"));
                @SuppressLint("Range") int totalOutgoing = cursor.getInt(cursor.getColumnIndex("TOTAL_OUTGOING"));
                @SuppressLint("Range") double dailyRevenue = cursor.getDouble(cursor.getColumnIndex("DAILY_REVENUE"));
                ThongKe stats = new ThongKe(createdDate, totalIncoming, totalOutgoing, (int) dailyRevenue);
                statsList.add(stats);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return statsList;
    }


    public ThongKe getStatsForDate(String date) {
        SQLiteDatabase db = base.getReadableDatabase();
        ThongKe stats = null;

        // Truy vấn cơ sở dữ liệu để lấy dữ liệu cho một ngày cụ thể
        String query = "SELECT B.CREATEDATE AS DATE, " +
                "SUM(CASE WHEN B.QUANTITY = '0' THEN BD.QUANTITY ELSE 0 END) AS TOTAL_INCOMING, " +
                "SUM(CASE WHEN B.QUANTITY = '1' THEN BD.QUANTITY ELSE 0 END) AS TOTAL_OUTGOING, " +
                "SUM(CASE WHEN B.QUANTITY = '1' THEN (BD.PRICEXUAT - P.PRICE) * BD.QUANTITY ELSE 0 END) AS DAILY_REVENUE " +
                "FROM BILL B " +
                "LEFT JOIN BILLDETAIL BD ON B.ID = BD.BILLID " +
                "LEFT JOIN PRODUCT P ON BD.IDPRODUCT = P.ID " +
                "WHERE B.CREATEDATE = ? " +
                "GROUP BY B.CREATEDATE";

        Cursor cursor = db.rawQuery(query, new String[]{date});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String dateStr = cursor.getString(cursor.getColumnIndex("DATE"));
            @SuppressLint("Range") int totalIncoming = cursor.getInt(cursor.getColumnIndex("TOTAL_INCOMING"));
            @SuppressLint("Range") int totalOutgoing = cursor.getInt(cursor.getColumnIndex("TOTAL_OUTGOING"));
            @SuppressLint("Range") double dailyRevenue = cursor.getDouble(cursor.getColumnIndex("DAILY_REVENUE"));
            stats = new ThongKe(dateStr, totalIncoming, totalOutgoing, (int) dailyRevenue);
        }

        cursor.close();
        db.close();

        return stats;
    }

    @SuppressLint("Range")
    public int getBillIdByCreatedDate(String createdDate) {
        SQLiteDatabase db = base.getReadableDatabase();
        int billId = -1;  // Giá trị mặc định nếu không tìm thấy ID

        String query = "SELECT B.ID AS BILL_ID " +
                "FROM BILL B " +
                "WHERE B.CREATEDATE = ?";
        Cursor cursor = db.rawQuery(query, new String[]{createdDate});

        if (cursor.moveToFirst()) {
            billId = cursor.getInt(cursor.getColumnIndex("BILL_ID"));
        }

        cursor.close();
        db.close();

        return billId;
    }

    public List<ThongKe> getStatsForLast4Months() {
        List<ThongKe> statsList = new ArrayList<>();
        SQLiteDatabase db = base.getReadableDatabase();

        // Lấy tháng và năm hiện tại
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        for (int i = 0; i < 4; i++) {
            // Lấy tháng và năm trong khoảng 4 tháng gần đây
            int targetMonth = (currentMonth - i + 12) % 12 + 1;  // Bắt đầu từ tháng hiện tại và trừ đi các tháng trước đó
            int targetYear = currentYear;
            if (currentMonth - i < 0) {
                targetYear--;
            }

            // Định dạng tháng và năm thành chuỗi
            String monthYearStr = String.format("%02d-%04d", targetMonth, targetYear);

            // Truy vấn cơ sở dữ liệu để lấy tổng lượng hàng nhập và xuất, doanh thu cho tháng hiện tại
            int totalIncoming = getMonthlyTotalIncoming(db, targetMonth, targetYear);
            int totalOutgoing = getMonthlyTotalOutgoing(db, targetMonth, targetYear);

            // Tạo đối tượng ThongKe và thêm vào danh sách
            ThongKe stats = new ThongKe(monthYearStr, totalIncoming, totalOutgoing);
            statsList.add(stats);
        }

        db.close();

        return statsList;
    }

    @SuppressLint("Range")
    public int getMonthlyTotalIncoming(SQLiteDatabase db, int month, int year) {
        // Truy vấn cơ sở dữ liệu để lấy tổng lượng hàng nhập cho tháng và năm cụ thể
        String query = "SELECT SUM(BD.QUANTITY) AS TOTAL_INCOMING " +
                "FROM BILLDETAIL BD " +
                "INNER JOIN BILL B ON BD.BILLID = B.ID " +
                "WHERE SUBSTR(B.CREATEDATE, 4, 7) = ? AND B.QUANTITY = '0'";

        Cursor cursor = db.rawQuery(query, new String[]{String.format("%02d-%04d", month, year)});

        int totalIncoming = 0;
        if (cursor.moveToFirst()) {
            totalIncoming = cursor.getInt(cursor.getColumnIndex("TOTAL_INCOMING"));
        }

        cursor.close();

        return totalIncoming;
    }

    @SuppressLint("Range")
    public int getMonthlyTotalOutgoing(SQLiteDatabase db, int month, int year) {
        // Truy vấn cơ sở dữ liệu để lấy tổng lượng hàng xuất cho tháng và năm cụ thể
        String query = "SELECT SUM(BD.QUANTITY) AS TOTAL_OUTGOING " +
                "FROM BILLDETAIL BD " +
                "INNER JOIN BILL B ON BD.BILLID = B.ID " +
                "WHERE SUBSTR(B.CREATEDATE, 4, 7) = ? AND B.QUANTITY = '1'";

        Cursor cursor = db.rawQuery(query, new String[]{String.format("%02d-%04d", month, year)});

        int totalOutgoing = 0;
        if (cursor.moveToFirst()) {
            totalOutgoing = cursor.getInt(cursor.getColumnIndex("TOTAL_OUTGOING"));
        }

        cursor.close();

        return totalOutgoing;
    }

    @SuppressLint("Range")
    public double getMonthlyRevenue(SQLiteDatabase db, int month, int year) {
        // Truy vấn cơ sở dữ liệu để lấy doanh thu cho tháng và năm cụ thể
        String query = "SELECT SUM((BD.PRICEXUAT - P.PRICE) * BD.QUANTITY) AS MONTHLY_REVENUE " +
                "FROM BILLDETAIL BD " +
                "INNER JOIN BILL B ON BD.BILLID = B.ID " +
                "INNER JOIN PRODUCT P ON BD.IDPRODUCT = P.ID " +
                "WHERE SUBSTR(B.CREATEDATE, 4, 7) = ? AND B.QUANTITY = '1'";

        Cursor cursor = db.rawQuery(query, new String[]{String.format("%02d-%04d", month, year)});

        double monthlyRevenue = 0;
        if (cursor.moveToFirst()) {
            monthlyRevenue = cursor.getDouble(cursor.getColumnIndex("MONTHLY_REVENUE"));
        }

        cursor.close();

        return monthlyRevenue;
    }

    public List<ThongKe> getMonthlyStatsWithinLast4Months() {
        List<ThongKe> monthlyStatsList = new ArrayList<>();
        SQLiteDatabase db = base.getReadableDatabase();

        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Tạo đối tượng SimpleDateFormat để định dạng ngày tháng
        SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");

        for (int i = 0; i < 4; i++) {
            // Lấy tháng trong khoảng 4 tháng gần đây
            calendar.setTime(currentDate);
            calendar.add(Calendar.MONTH, -i);
            Date date = calendar.getTime();

            // Định dạng ngày thành chuỗi
            String monthYearStr = sdf.format(date);

            // Truy vấn cơ sở dữ liệu để lấy doanh thu cho tháng hiện tại
            double monthlyRevenue = getMonthlyRevenue(db, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

            // Tạo đối tượng MonthlyRevenueStats và thêm vào danh sách
            ThongKe monthlyStats = new ThongKe(monthYearStr, 0, 0, (int) monthlyRevenue);
            monthlyStatsList.add(monthlyStats);
        }

        db.close();

        return monthlyStatsList;
    }

    public double getTotalMonthlyRevenueWithinLast4Months() {
        SQLiteDatabase db = base.getReadableDatabase();
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        double totalMonthlyRevenue = 0.0;

        for (int i = 0; i < 4; i++) {
            calendar.setTime(currentDate);
            calendar.add(Calendar.MONTH, -i);
            Date date = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
            String monthYearStr = sdf.format(date);
            double monthlyRevenue = getMonthlyRevenue(db, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
            totalMonthlyRevenue += monthlyRevenue;
        }

        db.close();

        return totalMonthlyRevenue;
    }

    public ThongKe getStatsForMonth(String monthYear) {
        SQLiteDatabase db = base.getReadableDatabase();
        ThongKe stats = null;

        // Truy vấn cơ sở dữ liệu để lấy dữ liệu cho một tháng cụ thể
        String query = "SELECT " +
                "SUM(CASE WHEN B.QUANTITY = '0' THEN BD.QUANTITY ELSE 0 END) AS TOTAL_INCOMING, " +
                "SUM(CASE WHEN B.QUANTITY = '1' THEN BD.QUANTITY ELSE 0 END) AS TOTAL_OUTGOING, " +
                "SUM(CASE WHEN B.QUANTITY = '1' THEN (BD.PRICEXUAT - P.PRICE) * BD.QUANTITY ELSE 0 END) AS MONTHLY_REVENUE " +
                "FROM BILL B " +
                "LEFT JOIN BILLDETAIL BD ON B.ID = BD.BILLID " +
                "LEFT JOIN PRODUCT P ON BD.IDPRODUCT = P.ID " +
                "WHERE SUBSTR(B.CREATEDATE, 4, 7) = ? " +
                "GROUP BY SUBSTR(B.CREATEDATE, 4, 7)";

        Cursor cursor = db.rawQuery(query, new String[]{monthYear});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int totalIncoming = cursor.getInt(cursor.getColumnIndex("TOTAL_INCOMING"));
            @SuppressLint("Range") int totalOutgoing = cursor.getInt(cursor.getColumnIndex("TOTAL_OUTGOING"));
            @SuppressLint("Range") double monthlyRevenue = cursor.getDouble(cursor.getColumnIndex("MONTHLY_REVENUE"));
            stats = new ThongKe(monthYear, totalIncoming, totalOutgoing, (int) monthlyRevenue);
        }

        cursor.close();
        db.close();

        return stats;
    }

    public List<ThongKe> getStatsBetweenMonths(String startMonthYear, String endMonthYear) {
        List<ThongKe> statsList = new ArrayList<>();
        SQLiteDatabase db = base.getReadableDatabase();

        // Truy vấn cơ sở dữ liệu để lấy dữ liệu trong khoảng tháng bắt đầu và tháng kết thúc
        String query = "SELECT SUBSTR(B.CREATEDATE, 4, 7) AS MONTH_YEAR, " +
                "SUM(CASE WHEN B.QUANTITY = '0' THEN BD.QUANTITY ELSE 0 END) AS TOTAL_INCOMING, " +
                "SUM(CASE WHEN B.QUANTITY = '1' THEN BD.QUANTITY ELSE 0 END) AS TOTAL_OUTGOING, " +
                "SUM(CASE WHEN B.QUANTITY = '1' THEN (BD.PRICEXUAT - P.PRICE) * BD.QUANTITY ELSE 0 END) AS MONTHLY_REVENUE " +
                "FROM BILL B " +
                "LEFT JOIN BILLDETAIL BD ON B.ID = BD.BILLID " +
                "LEFT JOIN PRODUCT P ON BD.IDPRODUCT = P.ID " +
                "WHERE SUBSTR(B.CREATEDATE, 4, 7) BETWEEN ? AND ? " +
                "GROUP BY SUBSTR(B.CREATEDATE, 4, 7)";

        Cursor cursor = db.rawQuery(query, new String[]{startMonthYear, endMonthYear});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String monthYear = cursor.getString(cursor.getColumnIndex("MONTH_YEAR"));
                @SuppressLint("Range") int totalIncoming = cursor.getInt(cursor.getColumnIndex("TOTAL_INCOMING"));
                @SuppressLint("Range") int totalOutgoing = cursor.getInt(cursor.getColumnIndex("TOTAL_OUTGOING"));
                @SuppressLint("Range") double monthlyRevenue = cursor.getDouble(cursor.getColumnIndex("MONTHLY_REVENUE"));
                ThongKe stats = new ThongKe(monthYear, totalIncoming, totalOutgoing, (int) monthlyRevenue);
                statsList.add(stats);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return statsList;
    }

    public List<ThongKe> getAllMonthlyStats() {
        List<ThongKe> statsList = new ArrayList<>();
        SQLiteDatabase db = base.getReadableDatabase();

        // Truy vấn cơ sở dữ liệu để lấy dữ liệu cho tất cả các tháng
        String query = "SELECT SUBSTR(B.CREATEDATE, 4, 7) AS MONTH_YEAR, " +
                "SUM(CASE WHEN B.QUANTITY = '0' THEN BD.QUANTITY ELSE 0 END) AS TOTAL_INCOMING, " +
                "SUM(CASE WHEN B.QUANTITY = '1' THEN BD.QUANTITY ELSE 0 END) AS TOTAL_OUTGOING, " +
                "SUM(CASE WHEN B.QUANTITY = '1' THEN (BD.PRICEXUAT - P.PRICE) * BD.QUANTITY ELSE 0 END) AS MONTHLY_REVENUE " +
                "FROM BILL B " +
                "LEFT JOIN BILLDETAIL BD ON B.ID = BD.BILLID " +
                "LEFT JOIN PRODUCT P ON BD.IDPRODUCT = P.ID " +
                "GROUP BY SUBSTR(B.CREATEDATE, 4, 7)";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String monthYear = cursor.getString(cursor.getColumnIndex("MONTH_YEAR"));
                @SuppressLint("Range") int totalIncoming = cursor.getInt(cursor.getColumnIndex("TOTAL_INCOMING"));
                @SuppressLint("Range") int totalOutgoing = cursor.getInt(cursor.getColumnIndex("TOTAL_OUTGOING"));
                @SuppressLint("Range") double monthlyRevenue = cursor.getDouble(cursor.getColumnIndex("MONTHLY_REVENUE"));
                ThongKe stats = new ThongKe(monthYear, totalIncoming, totalOutgoing, (int) monthlyRevenue);
                statsList.add(stats);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return statsList;
    }

    @SuppressLint("Range")
    public int getYearlyTotalIncoming(int year) {
        SQLiteDatabase db = base.getReadableDatabase();
        int totalIncoming = 0;

        // Truy vấn cơ sở dữ liệu để lấy tổng lượng hàng nhập cho một năm cụ thể
        String query = "SELECT SUM(BD.QUANTITY) AS TOTAL_INCOMING " +
                "FROM BILLDETAIL BD " +
                "INNER JOIN BILL B ON BD.BILLID = B.ID " +
                "WHERE SUBSTR(B.CREATEDATE, 7, 4) = ? AND B.QUANTITY = '0'";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(year)});

        if (cursor.moveToFirst()) {
            totalIncoming = cursor.getInt(cursor.getColumnIndex("TOTAL_INCOMING"));
        }

        cursor.close();
        db.close();

        return totalIncoming;
    }

    @SuppressLint("Range")
    public int getYearlyTotalOutgoing(int year) {
        SQLiteDatabase db = base.getReadableDatabase();
        int totalOutgoing = 0;

        // Truy vấn cơ sở dữ liệu để lấy tổng lượng hàng xuất ra cho một năm cụ thể
        String query = "SELECT SUM(BD.QUANTITY) AS TOTAL_OUTGOING " +
                "FROM BILLDETAIL BD " +
                "INNER JOIN BILL B ON BD.BILLID = B.ID " +
                "WHERE SUBSTR(B.CREATEDATE, 7, 4) = ? AND B.QUANTITY = '1'";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(year)});

        if (cursor.moveToFirst()) {
            totalOutgoing = cursor.getInt(cursor.getColumnIndex("TOTAL_OUTGOING"));
        }

        cursor.close();
        db.close();

        return totalOutgoing;
    }

    @SuppressLint("Range")
    public double getYearlyRevenue(int year) {
        SQLiteDatabase db = base.getReadableDatabase();
        double yearlyRevenue = 0;

        // Truy vấn cơ sở dữ liệu để lấy doanh thu cho một năm cụ thể
        String query = "SELECT SUM((BD.PRICEXUAT - P.PRICE) * BD.QUANTITY) AS YEARLY_REVENUE " +
                "FROM BILLDETAIL BD " +
                "INNER JOIN BILL B ON BD.BILLID = B.ID " +
                "INNER JOIN PRODUCT P ON BD.IDPRODUCT = P.ID " +
                "WHERE SUBSTR(B.CREATEDATE, 7, 4) = ? AND B.QUANTITY = '1'";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(year)});

        if (cursor.moveToFirst()) {
            yearlyRevenue = cursor.getDouble(cursor.getColumnIndex("YEARLY_REVENUE"));
        }

        cursor.close();
        db.close();

        return yearlyRevenue;
    }

    public List<ThongKe> getYearlyStatsWithinLast4Years() {
        List<ThongKe> yearlyStatsList = new ArrayList<>();
        SQLiteDatabase db = base.getReadableDatabase();

        // Lấy năm hiện tại
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        for (int i = 0; i < 4; i++) {
            // Lấy năm trong khoảng 4 năm gần đây
            int year = currentYear - i;

            // Lấy tổng lượng hàng nhập và xuất và doanh thu cho năm hiện tại
            int totalIncoming = getYearlyTotalIncoming(year);
            int totalOutgoing = getYearlyTotalOutgoing(year);

            // Tạo đối tượng ThongKe và thêm vào danh sách
            ThongKe yearlyStats = new ThongKe(String.valueOf(year), totalIncoming, totalOutgoing);
            yearlyStatsList.add(yearlyStats);
        }

        db.close();

        return yearlyStatsList;
    }

    public double getTotalYearlyRevenueWithinLast4Years() {
        SQLiteDatabase db = base.getReadableDatabase();
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        double totalYearlyRevenue = 0.0;

        for (int i = 0; i < 4; i++) {
            int year = currentYear - i;
            double yearlyRevenue = getYearlyRevenue(year);
            totalYearlyRevenue += yearlyRevenue;
        }

        db.close();

        return totalYearlyRevenue;
    }

    public List<ThongKe> getStatsForYear(int year) {
        List<ThongKe> statsList = new ArrayList<>();
        SQLiteDatabase db = base.getReadableDatabase();

        // Truy vấn cơ sở dữ liệu để lấy dữ liệu cho năm cụ thể
        String query = "SELECT B.CREATEDATE AS DATE, " +
                "SUM(CASE WHEN B.QUANTITY = '0' THEN BD.QUANTITY ELSE 0 END) AS TOTAL_INCOMING, " +
                "SUM(CASE WHEN B.QUANTITY = '1' THEN BD.QUANTITY ELSE 0 END) AS TOTAL_OUTGOING, " +
                "SUM(CASE WHEN B.QUANTITY = '1' THEN (BD.PRICEXUAT - P.PRICE) * BD.QUANTITY ELSE 0 END) AS DAILY_REVENUE " +
                "FROM BILL B " +
                "LEFT JOIN BILLDETAIL BD ON B.ID = BD.BILLID " +
                "LEFT JOIN PRODUCT P ON BD.IDPRODUCT = P.ID " +
                "WHERE SUBSTR(B.CREATEDATE, 7, 4) = ? " +
                "GROUP BY B.CREATEDATE";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(year)});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("DATE"));
                @SuppressLint("Range") int totalIncoming = cursor.getInt(cursor.getColumnIndex("TOTAL_INCOMING"));
                @SuppressLint("Range") int totalOutgoing = cursor.getInt(cursor.getColumnIndex("TOTAL_OUTGOING"));
                @SuppressLint("Range") double dailyRevenue = cursor.getDouble(cursor.getColumnIndex("DAILY_REVENUE"));
                ThongKe stats = new ThongKe(date, totalIncoming, totalOutgoing, (int) dailyRevenue);
                statsList.add(stats);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return statsList;
    }

    public List<ThongKe> getAllYearlyStats() {
        List<ThongKe> statsList = new ArrayList<>();
        SQLiteDatabase db = base.getReadableDatabase();

        // Truy vấn cơ sở dữ liệu để lấy dữ liệu cho tất cả các năm
        String query = "SELECT B.CREATEDATE AS CREATEDATE, " +
                "SUM(CASE WHEN B.QUANTITY = '0' THEN BD.QUANTITY ELSE 0 END) AS TOTAL_INCOMING, " +
                "SUM(CASE WHEN B.QUANTITY = '1' THEN BD.QUANTITY ELSE 0 END) AS TOTAL_OUTGOING, " +
                "SUM(CASE WHEN B.QUANTITY = '1' THEN (BD.PRICEXUAT - P.PRICE) * BD.QUANTITY ELSE 0 END) AS DAILY_REVENUE " +
                "FROM BILL B " +
                "LEFT JOIN BILLDETAIL BD ON B.ID = BD.BILLID " +
                "LEFT JOIN PRODUCT P ON BD.IDPRODUCT = P.ID " +
                "GROUP BY SUBSTR(B.CREATEDATE, 7, 4)";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String createdDate = cursor.getString(cursor.getColumnIndex("CREATEDATE"));
                @SuppressLint("Range") int totalIncoming = cursor.getInt(cursor.getColumnIndex("TOTAL_INCOMING"));
                @SuppressLint("Range") int totalOutgoing = cursor.getInt(cursor.getColumnIndex("TOTAL_OUTGOING"));
                @SuppressLint("Range") double dailyRevenue = cursor.getDouble(cursor.getColumnIndex("DAILY_REVENUE"));
                ThongKe stats = new ThongKe(createdDate, totalIncoming, totalOutgoing, (int) dailyRevenue);
                statsList.add(stats);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return statsList;
    }

    public List<ThongKe> getStatsBetweenYears(int startYear, int endYear) {
        List<ThongKe> statsList = new ArrayList<>();
        SQLiteDatabase db = base.getReadableDatabase();

        // Truy vấn cơ sở dữ liệu để lấy dữ liệu trong khoảng năm bắt đầu và năm kết thúc
        String query = "SELECT B.CREATEDATE AS DATE, " +
                "SUM(CASE WHEN B.QUANTITY = '0' THEN BD.QUANTITY ELSE 0 END) AS TOTAL_INCOMING, " +
                "SUM(CASE WHEN B.QUANTITY = '1' THEN BD.QUANTITY ELSE 0 END) AS TOTAL_OUTGOING, " +
                "SUM(CASE WHEN B.QUANTITY = '1' THEN (BD.PRICEXUAT - P.PRICE) * BD.QUANTITY ELSE 0 END) AS DAILY_REVENUE " +
                "FROM BILL B " +
                "LEFT JOIN BILLDETAIL BD ON B.ID = BD.BILLID " +
                "LEFT JOIN PRODUCT P ON BD.IDPRODUCT = P.ID " +
                "WHERE SUBSTR(B.CREATEDATE, 7, 4) BETWEEN ? AND ? " +
                "GROUP BY B.CREATEDATE";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(startYear), String.valueOf(endYear)});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("DATE"));
                @SuppressLint("Range") int totalIncoming = cursor.getInt(cursor.getColumnIndex("TOTAL_INCOMING"));
                @SuppressLint("Range") int totalOutgoing = cursor.getInt(cursor.getColumnIndex("TOTAL_OUTGOING"));
                @SuppressLint("Range") double dailyRevenue = cursor.getDouble(cursor.getColumnIndex("DAILY_REVENUE"));
                ThongKe stats = new ThongKe(date, totalIncoming, totalOutgoing, (int) dailyRevenue);
                statsList.add(stats);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return statsList;
    }

    public List<Integer> getBillIdsForMonth(String monthYear) {
        SQLiteDatabase db = base.getReadableDatabase();
        List<Integer> billIds = new ArrayList<>();

        // Truy vấn cơ sở dữ liệu để lấy ID của các hóa đơn trong tháng
        String query = "SELECT B.ID AS BILL_ID " +
                "FROM BILL B " +
                "WHERE SUBSTR(B.CREATEDATE, 4, 7) = ?";

        Cursor cursor = db.rawQuery(query, new String[]{monthYear});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int billId = cursor.getInt(cursor.getColumnIndex("BILL_ID"));
                billIds.add(billId);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return billIds;
    }


}

