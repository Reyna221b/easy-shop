package com.pluralsight.data;

import com.pluralsight.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    void addProduct(int userId, int productId, int quantity);
    void updateProduct(int userId, int productId, int quantity);
    void clearCart(int userId);
}
