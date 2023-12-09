package com.example.quanlykhohang.Adapter.ThongkeADT;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.example.quanlykhohang.Fragment.FraThongKe.ThongKeChiTietThangFragment;
import com.example.quanlykhohang.Interface.TransFerFra;

import com.example.quanlykhohang.R;
import com.example.quanlykhohang.dao.ThongKeDAO;
import com.example.quanlykhohang.databinding.ItemThongkethangBinding;
import com.example.quanlykhohang.model.ThongKe;

import java.util.ArrayList;


public class ThongkeThangAdapter extends RecyclerView.Adapter<ThongkeThangAdapter.ViewHolder>{
    private Context context;
    private ArrayList<ThongKe> list;
    private ThongKeDAO dao;

    public ThongkeThangAdapter(Context context, ArrayList<ThongKe> list, ThongKeDAO dao) {
        this.context = context;
        this.list = list;
        this.dao = dao;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var inflater = LayoutInflater.from(parent.getContext());
        var view = inflater.inflate(R.layout.item_thongkethang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.txtNgay.setText(list.get(position).getDate());
        holder.binding.txtNhapKho.setText(String.valueOf(list.get(position).getTongVao()));
        holder.binding.txtXuatKho.setText(String.valueOf(list.get(position).getTongRa()));
        holder.binding.txtDoanhThu.setText(String.valueOf(list.get(position).getTongDoanhThu()));
        holder.binding.btnchitiet.setOnClickListener(v -> {
            Fragment fragment = new ThongKeChiTietThangFragment();
            Bundle args = new Bundle();
            args.putString("date", list.get(position).getDate());
            fragment.setArguments(args);
            transferFragment(fragment, ThongKeChiTietThangFragment.TAG);
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
        private ItemThongkethangBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemThongkethangBinding.bind(itemView);
        }
    }
    private void transferFragment(Fragment fragment,String name) {
        ((TransFerFra) context).transferFragment(fragment,name);
    }
}
