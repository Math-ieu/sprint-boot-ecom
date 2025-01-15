package fr.smartshop.productservice.service;

import fr.smartshop.productservice.dto.CategoryDTO;
import fr.smartshop.productservice.dto.ProductDTO;
import fr.smartshop.productservice.exception.ResourceNotFoundException;
import fr.smartshop.productservice.model.Category;
import fr.smartshop.productservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CategoryService {  

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    /**
     * Create a new category
     */
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        log.info("Creating new category: {}", categoryDTO.getName());
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    /**
     * Update an existing category by ID
     */
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {   
        log.info("Updating category with ID: {}", id);
        return categoryRepository.findById(id)
                .map(category -> {
                    modelMapper.map(categoryDTO, category);
                    Category updatedCategory = categoryRepository.save(category);
                    return modelMapper.map(updatedCategory, CategoryDTO.class);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
    }

    /**
     * Delete a category by ID
     */
    public void deleteCategory(Long id) {
        log.info("Deleting category with ID: {}", id);
        categoryRepository.deleteById(id);
    }

    /**
     * Find a category by ID
     */
    public CategoryDTO getCategoryById(Long id) {
        log.info("Fetching category with ID: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
        return modelMapper.map(category, CategoryDTO.class);
    }

    /**
     * List all categories with pagination
     */
    public Page<CategoryDTO> getAllCategories(Pageable pageable) {
        log.info("Fetching all categories with pagination");
        return categoryRepository.findAll(pageable)
                .map(category -> modelMapper.map(category, CategoryDTO.class));
    }

    /**
     * List all categories without pagination
     */
    public List<CategoryDTO> getAllCategories() {
        log.info("Fetching all categories");
        return categoryRepository.findAll()
                .stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());

    }


     /**
     * Recherche des produits par mot-clé avec pagination.
     * 
     * @param keyword  Le mot-clé à rechercher
     * @return Une page de produits correspondant au mot-clé
     */
    public List<CategoryDTO> searchCategories(String keyword) {
        return categoryRepository.searchByName(keyword)
                .stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());

    }


}
