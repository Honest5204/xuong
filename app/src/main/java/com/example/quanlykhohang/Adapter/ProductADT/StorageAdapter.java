package com.example.quanlykhohang.Adapter.ProductADT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlykhohang.R;
import com.example.quanlykhohang.dao.ProductDAO;
import com.example.quanlykhohang.databinding.ItemStorageBinding;
import com.example.quanlykhohang.model.Product;

import java.util.ArrayList;
import java.util.List;

public class StorageAdapter extends RecyclerView.Adapter<StorageAdapter.ViewHolder> {
    private Context context;
    private List<Product> list;

    public StorageAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }
   public void setData(List<Product> listProduct) {
        this.list = listProduct;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var inflater = LayoutInflater.from(parent.getContext());
        var view = inflater.inflate(R.layout.item_storage, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = list.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemStorageBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemStorageBinding.bind(itemView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Product product) {
            binding.txtmaSP.setText("Mã sản phẩm: " + product.getId());
            binding.txtTenSP.setText("Tên sản phẩm: " + product.getName());
            binding.txtGiaSP.setText("Giá sản phẩm: " + product.getPrice());
            binding.txtSoLuong.setText("Số lượng: " + product.getQuantity());
            binding.txtNguoiThem.setText("Người thêm: " + product.getUserID());
            String photoPath = product.getPhoto();
            if (photoPath != null) {
                Uri uri = Uri.parse(photoPath);
                binding.imgHinhSP.setImageURI(uri);
            } else {
                binding.imgHinhSP.setImageResource(droidninja.filepicker.R.drawable.image_placeholder); // Đặt hình ảnh mặc định (R.drawable.default_image là ID của hình mặc định)
            }
        }
    }
}
