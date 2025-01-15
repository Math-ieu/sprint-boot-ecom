package fr.smartshop.productservice.controller;

import fr.smartshop.productservice.dto.CategoryDTO;
import fr.smartshop.productservice.dto.ProductDTO;
import fr.smartshop.productservice.exception.ResourceNotFoundException;
import fr.smartshop.productservice.service.CategoryService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/categories")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new CategoryDTO());
        return "categories/form";
    }

    @PostMapping
    public String createorUpdateCategory(@Valid @ModelAttribute("category") CategoryDTO categoryDTO,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "categories/form";
        }

        try {
            if(categoryDTO.getId() == null){
                redirectAttributes.addFlashAttribute("success", "Category created successfully");
            }else{
                redirectAttributes.addFlashAttribute("success", "Category updated successfully");
            }
            categoryService.createCategory(categoryDTO);
            return "redirect:/categories";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/categories/new";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        log.info("Deleting category with id: {}", id);
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Récupération de la catégorie existante à partir du service
            CategoryDTO categoryDTO = categoryService.getCategoryById(id);
            model.addAttribute("category", categoryDTO);
            return "categories/form";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/search/{keyword}")
    public String searchProductsByName(@RequestParam("keyword") String keyword,Model model) {
        List<CategoryDTO> categories = categoryService.searchCategories(keyword);
        log.info("Fetched categories: {}", categories);
        model.addAttribute("categories",categories);
        return "categories/list";
    }



}
