
package fr.smartshop.productservice.service;

import fr.smartshop.productservice.dto.ProductDTO;
import fr.smartshop.productservice.exception.ResourceNotFoundException;
import fr.smartshop.productservice.model.Category;
import fr.smartshop.productservice.model.Product;
import fr.smartshop.productservice.model.ProductStatus;
import fr.smartshop.productservice.repository.CategoryRepository;
import fr.smartshop.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    /**
     * Crée un nouveau produit.
     * 
     * @param productDTO Les données du produit à créer
     * @return Le produit créé sous forme de DTO
     */
    public ProductDTO createProduct(ProductDTO productDTO) {
        log.info("Creating new product: {}", productDTO.getName());

        // Valider la catégorie
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        // Mapper le DTO vers l'entité
        Product product = modelMapper.map(productDTO, Product.class);
        product.setCategory(category);
        product.setSku(generateSku(productDTO.getName()));

        // Sauvegarder et retourner le produit
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    /**
     * Met à jour un produit existant.
     * 
     * @param id         L'ID du produit à mettre à jour
     * @param productDTO Les données mises à jour
     * @return Le produit mis à jour sous forme de DTO
     */
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        return productRepository.findById(id)
                .map(product -> {
                    // Mettre à jour les champs
                    modelMapper.map(productDTO, product);
                    Product updated = productRepository.save(product);
                    return modelMapper.map(updated, ProductDTO.class);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    /**
     * Supprime un produit en le désactivant.
     * 
     * @param id L'ID du produit à supprimer
     */
    public void deleteProduct(Long id) {
        productRepository.findById(id).ifPresent(product -> {
            product.setStatus(ProductStatus.INACTIVE);
            productRepository.save(product);
        });
    }

    /**
     * Recherche des produits par mot-clé avec pagination.
     * 
     * @param keyword  Le mot-clé à rechercher
     * @param pageable Les informations de pagination
     * @return Une page de produits correspondant au mot-clé
     */
    public Page<ProductDTO> searchProducts(String keyword, Pageable pageable) {
        return productRepository.search(keyword, pageable)
                .map(product -> modelMapper.map(product, ProductDTO.class));
    }

    /**
     * Génère un SKU unique pour un produit.
     * 
     * @param productName Le nom du produit
     * @return Le SKU généré
     */
    private String generateSku(String productName) {
        return productName.substring(0, Math.min(3, productName.length())).toUpperCase()
                + "-" + System.currentTimeMillis();
    }


    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        log.info("Fetching all products with pagination");
        
        Page<Product> products = productRepository.findAll(pageable);

        if (products.isEmpty()) {
            log.warn("No products found in database");
            throw new ResourceNotFoundException("No products found.");
        }

        // Mapper la liste des entités Product en DTO
        return products.map(product -> modelMapper.map(product, ProductDTO.class));
    }


    public ProductDTO getProduct(Long id) {
        log.info("Fetching product with ID: {}", id);

        Optional <Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            log.error("Product not found with ID: {}", id);
            throw new ResourceNotFoundException("Product not found");
        }

        Product product = productOptional.get();
        return modelMapper.map(product, ProductDTO.class);
    }
}
