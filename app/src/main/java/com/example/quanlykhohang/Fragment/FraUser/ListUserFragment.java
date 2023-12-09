package com.example.quanlykhohang.Fragment.FraUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quanlykhohang.Adapter.UserAdapter;
import com.example.quanlykhohang.Interface.MenuController;
import com.example.quanlykhohang.Interface.UserContract;
import com.example.quanlykhohang.Presenter.UserPresenter;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.databinding.FragmentListUserBinding;
import com.example.quanlykhohang.model.User;

import java.util.List;


public class ListUserFragment extends Fragment implements UserContract.View {
    public static final String TAG = ListUserFragment.class.getName();
    private FragmentListUserBinding binding;
    private UserContract.Presenter userPresenter;
    private UserAdapter adapter;

    public ListUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_list_user, container, false);
        binding = FragmentListUserBinding.bind(view);
        loadData();
        setHasOptionsMenu(true);
        var actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        return view;
    }

    private void loadData() {
        adapter = new UserAdapter(requireContext());
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerview.setAdapter(adapter);
        userPresenter = new UserPresenter(this, requireContext());
        userPresenter.loadUsers();
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
        adapter.setData(userList);
    }

    @Override
    public void showUserDetails(User user) {

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