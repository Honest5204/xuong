package com.example.quanlykhohang.Fragment.FraProduct;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quanlykhohang.Adapter.ProductADT.ProductAdapter;
import com.example.quanlykhohang.Interface.MenuController;
import com.example.quanlykhohang.Interface.ProductContract;
import com.example.quanlykhohang.Presenter.ProductPresenter;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.Adapter.ProductADT.StorageAdapter;
import com.example.quanlykhohang.dao.ProductDAO;
import com.example.quanlykhohang.databinding.FragmentStorageProductBinding;
import com.example.quanlykhohang.model.Product;

import java.util.ArrayList;
import java.util.List;


public class StorageProductFragment extends Fragment implements ProductContract.View {
    public static final String TAG = StorageProductFragment.class.getName();
    private FragmentStorageProductBinding binding;
    private StorageAdapter adapter;
    private ProductContract.Presenter presenter;

    public StorageProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_storage_product, container, false);
        binding = FragmentStorageProductBinding.bind(view);
        loadData();
        setHasOptionsMenu(true);
        var actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        return view;
    }

    private void loadData() {
        adapter = new StorageAdapter(requireContext());
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerview.setAdapter(adapter);
        presenter = new ProductPresenter(this, requireContext());
        presenter.getAllProducts("0");
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
        adapter.setData(productList);
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