package com.pluralsight.data;

import com.pluralsight.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
}
