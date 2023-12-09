package com.example.quanlykhohang.Presenter;


import android.content.Context;

import com.example.quanlykhohang.Interface.ProductContract;
import com.example.quanlykhohang.dao.ProductDAO;
import com.example.quanlykhohang.model.Product;

import java.util.List;

public class ProductPresenter implements ProductContract.Presenter {
    private ProductContract.View view;
    private ProductDAO productDAO;
    public ProductPresenter(ProductContract.View view, Context context) {
        this.view = view;
        this.productDAO = new ProductDAO(context);
    }

    @Override
    public void getAllProducts(String storage) {
        try {
            List<Product> productList = productDAO.getALlProduct(storage);
            view.showProductsList(productList);
        } catch (Exception e) {
            e.printStackTrace();
            view.showErrorMessage("Đã xảy ra lỗi khi lấy danh sách sản phẩm");
        }
    }

    @Override
    public void insertProduct(Product product,String storage) {
        try {
            boolean success = productDAO.insertProduct(product);

            if (success) {
                view.showSuccessMessage("Thêm sản phẩm thành công");
                getAllProducts(storage); // Reload danh sách sản phẩm sau khi thêm
            } else {
                view.showErrorMessage("Thêm sản phẩm thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.showErrorMessage("Đã xảy ra lỗi khi thêm sản phẩm");
        }
    }

    @Override
    public void updateProduct(Product product, int id,String storage) {
        try {
            boolean success = productDAO.updateProduct(product, id);

            if (success) {
                view.showSuccessMessage("Cập nhật sản phẩm thành công");
                getAllProducts(storage); // Reload danh sách sản phẩm sau khi cập nhật
            } else {
                view.showErrorMessage("Cập nhật sản phẩm thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.showErrorMessage("Đã xảy ra lỗi khi cập nhật sản phẩm");
        }
    }

    @Override
    public void updateProductBill(int productQuantity, int id,String storage) {
        try {
            boolean success = productDAO.updateProductBill(productQuantity, id);

            if (success) {
                view.showSuccessMessage("Cập nhật số lượng sản phẩm thành công");
                getAllProducts(storage); // Reload danh sách sản phẩm sau khi cập nhật
            } else {
                view.showErrorMessage("Cập nhật số lượng sản phẩm thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.showErrorMessage("Đã xảy ra lỗi khi cập nhật số lượng sản phẩm");
        }
    }

    @Override
    public void updateProductStorage(String storage, int id) {
        try {
            boolean success = productDAO.updateProductStorage(storage, id);

            if (success) {
                view.showSuccessMessage("Cập nhật kho sản phẩm thành công");
                getAllProducts(storage); // Reload danh sách sản phẩm sau khi cập nhật
            } else {
                view.showErrorMessage("Cập nhật kho sản phẩm thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.showErrorMessage("Đã xảy ra lỗi khi cập nhật kho sản phẩm");
        }
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
