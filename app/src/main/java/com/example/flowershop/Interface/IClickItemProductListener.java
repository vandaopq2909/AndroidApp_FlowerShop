package com.example.flowershop.Interface;

import com.example.flowershop.Class.Product;

public interface IClickItemProductListener {
    void onClickItemProduct(Product product);
    void onClickAddToCart(Product product);
}
