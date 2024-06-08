package com.retail.ecom.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.retail.ecom.model.CartProduct;
import com.retail.ecom.model.Product;
import com.retail.ecom.repository.CartProductRepository;
import com.retail.ecom.service.CartProductService;
import com.retail.ecom.utility.ResponseStructure;

import lombok.AllArgsConstructor;
@AllArgsConstructor
@Service
public class CartProductServiceImpl implements CartProductService{
	private CartProductRepository cartProductRepository;

	@Override
	public ResponseEntity<ResponseStructure<CartProduct>> addProductToCartProduct(Product product) {
		return null;
	}

}
