package fr.smartshop.productservice;

// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;

// @SpringBootTest

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import fr.smartshop.productservice.model.Category;
import fr.smartshop.productservice.model.Product;
import fr.smartshop.productservice.dto.ProductDTO;
import fr.smartshop.productservice.exception.ResourceNotFoundException;
import fr.smartshop.productservice.repository.CategoryRepository;
import fr.smartshop.productservice.repository.ProductRepository;
import fr.smartshop.productservice.service.ProductService;
import fr.smartshop.productservice.model.ProductStatus;



@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private ProductService productService;

	private Product product;
	private ProductDTO productDTO;
	private Category category;

	@BeforeEach
	void setUp() {
		category = new Category();
		category.setId(1L);
		category.setName("Electronics");

		product = new Product();
		product.setId(1L);
		product.setName("Test Product");
		product.setPrice(new BigDecimal("99.99"));
		product.setStockQuantity(10);
		product.setCategory(category);

		productDTO = new ProductDTO();
		productDTO.setName("Test Product");
		productDTO.setPrice(new BigDecimal("99.99"));
		productDTO.setStockQuantity(10);
		productDTO.setCategoryId(1L);
	}

	@Test
	void createProduct_Success() {
		// Given

		when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
		when(modelMapper.map(productDTO,
				Product.class)).thenReturn(product);

		when(productRepository.save(any(Product.class))).thenReturn(product);
		when(modelMapper.map(product,
				ProductDTO.class)).thenReturn(productDTO);

		// When
		ProductDTO result = productService.createProduct(productDTO);

		// Then
		assertNotNull(result);
		assertEquals(productDTO.getName(), result.getName());
		verify(productRepository).save(any(Product.class));
	}

	@Test
	void createProduct_CategoryNotFound() {
		// Given
		when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(ResourceNotFoundException.class, () -> {
			productService.createProduct(productDTO);
		});
	}

	@Test
	void updateProduct_Success() {
		// Given

		when(productRepository.findById(1L)).thenReturn(Optional.of(product));

		when(productRepository.save(any(Product.class))).thenReturn(product);
		when(modelMapper.map(product,
				ProductDTO.class)).thenReturn(productDTO);

		// When
		ProductDTO result = productService.updateProduct(1L, productDTO);

		// Then
		assertNotNull(result);
		assertEquals(productDTO.getName(), result.getName());
		verify(productRepository).save(any(Product.class));
	}

	@Test
	void deleteProduct_Success() {
		// Given

		when(productRepository.findById(1L)).thenReturn(Optional.of(product));

		// When
		productService.deleteProduct(1L);

		// Then
		assertEquals(ProductStatus.INACTIVE, product.getStatus());
		verify(productRepository).save(product);
	}

	@Test
	void searchProducts_Success() {
		// Given
		Page<Product> productPage = new PageImpl<>(List.of(product));
		when(productRepository.search(anyString(), any(Pageable.class)))
				.thenReturn(productPage);
		when(modelMapper.map(product,
				ProductDTO.class)).thenReturn(productDTO);

		// When
		Page<ProductDTO> result = productService.searchProducts("test",
				PageRequest.of(0, 10));

		// Then
		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals(1, result.getContent().size());
	}
}