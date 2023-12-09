package com.example.quanlykhohang.Fragment.FraPhieuNhap;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.quanlykhohang.Interface.MenuController;
import com.example.quanlykhohang.Interface.ProductContract;
import com.example.quanlykhohang.Presenter.ProductPresenter;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.dao.BillDAO;
import com.example.quanlykhohang.dao.BillDetailDAO;
import com.example.quanlykhohang.dao.ProductDAO;
import com.example.quanlykhohang.database.DbHelper;
import com.example.quanlykhohang.databinding.FragmentAddPhieuNhapBinding;
import com.example.quanlykhohang.model.Bill;
import com.example.quanlykhohang.model.BillDetail;
import com.example.quanlykhohang.model.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddPhieuNhapFragment extends Fragment implements ProductContract.View{
    public static final String TAG = AddPhieuNhapFragment.class.getName();
    private FragmentAddPhieuNhapBinding binding;
    private BillDAO dao;
    private ProductDAO prod;
    private BillDetailDAO detail;

    public AddPhieuNhapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_add_phieu_nhap, container, false);
        binding = FragmentAddPhieuNhapBinding.bind(view);
        setHasOptionsMenu(true);
        ProductPresenter presenter = new ProductPresenter(this, requireContext());
        presenter.getAllProducts("1");
        var actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        final int[] index = {0};
        binding.seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                index[0] = i;
                binding.btnAdd.setText(index[0] > 1 ? "Tiếp" : "Tạo");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.btnAdd.setOnClickListener(view1 -> {
            if (index[0] > 1) {
                insertSp();
                index[0] = index[0] - 1;
                binding.spnTenSP.setSelection(0);
                binding.txtSoLuong.setText("");
            } else if (index[0] == 1) {
                insertSp();
                insetHoaDon();
                binding.btnAdd.setText("Tạo");
                getParentFragmentManager().popBackStack();
            } else {
                Toast.makeText(requireContext(), "Vui lòng chọn số sản phẩm muốn thêm lớn hơn 0", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    @SuppressLint("SimpleDateFormat")
    private void insertSp() {
        var HMMSP = (HashMap<String, Object>) binding.spnTenSP.getSelectedItem();
        var maSP = (int) HMMSP.get("maSP");
        var gia = (int) HMMSP.get("gia");
        var itt = (int) HMMSP.get("soLuong");
        var soL = binding.txtSoLuong.getText().toString().trim();


        var sdfDate = new SimpleDateFormat("dd-MM-yyyy");
        var currentDate = sdfDate.format(new Date());
        var createdate = currentDate;

        dao = new BillDAO(requireContext());
        var idbill = dao.getLatestBillId();
        if (soL.isEmpty()) {
            binding.txtSoLuong.setError("Vui lòng nhập số lượng ");
        } else {
            try {
                var soLuongSP = Integer.parseInt(soL);
                prod = new ProductDAO(requireContext());
                var tong = itt + soLuongSP;
                prod.updateProductBill(tong, maSP);

                detail = new BillDetailDAO(requireContext());
                var de = new BillDetail(maSP, idbill+1, soLuongSP + "", gia + "",0, createdate);
                var check = detail.addBillDetail(de);
                if (check) {
                    var sharedPreferences = requireContext().getSharedPreferences("INFOR", MODE_PRIVATE);
                    var id = sharedPreferences.getString("ID", "");
                    var base = new DbHelper(requireContext());
                    base.updateLastAction(id);
                    Toast.makeText(requireContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Số lượng phải là số", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }




    private void insetHoaDon() {
        var sharedPreferences = requireContext().getSharedPreferences("INFOR", MODE_PRIVATE);
        var id = sharedPreferences.getString("ID", "");
        var sdfDate = new SimpleDateFormat("dd-MM-yyyy");
        var currentDate = sdfDate.format(new Date());
        var createdate = currentDate;
        dao = new BillDAO(requireContext());
        var bi = new Bill(id, "0", createdate, "");
        dao.insertBill(bi);
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

    @Override
    public void showProductsList(List<Product> productList) {
        List<Map<String, Object>> listData = new ArrayList<>();
        for (Product product : productList) {
            Map<String, Object> listItem = new HashMap<>();
            listItem.put("maSP", product.getId());
            listItem.put("nameSP", product.getName());
            listItem.put("gia", product.getPrice());
            listItem.put("soLuong", product.getQuantity());
            listData.add(listItem);
        }

        String[] from = new String[]{"nameSP"};
        int[] to = new int[]{android.R.id.text1};
        SimpleAdapter adapter = new SimpleAdapter(requireContext(), listData, android.R.layout.simple_list_item_1, from, to);
        binding.spnTenSP.setAdapter(adapter);
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}