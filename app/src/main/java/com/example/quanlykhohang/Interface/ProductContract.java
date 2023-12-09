package com.example.quanlykhohang.Interface;

import com.example.quanlykhohang.model.Product;

import java.util.List;

public interface ProductContract {
    interface View {
        void showProductsList(List<Product> productList);

        void showErrorMessage(String message);

        void showSuccessMessage(String message);
    }

    interface Presenter {
        void getAllProducts(String storage);

        void insertProduct(Product product,String storage);

        void updateProduct(Product product, int id,String storage);

        void updateProductBill(int productQuantity, int id,String storage);

        void updateProductStorage(String storage, int id);

        void onDestroy();
    }
}
