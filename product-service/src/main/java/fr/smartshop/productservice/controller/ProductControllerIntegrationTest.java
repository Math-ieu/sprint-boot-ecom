// package fr.smartshop.productservice.controller;


// // import static org.hamcrest.Matchers.*;
// // import static org.mockito.Mockito.*;

// import java.math.BigDecimal;
// import java.util.Optional;

// import com.fasterxml.jackson.databind.ObjectMapper;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;

// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import org.springframework.http.MediaType;

// import fr.smartshop.productservice.dto.ProductDTO;
// import fr.smartshop.productservice.model.Category;
// import fr.smartshop.productservice.model.Product;
// import fr.smartshop.productservice.repository.ProductRepository;
// import fr.smartshop.productservice.model.ProductStatus;


// @SpringBootTest
// @AutoConfigureMockMvc
// class ProductControllerIntegrationTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private ProductRepository productRepository;

//     @Autowired
//     private ObjectMapper objectMapper;

//     private Product testProduct;

//     @BeforeEach
//     void setUp() {
//         productRepository.deleteAll();

//         Category category = new Category();
//         category.setName("Test Category");

//         testProduct = new Product();
//         testProduct.setName("Test Product");
//         testProduct.setPrice(new BigDecimal("99.99"));
//         testProduct.setStockQuantity(10);
//         testProduct.setCategory(category);

//         testProduct = productRepository.save(testProduct);
//     }

//     @Test 
//     void getProducts_Success() throws Exception { 
//         mockMvc.perform(get("/api/products") 
//                 .contentType(MediaType.APPLICATION_JSON)) 
//                 .andExpect(status().isOk()) 
//                 .andExpect(jsonPath("$.content", hasSize(1))) 
//                 .andExpect(jsonPath("$.content[0].name").value("Test Product")); 
//     }

//     @Test
//     void createProduct_Success() throws Exception {
//         ProductDTO productDTO = new ProductDTO();
//         productDTO.setName("New Product");
//         productDTO.setPrice(new BigDecimal("149.99"));
//         productDTO.setStockQuantity(5);

//         mockMvc.perform(post("/api/products")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(productDTO)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.name").value("New Product"));
//     }

//     @Test
//     void updateProduct_Success() throws Exception {
//         ProductDTO updateDTO = new ProductDTO();
//         updateDTO.setName("Updated Product");
//         updateDTO.setPrice(new BigDecimal("199.99"));

//         mockMvc.perform(put("/api/products/{id}", testProduct.getId())
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(updateDTO)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.name").value("Updated Product"));
//     }

//     @Test
//     void deleteProduct_Success() throws Exception {
//         mockMvc.perform(delete("/api/products/{id}", testProduct.getId()))
//                 .andExpect(status().isOk());

//         Optional<Product> deletedProduct = productRepository.findById(testProduct.getId());
//         assertTrue(deletedProduct.isPresent());
//         assertEquals(ProductStatus.INACTIVE,
//                 deletedProduct.get().getStatus());
//     }
// }