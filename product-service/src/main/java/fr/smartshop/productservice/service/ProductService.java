
package fr.smartshop.productservice.service;

import fr.smartshop.productservice.dto.ProductDTO;
import fr.smartshop.productservice.exception.ResourceNotFoundException;
import fr.smartshop.productservice.model.Category;
import fr.smartshop.productservice.model.Product;
import fr.smartshop.productservice.repository.CategoryRepository;
import fr.smartshop.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException; 
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    // private final ProductImageRepository productImageRepository;
    private final ModelMapper modelMapper;

    private String saveImage(MultipartFile imageFile, ProductDTO productDTO) throws IOException {
        if (imageFile.isEmpty()) {
            throw new IOException("Le fichier image est vide.");
        }

        // Obtenir l'extension d'origine
        String originalFileName = imageFile.getOriginalFilename();
        String fileExtension = "";

        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        // Nom personnalisé avec l'extension d'origine
        String fileName = "image_" + generateSku(productDTO.getName()) + fileExtension;
        Path filePath = Paths.get(uploadDir, fileName);

        Files.createDirectories(filePath.getParent()); // Créer le répertoire si nécessaire
        Files.write(filePath, imageFile.getBytes()); // Sauvegarder le fichier

        return fileName;
    }

    /**
     * Crée un nouveau produit.
     * 
     * @param productDTO Les données du produit à créer
     * @return Le produit créé sous forme de DTO
     */
    public ProductDTO createProduct(ProductDTO productDTO) throws IOException {

        log.info("Creating new product: {}", productDTO.getName());

        // Valider la catégorie
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        String imageName = null;

        if (productDTO.getImageFile() != null && !productDTO.getImageFile().isEmpty()) {
            imageName = saveImage(productDTO.getImageFile(), productDTO);
        }
        // Mapper le DTO vers l'entité
        Product product = modelMapper.map(productDTO, Product.class);
        product.setCategory(category);
        product.setSku(generateSku(productDTO.getName()));
        product.setCreatedAt(LocalDateTime.now());
        product.setImageName(imageName);

        // Sauvegarder le produit dans la bd
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
                    product.setUpdatedAt(LocalDateTime.now());
                    Product updated = productRepository.save(product);
                    return modelMapper.map(updated, ProductDTO.class);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    /**
     * Supprime un produit en le désactivant.
     * 
     */
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        productRepository.deleteById(id);
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

        /*
         * if (products.isEmpty()) {
         * log.warn("No products found in database");
         * throw new ResourceNotFoundException("No products found.");
         * }
         */

        // Mapper la liste des entités Product en DTO
        return products.map(product -> modelMapper.map(product, ProductDTO.class));
    }

    public ProductDTO getProduct(Long id) {
        log.info("Fetching product with ID: {}", id);

        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            log.error("Product not found with ID: {}", id);
            throw new ResourceNotFoundException("Product not found");
        }

        Product product = productOptional.get();
        return modelMapper.map(product, ProductDTO.class);
    }

}
