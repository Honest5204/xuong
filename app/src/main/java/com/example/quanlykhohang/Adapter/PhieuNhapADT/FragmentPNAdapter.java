package com.example.quanlykhohang.Adapter.PhieuNhapADT;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlykhohang.Fragment.FraPhieuNhap.PhieuNhapChiTietFragment;
import com.example.quanlykhohang.Interface.TransFerFra;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.dao.BillDAO;
import com.example.quanlykhohang.dao.BillDetailDAO;
import com.example.quanlykhohang.databinding.ItemPhieuNhapBinding;
import com.example.quanlykhohang.model.Bill;

import java.util.ArrayList;

public class FragmentPNAdapter extends RecyclerView.Adapter<FragmentPNAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Bill> list;
    private BillDAO dao;
    private BillDetailDAO daoCTPN;

    public FragmentPNAdapter(Context context, ArrayList<Bill> list, BillDAO dao) {
        this.context = context;
        this.list = list;
        this.dao = dao;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var inflater = LayoutInflater.from(parent.getContext());
        var view = inflater.inflate(com.example.quanlykhohang.R.layout.item_phieu_nhap, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.txtMa.setText(String.valueOf(list.get(position).getId()));
        if (Integer.parseInt(list.get(position).getQuantity()) == 0){
            holder.binding.txtTrangThai.setText("Nhập kho");
        }
        holder.binding.txtNguoiTao.setText(list.get(position).getCreatedByUser());
        holder.binding.txtNgay.setText(list.get(position).getCreatedDate());
        holder.binding.btnchitiet.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Fragment fragment = new PhieuNhapChiTietFragment();
            Bundle args = new Bundle();
            args.putString("id", String.valueOf(list.get(position).getId()));
            fragment.setArguments(args);
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fame, fragment) // R.id.fragment_container là ID của một ViewGroup để chứa Fragment
                    .addToBackStack(PhieuNhapChiTietFragment.TAG) // Cho phép người dùng quay trở lại Fragment trước đó
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemPhieuNhapBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemPhieuNhapBinding.bind(itemView);
        }
    }
}
