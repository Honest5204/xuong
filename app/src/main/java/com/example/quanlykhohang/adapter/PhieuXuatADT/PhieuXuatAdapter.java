package com.example.quanlykhohang.adapter.PhieuXuatADT;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlykhohang.Fragment.FraPhieuNhap.phieunhapchitiet;
import com.example.quanlykhohang.Fragment.FraPhieuXuat.phieuxuatchitiet;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.adapter.PhieuNhapADT.BillDeatailAdapter;
import com.example.quanlykhohang.dao.billDAO;
import com.example.quanlykhohang.dao.billDetailDAO;
import com.example.quanlykhohang.databinding.ItemPhieuXuatBinding;
import com.example.quanlykhohang.model.Bill;

import java.util.ArrayList;

public class PhieuXuatAdapter extends RecyclerView.Adapter<PhieuXuatAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Bill> list;
    private billDAO dao;
    private billDetailDAO daoCTPN;

    public PhieuXuatAdapter(Context context, ArrayList<Bill> list, billDAO dao) {
        this.context = context;
        this.list = list;
        this.dao = dao;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var inflater = LayoutInflater.from(parent.getContext());
        var view = inflater.inflate(com.example.quanlykhohang.R.layout.item_phieu_xuat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.txtMa.setText("" + list.get(position).getId());
        if (Integer.parseInt(list.get(position).getQuantity()) == 1){
            holder.binding.txtTrangThai.setText("Xuất kho");
        }
        holder.binding.txtNguoiTao.setText("" + list.get(position).getCreatedByUser());
        holder.binding.txtNgay.setText("" + list.get(position).getCreatedDate());
        holder.binding.btnchitiet.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Fragment fragment = new phieuxuatchitiet();
            Bundle args = new Bundle();
            args.putString("id", "" + list.get(position).getId());
            fragment.setArguments(args);

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fame, fragment) // R.id.fragment_container là ID của một ViewGroup để chứa Fragment
                    .addToBackStack(null) // Cho phép người dùng quay trở lại Fragment trước đó
                    .commit();
        });
//        holder.binding.rcviewCTPX.setLayoutManager(new LinearLayoutManager(context));
//        daoCTPN = new billDetailDAO(context);
//        var listt = daoCTPN.getALlBillDetailX(list.get(position).getId());
//        var adapter = new BillDetailPXAdapter(context, listt, daoCTPN);
//        holder.binding.rcviewCTPX.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        if (list != null) {return list.size();}
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemPhieuXuatBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemPhieuXuatBinding.bind(itemView);
        }
    }
}
