package com.example.quanlykhohang.Adapter.PhieuNhapADT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.quanlykhohang.dao.BillDetailDAO;
import com.example.quanlykhohang.databinding.ItemHoadonchitietBinding;
import com.example.quanlykhohang.model.BillDetail;

import java.util.ArrayList;

public class BillDeatailAdapter extends RecyclerView.Adapter<BillDeatailAdapter.ViewHolder> {
    private Context context;
    private ArrayList<BillDetail> list;
    private BillDetailDAO dao;

    public BillDeatailAdapter(Context context, ArrayList<BillDetail> list, BillDetailDAO dao) {
        this.context = context;
        this.list = list;
        this.dao = dao;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var inflater = LayoutInflater.from(parent.getContext());
        var view = inflater.inflate(com.example.quanlykhohang.R.layout.item_hoadonchitiet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.txtSanPham.setText(list.get(position).getNameProduct());
        holder.binding.txtSoLuong.setText(list.get(position).getQuantity());
        holder.binding.txtGiaNhap.setText(list.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        if (list != null) {return list.size();}
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemHoadonchitietBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemHoadonchitietBinding.bind(itemView);
        }
    }
}