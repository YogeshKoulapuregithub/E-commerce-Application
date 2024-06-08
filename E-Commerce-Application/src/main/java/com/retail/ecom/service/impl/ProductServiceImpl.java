package com.retail.ecom.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.retail.ecom.controller.ImageController;
import com.retail.ecom.enums.ImageType;
import com.retail.ecom.enums.OrderBy;
import com.retail.ecom.exception.ProductNotFoundById;
import com.retail.ecom.model.Product;
import com.retail.ecom.repository.ProductRepository;
import com.retail.ecom.requestdto.ProductRequest;
import com.retail.ecom.requestdto.SearchFilter;
import com.retail.ecom.responsedto.ProductResponse;
import com.retail.ecom.service.ProductService;
import com.retail.ecom.utility.ProductSpecification;
import com.retail.ecom.utility.ResponseStructure;

import lombok.AllArgsConstructor;
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{
	
	private ProductRepository productRepository;
	private ResponseStructure<ProductResponse> response;
	ResponseStructure<List<ProductResponse>> responseStructure;
	private ImageController controller;
	private ProductSpecification productSpecification;

	@Override
	public ResponseEntity<ResponseStructure<ProductResponse>> addProduct(ProductRequest productRequest) {
		
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		System.err.println(name);
		Product saveProduct=productRepository.save(mapToProduct(productRequest));
		return ResponseEntity.ok(response.setStatuscode(HttpStatus.CREATED.value())
				.setMessage("product added successfully")
				.setData(mapToProductResponse(saveProduct)));
	}

	private ProductResponse mapToProductResponse(Product saveProduct) {
	//	System.out.println(controller.imageFindByProductId(saveProduct.getProductId(), ImageType.COVER));
		return ProductResponse.builder()
				.productId(saveProduct.getProductId())
				.productBrand(saveProduct.getProductBrand())
				.productModel(saveProduct.getProductModel())
				.productDescription(saveProduct.getProductDescription())
				.productPrice(saveProduct.getProductPrice())
				.productQuantity(saveProduct.getProductQuantity())
				.category(saveProduct.getCategory())
				.availabilityStatus(saveProduct.getAvailabilityStatus())
				.coverImage(controller.imageFindByProductId(saveProduct.getProductId(), ImageType.COVER))
			.normalImage(controller.imagesFindByProductId(saveProduct.getProductId(), ImageType.NORMAL))
//				.images(saveProduct)
				.build();
		
		
	}

	private Product mapToProduct(ProductRequest productRequest) {
		return Product.builder()
				.productBrand(productRequest.getProductBrand())
				.productModel(productRequest.getProductModel())
				.productDescription(productRequest.getProductDescription())
				.productPrice(productRequest.getProductPrice())
				.productQuantity(productRequest.getProductQuantity())
				.category(productRequest.getCategory())
				
				.availabilityStatus(productRequest.getAvailabilityStatus())
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductResponse>> updateProduct(ProductRequest productRequest,
			int productId) {

		return productRepository.findById(productId).map(existingProduct -> {
			Product updatedProduct=mapToProduct(productRequest);
			updatedProduct.setProductId(productId);
			productRepository.save(updatedProduct);
			
			return ResponseEntity.ok(response
					.setMessage("Product updated succsfully")
					.setStatuscode(HttpStatus.OK.value())
					.setData(mapToProductResponse(updatedProduct)));
		}).orElseThrow(()->new ProductNotFoundById("Product is not present by this id"));
	}

	@Override
	public ResponseEntity<?> findProducts() {
        List<Product> products = productRepository.findAll(); 
        List<ProductResponse> productResponses = convertToProductResponses(products);
		
        
        return ResponseEntity.ok().body(responseStructure.setData(productResponses));
        
    }

    private List<ProductResponse> convertToProductResponses(List<Product> products) {
        List<ProductResponse> responses = new ArrayList<>();
        
        for (Product product : products) {
        	
        	ProductResponse response2 = mapToProductResponse(product);
        	//System.out.println(response2.getProductId()+" ");
            responses.add(response2);
        	}
        
       
        return responses;
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductResponse>> findByProductId(int productId) {

		return productRepository.findById(productId).map(Product -> {
		
			return ResponseEntity.ok(response
					.setStatuscode(HttpStatus.OK.value())
					.setMessage("Product found successfully")
					.setData(mapToProductResponse(Product)));
		}).orElseThrow(()-> new ProductNotFoundById("Product is not present by this id"));
	

	}

	

	@Override
	public ResponseEntity<?> findProductByFilter(SearchFilter searchFilter,int page,OrderBy orderBy,String sortBy) {
		Specification<Product> specification = productSpecification.buildSpecification(searchFilter);
		Pageable pageable= (Pageable)PageRequest.of(page, 15,Sort.by(OrderBy.DESC ==orderBy ? Sort.Direction.DESC : Sort.Direction.ASC,sortBy));
		
		  Page<Product> page2 = productRepository.findAll(specification,pageable); 
		  List<Product> products = page2.getContent();
	        List<ProductResponse> productResponses = convertToProductResponses(products);
	        return ResponseEntity.ok().body(responseStructure.setData(productResponses));
		
		
	}

	@Override
	public ResponseEntity<?> searchString(String serachText) {
	List<ProductResponse> products=productRepository.findByproductBrandContainingOrProductDescriptionContaining(serachText,serachText)
				.stream().map(this::mapToProductResponse)
				.toList();
		
		return ResponseEntity.status(HttpStatus.OK.value())
				.body(responseStructure.setStatuscode(HttpStatus.OK.value())
						.setMessage("the product is found")
						.setData(products));
	}
	


	
	
	
	

}