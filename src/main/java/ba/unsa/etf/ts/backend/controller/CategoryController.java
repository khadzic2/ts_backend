package ba.unsa.etf.ts.backend.controller;

import ba.unsa.etf.ts.backend.model.Category;
import ba.unsa.etf.ts.backend.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/category")
    public ResponseEntity<List<Category>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Object> getCategoryById(@PathVariable Integer id){
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    @PostMapping("/category")
    public ResponseEntity<Object> addCategory(@RequestBody @Valid Category category){
        return new ResponseEntity<>(categoryService.addCategory(category), HttpStatus.CREATED);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Integer id){
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

    @DeleteMapping("/category")
    public ResponseEntity<Object> deleteAllCategories(){
        return ResponseEntity.ok(categoryService.deleteAll());
    }
}