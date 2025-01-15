package fr.smartshop.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.smartshop.productservice.model.ProductImage;

public interface ProductImageRepository  extends JpaRepository<ProductImage,Long>{
    
}
