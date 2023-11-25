package com.example.quanlykhohang.Fragment.FraPhieuNhap;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.quanlykhohang.Interface.MenuController;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.adapter.PhieuNhapADT.BillDeatailAdapter;
import com.example.quanlykhohang.adapter.PhieuNhapADT.FragmentPNAdapter;
import com.example.quanlykhohang.dao.billDAO;
import com.example.quanlykhohang.dao.billDetailDAO;
import com.example.quanlykhohang.databinding.FragmentPhieunhapchitietBinding;


public class phieunhapchitiet extends Fragment {
    private FragmentPhieunhapchitietBinding binding;
    private billDetailDAO daoCTPN;
    Bundle args;


    public phieunhapchitiet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_phieunhapchitiet, container, false);
        binding = FragmentPhieunhapchitietBinding.bind(view);
        setHasOptionsMenu(true);
        var actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);

        args = getArguments();
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        daoCTPN = new billDetailDAO(requireContext());
        var listt = daoCTPN.getALlBillDetail(Integer.parseInt(args.getString("id")));
        var adapter = new BillDeatailAdapter(requireContext(), listt, daoCTPN);
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