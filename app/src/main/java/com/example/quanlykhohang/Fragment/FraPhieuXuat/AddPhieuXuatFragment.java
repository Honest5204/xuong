package com.example.quanlykhohang.Fragment.FraPhieuXuat;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.quanlykhohang.databinding.FragmentAddPhieuXuatBinding;
import com.example.quanlykhohang.model.Bill;
import com.example.quanlykhohang.model.BillDetail;
import com.example.quanlykhohang.model.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPhieuXuatFragment extends Fragment implements ProductContract.View{
    public static final String TAG = AddPhieuXuatFragment.class.getName();
    private FragmentAddPhieuXuatBinding binding;

    private BillDAO dao;
    private ProductDAO prod;
    private BillDetailDAO detail;


    public AddPhieuXuatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_add_phieu_xuat, container, false);
        binding = FragmentAddPhieuXuatBinding.bind(view);
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
                // TODO document why this method is empty
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO document why this method is empty
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
        binding.spnTenSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Lấy giá trị đã chọn từ Spinner
                var HMMSP = (HashMap<String, Object>) binding.spnTenSP.getSelectedItem();
                var itt = (int) HMMSP.get("soLuong");
                binding.txtSoLuongConLai.setText(itt + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Xử lý khi không có mục nào được chọn
            }
        });


        return view;
    }
    @SuppressLint("SimpleDateFormat")
    private void insertSp() {
        var HMMSP = (HashMap<String, Object>) binding.spnTenSP.getSelectedItem();
        var maSP = (int) HMMSP.get("maSP");
        var itt = (int) HMMSP.get("soLuong");
        var gia = (int) HMMSP.get("gia");
        var giaxuat = binding.txtGiaXuat.getText().toString().trim();
        var soL = binding.txtSoLuong.getText().toString().trim();

        if (soL.isEmpty() || giaxuat.isEmpty()) {
            binding.txtSoLuong.setError("Vui lòng nhập số lượng");
            binding.txtGiaXuat.setError("Vui lòng nhập giá xuất");
            return;
        }

        try {
            var soLuongSP = Integer.parseInt(soL);
            var giaXuat = Integer.parseInt(giaxuat);

            if (soLuongSP > itt) {
                Toast.makeText(requireContext(), "Số lượng xuất không được lớn hơn số lượng tồn", Toast.LENGTH_SHORT).show();
                return;
            }

            prod = new ProductDAO(requireContext());
            var tong = itt - soLuongSP;
            prod.updateProductBill(tong, maSP);

            dao = new BillDAO(requireContext());
            var idbill = dao.getLatestBillId();

            detail = new BillDetailDAO(requireContext());
            var createdate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

            var de = new BillDetail(maSP, idbill + 1, soLuongSP + "", gia + "", giaXuat, createdate);
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
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Số lượng và giá xuất phải là số", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void insetHoaDon() {
        var sharedPreferences = requireContext().getSharedPreferences("INFOR", MODE_PRIVATE);
        var id = sharedPreferences.getString("ID", "");
        var sdfDate = new SimpleDateFormat("dd-MM-yyyy");
        var currentDate = sdfDate.format(new Date());
        var createdate = currentDate;
        dao = new BillDAO(requireContext());
        var bi = new Bill(id, "1", createdate, "");
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