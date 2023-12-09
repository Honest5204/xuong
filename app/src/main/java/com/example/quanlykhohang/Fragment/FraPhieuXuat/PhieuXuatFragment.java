package com.example.quanlykhohang.Fragment.FraPhieuXuat;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import com.example.quanlykhohang.Adapter.PhieuXuatADT.PhieuXuatAdapter;
import com.example.quanlykhohang.Interface.FragmentInteractionListener;
import com.example.quanlykhohang.Interface.TransFerFra;

import com.example.quanlykhohang.R;

import com.example.quanlykhohang.dao.BillDAO;
import com.example.quanlykhohang.databinding.FragmentPhieuXuatBinding;


public class PhieuXuatFragment extends Fragment {
    private FragmentPhieuXuatBinding binding;
    private FragmentInteractionListener interactionListener;
    private boolean isExpanded = false;

    private Animation fromBottomFabAnim;
    private Animation toBottomFabAnim;
    private Animation rotateClockWiseFabAnim;
    private Animation rotateAntiClockWiseFabAnim;
    private Animation fromBottomBgAnim;
    private Animation toBottomBgAnim;
    private BillDAO dao;

    public PhieuXuatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_phieu_xuat, container, false);
        binding = FragmentPhieuXuatBinding.bind(view);
        loadData();
        anhxaAnima();
        moAnima();
        binding.addSP.setOnClickListener(v -> {
            transferFragment(new AddPhieuXuatFragment(),AddPhieuXuatFragment.TAG);
        });
        binding.btnStorage.setOnClickListener(v -> {
            transferFragment(new StoragePhieuXuatFragment(),StoragePhieuXuatFragment.TAG);
        });
        return view;
    }

    private void moAnima() {
        binding.mainFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    shrinkFab();
                } else {
                    expandFab();
                }
            }
        });
    }

    public void shrinkFab() {
        binding.transparentBg.startAnimation(toBottomBgAnim);
        binding.mainFabBtn.startAnimation(rotateAntiClockWiseFabAnim);
        binding.addSP.startAnimation(toBottomFabAnim);
        binding.btnStorage.startAnimation(toBottomFabAnim);
        binding.galleryTv.startAnimation(toBottomFabAnim);
        binding.shareTv.startAnimation(toBottomFabAnim);

        isExpanded = !isExpanded;
    }

    private void expandFab() {
        binding.transparentBg.startAnimation(fromBottomBgAnim);
        binding.mainFabBtn.startAnimation(rotateClockWiseFabAnim);
        binding.addSP.startAnimation(fromBottomFabAnim);
        binding.btnStorage.startAnimation(fromBottomFabAnim);
        binding.galleryTv.startAnimation(fromBottomFabAnim);
        binding.shareTv.startAnimation(fromBottomFabAnim);

        isExpanded = !isExpanded;
    }


    private void anhxaAnima() {
        fromBottomFabAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_fab);
        toBottomFabAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_fab);
        rotateClockWiseFabAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_clock_wise);
        rotateAntiClockWiseFabAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_anti_clock_wise);
        fromBottomBgAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim);
        toBottomBgAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim);
    }

    private void loadData() {
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        dao = new BillDAO(requireContext());
        var list = dao.getALlBillX();
        var adapter = new PhieuXuatAdapter(requireContext(), list, dao);
        binding.recyclerview.setAdapter(adapter);
    }

    private void transferFragment(Fragment fragment,String name) {
        ((TransFerFra) requireActivity()).transferFragment(fragment,name);
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener) {
            interactionListener = (FragmentInteractionListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement FragmentInteractionListener");
        }
    }


    public void handleBackPressed() {
        if (isExpanded) {
            shrinkFab();
        } else {
            interactionListener.onFragmentBackPressed();
        }
    }

    public boolean handleDispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isExpanded) {
                var outRect = new Rect();
                binding.fabConstraint.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    shrinkFab();
                }
            }
        }
        return interactionListener.onFragmentDispatchTouchEvent(ev);
    }
}