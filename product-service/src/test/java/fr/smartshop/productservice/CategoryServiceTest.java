package fr.smartshop.productservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import fr.smartshop.productservice.dto.CategoryDTO;
import fr.smartshop.productservice.model.Category;
import fr.smartshop.productservice.repository.CategoryRepository;
import fr.smartshop.productservice.service.CategoryService;



@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        categoryDTO = new CategoryDTO();
        categoryDTO.setName("Electronics");
    }

    @Test
    void createCategory_Success() {

        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(modelMapper.map(categoryDTO,
                Category.class)).thenReturn(category);
        when(modelMapper.map(category,
                CategoryDTO.class)).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.createCategory(categoryDTO);

        assertNotNull(result);
        assertEquals(categoryDTO.getName(), result.getName());
    }
}