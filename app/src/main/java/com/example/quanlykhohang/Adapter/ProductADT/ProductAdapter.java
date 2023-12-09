package com.example.quanlykhohang.Adapter.ProductADT;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlykhohang.Interface.ProductContract;
import com.example.quanlykhohang.R;
import com.example.quanlykhohang.databinding.ItemProductBinding;
import com.example.quanlykhohang.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements ProductContract.View{
    private Context context;
    private List<Product> list;
    private ProductAdapterListener listener;
    private ProductContract.Presenter productPresenter;

    public ProductAdapter(Context context, ProductAdapterListener listener) {
        this.context = context;
        this.list = new ArrayList<>();
        this.listener = listener;
    }

    public void setProductPresenter(ProductContract.Presenter productPresenter) {
        this.productPresenter = productPresenter;
    }

    public void setData(List<Product> listProduct) {
        this.list = listProduct;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var inflater = LayoutInflater.from(parent.getContext());
        var view = inflater.inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = list.get(position);
        holder.bind(product);
    }

    private void storageProduct(Product product) {
        new AlertDialog.Builder(context)
                .setTitle("Thông báo")
                .setMessage("Bạn có muốn lưu trữ sản phẩm này không?")
                .setPositiveButton("Có", (dialogInterface, i) -> {
                    if (productPresenter != null) {
                        productPresenter.updateProductStorage("0", product.getId());
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }

    @Override
    public void showProductsList(List<Product> productList) {

    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public interface ProductAdapterListener {
        void onEditProductClicked(Product product);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemProductBinding.bind(itemView);
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
            binding.foldingCell.setOnClickListener(v -> binding.foldingCell.toggle(false));

            binding.btnLuuTru.setOnClickListener(view -> {
                if (productPresenter != null) {
                    storageProduct(product);
                }
            });

            binding.btnSua.setOnClickListener(view -> listener.onEditProductClicked(list.get(getAdapterPosition())));
        }
    }


}
