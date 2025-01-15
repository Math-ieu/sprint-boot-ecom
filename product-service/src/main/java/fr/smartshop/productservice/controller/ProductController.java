package fr.smartshop.productservice.controller;


import fr.smartshop.productservice.dto.ProductDTO;
import fr.smartshop.productservice.exception.ResourceNotFoundException;

import fr.smartshop.productservice.service.ProductService;
import fr.smartshop.productservice.service.CategoryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
                               @RequestParam(defaultValue = "6") int size) {
        log.info("Fetching product list with pagination: page {}, size {}", page, size);

        Page<ProductDTO> products = productService.getAllProducts(PageRequest.of(page, size));
        log.info("Fetched products: {}", products.getContent());
        model.addAttribute("products", products);
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
            log.info("id",productDTO.getId());
            if(productDTO.getId() == null){
                redirectAttributes.addFlashAttribute("success", "Product created successfully");
            }else{
                redirectAttributes.addFlashAttribute("success", "Product updated successfully");
            }
            productService.createProduct(productDTO);
            return "redirect:/products";
        } catch (Exception e) {
            log.error("Error occurred while creating a product: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/products/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String showUpdateForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Récupération de la catégorie existante à partir du service
            ProductDTO productDTO = productService.getProduct(id);
            model.addAttribute("product", productDTO);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "products/form";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/products";
        }
    }

    @GetMapping("/search/{keyword}")
    public String searchProductsByName(@RequestParam("keyword") String keyword,Model model,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "6") int size) {
        Page<ProductDTO> products = productService.searchProducts(keyword, PageRequest.of(page, size));
        log.info("Fetched products: {}", products);
        model.addAttribute("products", products);
        return "products/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteProducts(@PathVariable Long id) {
        log.info("Deleting products with id: {}", id);
        productService.deleteProduct(id);
        return "redirect:/products";
    }
    
}
