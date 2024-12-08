package fr.smartshop.productservice.controller;

import fr.smartshop.productservice.dto.CategoryDTO;
import fr.smartshop.productservice.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String createCategory(@Valid @ModelAttribute("category") CategoryDTO categoryDTO,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "categories/form";
        }

        try {
            categoryService.createCategory(categoryDTO);
            redirectAttributes.addFlashAttribute("success", "Category created successfully");
            return "redirect:/categories";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/categories/new";
        }
    }
}
