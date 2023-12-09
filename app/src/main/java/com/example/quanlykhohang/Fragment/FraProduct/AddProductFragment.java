package com.example.quanlykhohang.Fragment.FraProduct;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.quanlykhohang.Interface.MenuController;
import com.example.quanlykhohang.Interface.ProductContract;
import com.example.quanlykhohang.Presenter.ProductPresenter;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.dao.ProductDAO;
import com.example.quanlykhohang.database.DbHelper;
import com.example.quanlykhohang.databinding.FragmentAddProductBinding;
import com.example.quanlykhohang.model.Product;

import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class AddProductFragment extends Fragment implements EasyPermissions.PermissionCallbacks, ProductContract.View {
    public static final String TAG = AddProductFragment.class.getName();
    ArrayList<Uri> listUri = new ArrayList<>();
    private ProductDAO dao;
    private FragmentAddProductBinding binding;
    private ProductPresenter productPresenter;

    public AddProductFragment() {
        // TODO document why this constructor is empty
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_add_product, container, false);
        binding = FragmentAddProductBinding.bind(view);
        setHasOptionsMenu(true);
        var actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);

        productPresenter = new ProductPresenter(this, requireContext());
        binding.imgHinhSP.setOnClickListener(v -> requestPermission());
        binding.btnThemSP.setOnClickListener(v -> addProducts());
        return view;
    }

    private void addProducts() {
        try {
            var hinhanhSP = listUri.get(0).toString();
            var tensp = binding.txtTenSP.getText().toString().trim();
            var giasp = binding.txtGiaSP.getText().toString().trim();
            var soluong = binding.txtSoLuong.getText().toString().trim();
            var sharedPreferences = requireContext().getSharedPreferences("INFOR", MODE_PRIVATE);
            var id = sharedPreferences.getString("ID", "");
            if (tensp.isEmpty() || giasp.isEmpty() || soluong.isEmpty() || hinhanhSP.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    var gia = Integer.parseInt(giasp);
                    var sol = Integer.parseInt(soluong);
                    var pr = new Product(tensp, gia, sol, hinhanhSP, "1", id);
                    productPresenter.insertProduct(pr, "1");
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Giá và số lượng phải là số", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void requestPermission() {
        var strings = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(requireContext(), strings)) {
            imagePicker();
        } else {
            EasyPermissions.requestPermissions(this, "Cấp quyền truy cập ảnh", 100, strings);
        }
    }

    public void imagePicker() {
        FilePickerBuilder.getInstance()
                .setActivityTitle("Chọn ảnh")
                .setSpan(FilePickerConst.SPAN_TYPE.FOLDER_SPAN, 3)
                .setSpan(FilePickerConst.SPAN_TYPE.DETAIL_SPAN, 4)
                .setMaxCount(1)
                .setSelectedFiles(listUri)
                .setActivityTheme(R.style.CustomTheme)
                .pickPhoto(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && (requestCode == FilePickerConst.REQUEST_CODE_PHOTO)) {
            assert data != null;
            listUri = data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
            assert listUri != null;
            binding.imgHinhSP.setImageURI(listUri.get(0));

        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == 100 && perms.size() == 1) {
            imagePicker();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        } else {
            Toast.makeText(requireContext(), "Premission denied", Toast.LENGTH_SHORT).show();
        }
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

    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        var sharedPreferences = requireContext().getSharedPreferences("INFOR", MODE_PRIVATE);
        var id = sharedPreferences.getString("ID", "");
        var base = new DbHelper(requireContext());
        base.updateLastAction(id);
        getParentFragmentManager().popBackStack();
    }
}