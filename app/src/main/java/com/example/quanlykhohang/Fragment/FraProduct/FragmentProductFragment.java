package com.example.quanlykhohang.Fragment.FraProduct;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quanlykhohang.Adapter.UserAdapter;
import com.example.quanlykhohang.Interface.FragmentInteractionListener;
import com.example.quanlykhohang.Interface.ProductContract;
import com.example.quanlykhohang.Interface.TransFerFra;
import com.example.quanlykhohang.Presenter.ProductPresenter;
import com.example.quanlykhohang.Presenter.UserPresenter;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.Adapter.ProductADT.ProductAdapter;
import com.example.quanlykhohang.dao.ProductDAO;

import com.example.quanlykhohang.databinding.FragmentProductBinding;
import com.example.quanlykhohang.model.Product;

import java.util.ArrayList;
import java.util.List;


public class FragmentProductFragment extends Fragment implements ProductAdapter.ProductAdapterListener, ProductContract.View {
    private FragmentProductBinding binding;
    private FragmentInteractionListener interactionListener;
    private boolean isExpanded = false;

    private Animation fromBottomFabAnim;
    private Animation toBottomFabAnim;
    private Animation rotateClockWiseFabAnim;
    private Animation rotateAntiClockWiseFabAnim;
    private Animation fromBottomBgAnim;
    private Animation toBottomBgAnim;
    private ProductContract.Presenter presenter;
    private ProductAdapter adapter;

    public FragmentProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_product, container, false);
        binding = FragmentProductBinding.bind(view);
        anhxaAnima();
        moAnima();
        loadData();
        var actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_24);
        binding.addSP.setOnClickListener(v -> transferFragment(new AddProductFragment(),
                                                               AddProductFragment.TAG));
        binding.btnStorage.setOnClickListener(v -> transferFragment(new StorageProductFragment(),
                                                                    StorageProductFragment.TAG));
        return view;
    }

    private void moAnima() {
        binding.mainFabBtn.setOnClickListener(v -> {
            if (isExpanded) {
                shrinkFab();
            } else {
                expandFab();
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
        adapter = new ProductAdapter(requireContext(), this);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerview.setAdapter(adapter);
        presenter = new ProductPresenter(this, requireContext());
        adapter.setProductPresenter(presenter);
        presenter.getAllProducts("1");
    }

    private void transferFragment(Fragment fragment,String name) {
        ((TransFerFra) requireActivity()).transferFragment(fragment,name);
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener) {
            interactionListener = (FragmentInteractionListener) context;
        } else {
            throw new ClassCastException(context + " must implement FragmentInteractionListener");
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
        if (ev.getAction() == MotionEvent.ACTION_DOWN && (isExpanded)) {
                var outRect = new Rect();
                binding.fabConstraint.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    shrinkFab();
                }

        }
        return interactionListener.onFragmentDispatchTouchEvent(ev);
    }

    @Override
    public void onEditProductClicked(Product product) {
        var fragment = new UpdateProductFragment();
        var bundle = new Bundle();
        bundle.putString("id", String.valueOf(product.getId()));
        bundle.putString("name", product.getName());
        bundle.putString("price", String.valueOf(product.getPrice()));
        bundle.putString("quantity", String.valueOf(product.getQuantity()));
        bundle.putString("photo", product.getPhoto());
        fragment.setArguments(bundle);
        transferFragment(fragment, UpdateProductFragment.TAG);
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