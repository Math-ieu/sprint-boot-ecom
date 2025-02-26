package fr.smartshop.productservice.repository;

import fr.smartshop.productservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
  
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Trouver des produits par l'ID de leur catégorie.
     * 
     * @param categoryId ID de la catégorie
     * @return Liste des produits appartenant à la catégorie
     */
    List<Product> findByCategoryId(Long categoryId);

    /**
     * Trouver un produit par son SKU (Stock Keeping Unit).
     * 
     * @param sku Identifiant unique du produit
     * @return Un Optional contenant le produit s'il existe
     */
    Optional<Product> findBySku(String sku);

    /**
     * Rechercher des produits en fonction d'un mot-clé dans le nom ou la description.
     * 
     * @param keyword Mot-clé à rechercher
     * @param pageable Objet pour la pagination
     * @return Page contenant les produits correspondants
     */
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> search(@Param("keyword") String keyword, Pageable pageable);

   /*  @Query("SELECT p FROM Product p WHERE " +
        "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
        "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> search(@Param("keyword") String keyword, Pageable pageable); */

    /**
     * Trouver les produits ayant un stock inférieur à un seuil donné.
     * 
     * @param threshold Seuil de stock
     * @return Liste des produits en faible stock
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity < :threshold")
    List<Product> findLowStockProducts(@Param("threshold") int threshold);
}
