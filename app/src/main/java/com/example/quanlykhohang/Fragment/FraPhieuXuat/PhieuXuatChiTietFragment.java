package com.example.quanlykhohang.Fragment.FraPhieuXuat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.quanlykhohang.Adapter.PhieuXuatADT.BillDetailPXAdapter;
import com.example.quanlykhohang.Fragment.FraPhieuNhap.PhieuNhapChiTietFragment;
import com.example.quanlykhohang.Interface.MenuController;

import com.example.quanlykhohang.R;


import com.example.quanlykhohang.dao.BillDetailDAO;
import com.example.quanlykhohang.databinding.FragmentPhieuxuatchitietBinding;
import com.example.quanlykhohang.model.BillDetail;

import java.util.ArrayList;
import java.util.Objects;


public class PhieuXuatChiTietFragment extends Fragment {
    public static final String TAG = PhieuXuatChiTietFragment.class.getName();
    private FragmentPhieuxuatchitietBinding binding;
    Bundle args;
    private BillDetailDAO daoCTPN;

    public PhieuXuatChiTietFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_phieuxuatchitiet, container, false);
        binding = FragmentPhieuxuatchitietBinding.bind(view);
        setHasOptionsMenu(true);
        var actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);

        args = getArguments();
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        daoCTPN = new BillDetailDAO(requireContext());
        var listt = daoCTPN.getALlBillDetailX(Integer.parseInt(Objects.requireNonNull(args.getString("id"))));
        var adapter = new BillDetailPXAdapter(requireContext(), (ArrayList<BillDetail>) listt, daoCTPN);
        binding.recyclerview.setAdapter(adapter);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            requireActivity().getSupportFragmentManager().popBackStack();
            closeMenu();
            var actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_24);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void closeMenu() {
        ((MenuController) requireActivity()).closeMenu();
    }
}