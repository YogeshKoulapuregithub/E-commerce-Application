package com.retail.ecom.service;

import org.springframework.http.ResponseEntity;

import com.retail.ecom.model.CartProduct;
import com.retail.ecom.model.Product;
import com.retail.ecom.utility.ResponseStructure;

public interface CartProductService {

	public ResponseEntity<ResponseStructure<CartProduct>> addProductToCartProduct(Product product);
	
	

}
