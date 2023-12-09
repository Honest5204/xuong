package com.example.quanlykhohang.Fragment.FraThongKe;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quanlykhohang.Adapter.ThongkeADT.ThongkeAdapter;
import com.example.quanlykhohang.Interface.MenuController;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.dao.ThongKeDAO;
import com.example.quanlykhohang.databinding.FragmentThongkeNgayBinding;
import com.example.quanlykhohang.model.ThongKe;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ThongKeNgayFragment extends Fragment {
    public static final String TAG = ThongKeNgayFragment.class.getName();
    private FragmentThongkeNgayBinding binding;
    private ThongKeDAO dao;

    public ThongKeNgayFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_thongke_ngay, container, false);
        binding = FragmentThongkeNgayBinding.bind(view);
        setHasOptionsMenu(true);
        initActionBar();
        var calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        var thongKe = new ThongKeDAO(requireContext());
        var dailyStatsList = thongKe.getDailyStatsWithinLast7Days();
        var dailyRevenues = thongKe.getRevenueWithinLast7Days();
        var totalRevenue = (float) thongKe.getTotalRevenueWithinLast7Days();
        var barEntries1 = new ArrayList<BarEntry>();
        var barEntries2 = new ArrayList<BarEntry>();
        var barEntries3 = new ArrayList<BarEntry>();
        var barEntries4 = new ArrayList<BarEntry>();

        for (int i = 0; i < 7; i++) {
            var dailyStat = dailyRevenues.get(i);
            var dailyStats = dailyStatsList.get(i);
            float totalIncoming = dailyStat.getTongDoanhThu();
            float totalIncomingg = dailyStats.getTongVao();
            float totalOutgoing = dailyStats.getTongRa();
            barEntries1.add(new BarEntry(i + 1, totalIncomingg));
            barEntries2.add(new BarEntry(i + 1, totalOutgoing));
            barEntries3.add(new BarEntry(i + 1, totalIncoming));
            barEntries4.add(new BarEntry(i + 1, totalRevenue));
        }
        var barDataSet1 = new BarDataSet(barEntries1, "Nhập kho");
        var barDataSet2 = new BarDataSet(barEntries2, "Xuất kho");
        var barDataSet3 = new BarDataSet(barEntries3, "Tổng DT hằng ngày");
        var barDataSet4 = new BarDataSet(barEntries4, "Tổng doanh thu cả tuần");
        barDataSet1.setColor(Color.RED);
        barDataSet2.setColor(Color.BLUE);
        barDataSet3.setColor(Color.RED);
        barDataSet4.setColor(Color.BLUE);
        var barData = new BarData(barDataSet1, barDataSet2);
        var barDatat = new BarData(barDataSet3, barDataSet4);
        binding.barChart.setData(barDatat);
        binding.lineChart.setData(barData);

        var days = new String[7];
        for (int i = 0; i < 7; i++) {
            var dateFormat = new SimpleDateFormat("dd/MM");
            days[i] = dateFormat.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        String[] day = new String[]{days[0], days[1], days[2], days[3], days[4], days[5], days[6]};

        var xAxis = binding.barChart.getXAxis();
        var xAxiss = binding.lineChart.getXAxis();

        xAxiss.setValueFormatter(new IndexAxisValueFormatter(day));
        xAxis.setValueFormatter(new IndexAxisValueFormatter(day));
        xAxiss.setCenterAxisLabels(true);
        xAxis.setCenterAxisLabels(true);
        xAxiss.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxiss.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxiss.setGranularityEnabled(true);

        binding.lineChart.setDragEnabled(true);
        binding.barChart.setDragEnabled(true);
        binding.barChart.setVisibleXRangeMaximum(7);
        binding.lineChart.setVisibleXRangeMaximum(7);

        float barSpace = 0.06f;
        float groupSpace = 0.16f;
        barDatat.setBarWidth(0.35f);
        barData.setBarWidth(0.35f);

        binding.lineChart.getXAxis().setAxisMinimum(0);
        binding.barChart.getXAxis().setAxisMinimum(0);
        binding.barChart.getAxisRight().setEnabled(false);
        binding.lineChart.getAxisRight().setEnabled(false);
        binding.barChart.groupBars(0, groupSpace, barSpace);
        binding.lineChart.groupBars(0, groupSpace, barSpace);
        binding.lineChart.getAxisLeft().setAxisMinimum(0);
        binding.lineChart.invalidate();
        binding.barChart.invalidate();

        binding.edtngaybd.setOnClickListener(v -> setupDatePickerDialog(binding.edtngaybd));

        binding.edtngaykt.setOnClickListener(v -> setupDatePickerDialog(binding.edtngaykt));
        binding.btnTatCaNgay.setOnClickListener(v -> loadData());

        binding.btnkhoang.setOnClickListener(view1 -> {
            try {
                var ngaybd = binding.edtngaybd.getText().toString();
                var ngaykt = binding.edtngaykt.getText().toString();
                if (ngaybd.isEmpty() || ngaykt.isEmpty()) {
                    binding.edtngaybd.setError("Không được để trống");
                    binding.edtngaykt.setError("Không được để trống");
                    Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ngaybd.compareTo(ngaykt) > 0) {
                    binding.edtngaybd.setError("Ngày bắt đầu phải nhỏ hơn ngày kết thúc");
                    binding.edtngaykt.setError("Ngày kết thúc phải lớn hơn ngày bắt đầu");
                    Toast.makeText(requireContext(), "Ngày bắt đầu phải nhỏ hơn ngày kết thúc", Toast.LENGTH_SHORT).show();
                    return;
                }
                loadStatsBetweenDates(ngaybd, ngaykt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        binding.btn1Ngay.setOnClickListener(v -> {
            try {
                var calendarr = Calendar.getInstance();
                var nam = calendarr.get(Calendar.YEAR);
                var thang = calendarr.get(Calendar.MONTH);
                var dayss = calendarr.get(Calendar.DAY_OF_MONTH);
                var datePickerDialog = new DatePickerDialog(requireContext(), (view12, year, month, dayOfMonth) -> {
                    var formattedYear = String.valueOf(year);
                    var formattedMonth = (month < 9) ? "0" + (month + 1) : String.valueOf(month + 1);
                    var formattedDay = (dayOfMonth < 10) ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                    var dates = String.format("%s-%s-%s", formattedDay, formattedMonth, formattedYear);
                    loadStatsForDate(dates);
                }, nam, thang, dayss);
                datePickerDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return view;
    }


    private void setupDatePickerDialog(EditText editText) {
        var calendar = Calendar.getInstance();
        var nam = calendar.get(Calendar.YEAR);
        var thang = calendar.get(Calendar.MONTH);
        var dayss = calendar.get(Calendar.DAY_OF_MONTH);

        var datePickerDialog = new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            var formattedYear = String.valueOf(year);
            var formattedMonth = (month < 9) ? "0" + (month + 1) : String.valueOf(month + 1);
            var formattedDay = (dayOfMonth < 10) ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
            editText.setText(String.format("%s-%s-%s", formattedDay, formattedMonth, formattedYear));
        }, nam, thang, dayss);

        datePickerDialog.show();
    }

    private void loadData() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        dao = new ThongKeDAO(requireContext());
        var list = dao.getAllStats();
        var adapter = new ThongkeAdapter(requireContext(), (ArrayList<ThongKe>) list, dao);
        binding.recyclerView.setAdapter(adapter);
    }

    private void loadStatsForDate(String date) {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        dao = new ThongKeDAO(requireContext());
        var stats = dao.getStatsForDate(date);

        if (stats != null) {
            ArrayList<ThongKe> list = new ArrayList<>();
            list.add(stats);
            var adapter = new ThongkeAdapter(requireContext(), list, dao);
            binding.recyclerView.setAdapter(adapter);
        }

    }

    private void loadStatsBetweenDates(String startdate, String enddate) {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        dao = new ThongKeDAO(requireContext());
        List<ThongKe> list = dao.getStatsBetweenDates(startdate, enddate);
        if (list != null && !list.isEmpty()) {
            var adapter = new ThongkeAdapter(requireContext(), (ArrayList<ThongKe>) list, dao);
            binding.recyclerView.setAdapter(adapter);
        }
    }


    private void initActionBar() {
        var actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            requireActivity().getSupportFragmentManager().popBackStack();
            closeMenu();
            var actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
            assert actionBar != null;
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_24);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void closeMenu() {
        ((MenuController) requireActivity()).closeMenu();
    }
}