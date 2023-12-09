package com.example.quanlykhohang.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.example.quanlykhohang.Fragment.FraPhieuNhap.PhieuNhapFragment;
import com.example.quanlykhohang.Fragment.FraPhieuXuat.PhieuXuatFragment;
import com.example.quanlykhohang.Fragment.FraProduct.FragmentProductFragment;
import com.example.quanlykhohang.Fragment.FraThongKe.BaoCaoFragment;
import com.example.quanlykhohang.Fragment.FraUser.FragmentUserFragment;
import com.example.quanlykhohang.Interface.FragmentInteractionListener;
import com.example.quanlykhohang.Interface.MenuController;
import com.example.quanlykhohang.Interface.TransFerFra;
import com.example.quanlykhohang.Interface.UserContract;
import com.example.quanlykhohang.Presenter.UserPresenter;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.databinding.ActivityMainBinding;
import com.example.quanlykhohang.model.User;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MenuController, FragmentInteractionListener, TransFerFra, UserContract.View {
    private ActivityMainBinding binding;
    private FragmentProductFragment yourFragment;
    private UserPresenter userPresenter;
    private TextView txtTen, fullName;
    private ImageView imgAvatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        var header = binding.nav.getHeaderView(0);
        txtTen = header.findViewById(R.id.txtNamess);
        fullName = header.findViewById(R.id.txtFullNames);
        imgAvatar = header.findViewById(R.id.imgAvatarr);

        var sharedPreferences = this.getSharedPreferences("INFOR", MODE_PRIVATE);
        var id = sharedPreferences.getString("ID", "");
        userPresenter = new UserPresenter(this, this);
        userPresenter.getUserDetails(id);
        setSupportActionBar(binding.toolbar);
        var actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_24);
        getSupportFragmentManager().beginTransaction().replace(R.id.fame, new PhieuNhapFragment()).commit();
        binding.nav.setNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            var itemId = item.getItemId();
            if (itemId == R.id.product) {
                fragment = new FragmentProductFragment();
            } else if (itemId == R.id.user) {
                fragment = new FragmentUserFragment();
            } else if (itemId == R.id.dangxuat) {
                var intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivities(new Intent[]{intent});
            }
            if (fragment != null) {
                var fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fame, fragment).commit();
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                binding.toolbar.setTitle(item.getTitle());
            }
            return false;
        });


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.baocao) {
                fragment = new BaoCaoFragment();
            } else if (itemId == R.id.phieunhap) {
                fragment = new PhieuNhapFragment();
            } else {
                fragment = new PhieuXuatFragment();
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fame, fragment)
                    .commit();
            binding.toolbar.setTitle(item.getTitle());
            binding.drawerLayout.close();
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void closeMenu() {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }


    @Override
    public void onFragmentBackPressed() {
        boolean isExpanded = yourFragment.isExpanded();
        // Xử lý sự kiện khi Fragment yêu cầu trở về
        if (isExpanded) {
            yourFragment.shrinkFab();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onFragmentDispatchTouchEvent(MotionEvent ev) {
        // Xử lý sự kiện khi Fragment yêu cầu xử lý sự kiện chạm vào màn hình
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // ...
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void transferFragment(Fragment fragment, String name) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fame, fragment)
                .addToBackStack(name)
                .commit();
    }

    @Override
    public void showUsersList(List<User> userList) {

    }

    @Override
    public void showUserDetails(User user) {
        imgAvatar.setImageURI(Uri.parse(user.getAvatar()));
        fullName.setText(user.getName());
        txtTen.setText(user.getPosition());
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String message) {

    }
}

