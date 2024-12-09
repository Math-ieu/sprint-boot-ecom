package fr.smartshop.productservice.controller;

import fr.smartshop.productservice.dto.ProductDTO;
import fr.smartshop.productservice.service.ProductService;
import fr.smartshop.productservice.service.CategoryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    /**
     * Liste des produits avec pagination.
     */
    @GetMapping
    public String listProducts(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching product list with pagination: page {}, size {}", page, size);

        Page<ProductDTO> products = productService.getAllProducts(PageRequest.of(page, size));
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategories());

        return "products/list";
    }

    /**
     * Afficher le formulaire pour créer un produit.
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        log.info("Showing product creation form");

        model.addAttribute("product", new ProductDTO());
        model.addAttribute("categories", categoryService.getAllCategories());

        return "products/form";
    }

    /**
     * Créer un produit après soumission du formulaire.
     */
    @PostMapping
    public String createProduct(@Valid @ModelAttribute("product") ProductDTO productDTO,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            log.error("Validation errors while creating a product");
            return "products/form";
        }

        try {
            log.info("Creating a new product: {}", productDTO.getName());
            productService.createProduct(productDTO);
            redirectAttributes.addFlashAttribute("success", "Product created successfully");
            return "redirect:/products";
        } catch (Exception e) {
            log.error("Error occurred while creating a product: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/products/new";
        }
    }
}
