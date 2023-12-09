package com.example.quanlykhohang.Fragment.FraUser;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.quanlykhohang.Interface.TransFerFra;
import com.example.quanlykhohang.Interface.UserContract;
import com.example.quanlykhohang.Presenter.UserPresenter;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.dao.UserDAO;
import com.example.quanlykhohang.databinding.FragmentUserBinding;
import com.example.quanlykhohang.model.User;

import java.util.List;


public class FragmentUserFragment extends Fragment implements UserContract.View {
    private FragmentUserBinding binding;
    private UserPresenter userPresenter;

    public FragmentUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_user, container, false);
        binding = FragmentUserBinding.bind(view);
        phanQuyen();
        seticon();
        updatedUserInterface();
        binding.listUser.setOnClickListener(v -> transferFragment(new ListUserFragment(),
                                                                  ListUserFragment.TAG));

        binding.addUser.setOnClickListener(v -> transferFragment(new AddUserFragment(),
                                                                 AddUserFragment.TAG));

        binding.btnUpdate.setOnClickListener(v -> transferFragment(new UpdateUserFragment(),
                                                                   UpdateUserFragment.TAG));

        binding.changePassword.setOnClickListener(v -> transferFragment(new ChangePasswordFragment(),
                                                                        ChangePasswordFragment.TAG));

        return view;
    }

    private void seticon() {
        var actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_24);
    }

    @SuppressLint("SetTextI18n")
    private void updatedUserInterface() {
        var sharedPreferences = requireContext().getSharedPreferences("INFOR", MODE_PRIVATE);
        var id = sharedPreferences.getString("ID", "");
        userPresenter = new UserPresenter(this, requireContext());
        userPresenter.getUserDetails(id);
    }

    private void phanQuyen() {
        var sharedPreferences = requireContext().getSharedPreferences("INFOR", MODE_PRIVATE);
        var quyen = sharedPreferences.getString("POSITION", "");
        if (!quyen.equals("admin")) {
            binding.addUser.setVisibility(View.GONE);
            binding.listUser.setVisibility(View.GONE);
        }
    }


    private void transferFragment(Fragment fragment, String name) {
        ((TransFerFra) requireActivity()).transferFragment(fragment, name);
    }

    @Override
    public void showUsersList(List<User> userList) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showUserDetails(User user) {
        binding.imgavata.setImageURI(Uri.parse(user.getAvatar()));
        binding.txtNamePosi.setText(user.getName() + " (" + user.getPosition() + ")");
        binding.txtQue.setText(user.getHomeTown());
    }

    @Override
    public void showErrorMessage(String message) {

    }

    @Override
    public void showSuccessMessage(String message) {

    }
}