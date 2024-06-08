package com.retail.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retail.ecom.model.CartProduct;

public interface CartProductRepository extends JpaRepository<CartProduct, Integer> {

}
