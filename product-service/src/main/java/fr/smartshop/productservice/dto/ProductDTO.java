package fr.smartshop.productservice.dto;

import lombok.Data;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

import  fr.smartshop.productservice.model.*;

@Data
public class ProductDTO {
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @Min(0)
    private Integer stockQuantity;

    private String sku;
    private Long categoryId;
    private List<String> imageUrls;
    private ProductStatus status;

}