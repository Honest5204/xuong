package com.example.quanlykhohang.Adapter.PhieuXuatADT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlykhohang.dao.BillDetailDAO;
import com.example.quanlykhohang.databinding.ItemHoadonxuatchitietBinding;
import com.example.quanlykhohang.model.BillDetail;

import java.util.ArrayList;

public class BillDetailPXAdapter extends RecyclerView.Adapter<BillDetailPXAdapter.ViewHolder>{
    private Context context;
    private ArrayList<BillDetail> list;
    private BillDetailDAO dao;

    public BillDetailPXAdapter(Context context, ArrayList<BillDetail> list, BillDetailDAO dao) {
        this.context = context;
        this.list = list;
        this.dao = dao;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var inflater = LayoutInflater.from(parent.getContext());
        var view = inflater.inflate(com.example.quanlykhohang.R.layout.item_hoadonxuatchitiet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.txtSanPham.setText(list.get(position).getNameProduct());
        holder.binding.txtSoLuong.setText(list.get(position).getQuantity());
        holder.binding.txtGiaXuat.setText(String.valueOf(list.get(position).getPriceXuat()));
    }

    @Override
    public int getItemCount() {
        if (list != null) {return list.size();}
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemHoadonxuatchitietBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemHoadonxuatchitietBinding.bind(itemView);
        }
    }
}