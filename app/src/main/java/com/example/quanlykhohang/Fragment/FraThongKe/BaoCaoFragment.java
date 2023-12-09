package com.example.quanlykhohang.Fragment.FraThongKe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.quanlykhohang.Interface.TransFerFra;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.databinding.FragmentBaoCaoBinding;


public class BaoCaoFragment extends Fragment {
    private FragmentBaoCaoBinding binding;

    public BaoCaoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_bao_cao, container, false);
        binding = FragmentBaoCaoBinding.bind(view);
        seticon();
        binding.addUser.setOnClickListener(v -> transferFragment(new ThongKeNgayFragment(),
                                                                 ThongKeNgayFragment.TAG));
        binding.changePassword.setOnClickListener(v -> transferFragment(new ThongKeThangFragment(),
                                                                        ThongKeThangFragment.TAG));
        binding.listUser.setOnClickListener(v -> transferFragment(new ThongKeNamFragment(),
                                                                  ThongKeNamFragment.TAG));
        return view;
    }

    private void transferFragment(Fragment fragment,String name) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fame, fragment)
                .addToBackStack(name)
                .commit();
    }



    private void seticon() {
        var actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_24);
    }
}