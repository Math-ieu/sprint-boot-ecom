package fr.smartshop.productservice.repository;

import fr.smartshop.productservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * Trouver une catégorie par son nom.
     * 
     * @param name Nom de la catégorie 
     * @return Un Optional contenant la catégorie si elle existe
     */
    Optional<Category> findByName(String name);

    /**
     * Rechercher les catégories contenant un mot-clé dans leur nom.
     * 
     * @param keyword Mot-clé à rechercher
     * @return Liste des catégories correspondantes
     */
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Category> searchByName(@Param("keyword") String keyword);

    /**
     * Obtenir toutes les catégories avec le nombre de produits associé.
     * 
     * @return Liste des catégories avec le nombre de produits
     */
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.products p GROUP BY c.id ORDER BY COUNT(p) DESC")
    List<Category> findAllWithProductCount();
}
