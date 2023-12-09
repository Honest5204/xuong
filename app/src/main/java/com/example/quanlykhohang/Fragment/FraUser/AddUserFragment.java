package com.example.quanlykhohang.Fragment.FraUser;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
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
import com.example.quanlykhohang.Interface.UserContract;
import com.example.quanlykhohang.Presenter.UserPresenter;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.dao.UserDAO;
import com.example.quanlykhohang.databinding.FragmentAddUserBinding;
import com.example.quanlykhohang.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class AddUserFragment extends Fragment implements EasyPermissions.PermissionCallbacks, UserContract.View {
    public static final String TAG = AddUserFragment.class.getName();
    ArrayList<Uri> listUri = new ArrayList<>();
    private FragmentAddUserBinding binding;
    private UserPresenter userPresenter;

    public AddUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_add_user, container, false);
        binding = FragmentAddUserBinding.bind(view);

        setHasOptionsMenu(true);
        var actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        userPresenter = new UserPresenter(this, requireContext());
        binding.imgAvatar.setOnClickListener(v -> requestPermission());
        binding.btnAdd.setOnClickListener(v -> addUser());
        return view;
    }

    @SuppressLint("SimpleDateFormat")
    private void addUser() {
        try {
            var imgavatar = listUri.get(0).toString();
            var name = binding.txtName.getText().toString().trim();
            var hometown = "";
            var id = binding.txtID.getText().toString().trim();
            var password = binding.txtPassword.getText().toString().trim();
            var password2 = binding.txtPassword2.getText().toString().trim();
            var position = "thuthu";
            var sdfDate = new SimpleDateFormat("dd-MM-yyyy");
            var sdfDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            var currentDate = sdfDate.format(new Date());
            var currentDatee = sdfDateTime.format(new Date());

            var lastLogin = "";
            if (name.isEmpty() || id.isEmpty() || password.isEmpty() || password2.isEmpty() || imgavatar.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                if (password.equals(password2)) {
                    User user = new User(id, name, password, position, lastLogin, currentDate, currentDatee, hometown, imgavatar);
                    userPresenter.addUser(user);
                } else {
                    Toast.makeText(requireContext(), "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
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
            binding.imgAvatar.setImageURI(listUri.get(0));

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
    public void showUsersList(List<User> userList) {

    }

    @Override
    public void showUserDetails(User user) {

    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        getParentFragmentManager().popBackStack();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        // Xử lý logic khi thêm người dùng thất bại
    }
}