package fr.smartshop.productservice.controller.api;

import fr.smartshop.productservice.dto.ProductDTO;
import fr.smartshop.productservice.service.ProductService;
import fr.smartshop.productservice.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product", description = "Product management APIs")
@Slf4j
public class ProductRestController {
 
    private final ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }
  
    /**
     * Crée un nouveau produit
     */
    @PostMapping
    @Operation(summary = "Create new product")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        log.info("Received request to create a new product with name: {}", productDTO.getName());
        try {
            ProductDTO createdProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(createdProduct);
        } catch (Exception e) {
            log.error("Error creating product", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Récupère un produit par son ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        log.info("Fetching product with ID: {}", id);
        try {
            ProductDTO productDTO = productService.getProduct(id);
            return ResponseEntity.ok(productDTO);
        } catch (ResourceNotFoundException e) {
            log.error("Product not found", e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Recherche de produits avec pagination
     */
    @GetMapping
    @Operation(summary = "Search products")
    public ResponseEntity<Page<ProductDTO>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Searching for products with keyword: {} at page {} with size {}", keyword, page, size);
        return ResponseEntity.ok(productService.searchProducts(keyword, PageRequest.of(page, size)));
    }
}
