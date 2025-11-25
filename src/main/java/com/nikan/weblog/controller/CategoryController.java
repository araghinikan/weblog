package com.nikan.weblog.controller;

import com.nikan.weblog.dto.CategoryDto;
import com.nikan.weblog.model.Category;
import com.nikan.weblog.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    private Category convertToEntity(CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.name());
        category.setDescription(dto.description());
        return category;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable int id) {
        Optional<Category> category = categoryService.findById(id);
        return category.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Category> save(@Valid @RequestBody CategoryDto categoryDto) {
        Category category = convertToEntity(categoryDto);
        Category savedCategory = categoryService.save(category);
        return ResponseEntity.ok(savedCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable int id, @Valid @RequestBody CategoryDto categoryDto) {
        Optional<Category> optionalCategory = categoryService.findById(id);
        if (optionalCategory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Category categoryToUpdate = optionalCategory.get();
        categoryToUpdate.setName(categoryDto.name());
        categoryToUpdate.setDescription(categoryDto.description());
        Category updatedCategory = categoryService.save(categoryToUpdate);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        categoryService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
