package com.retail.ecom.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.retail.ecom.model.CartProduct;
import com.retail.ecom.model.Product;
import com.retail.ecom.service.CartProductService;
import com.retail.ecom.utility.ResponseStructure;

import lombok.AllArgsConstructor;
@RestController
@AllArgsConstructor
public class CartProductController {
	private CartProductService cartProductService;
	private ResponseStructure<CartProduct> response;
	
	
	public ResponseEntity<ResponseStructure<CartProduct>> addProductToCartProduct(@RequestBody Product product ) {
		return cartProductService.addProductToCartProduct(product);
	}
	
	

}
