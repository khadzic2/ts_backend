package ba.unsa.etf.ts.backend.services;

import ba.unsa.etf.ts.backend.exception.NotFoundException;
import ba.unsa.etf.ts.backend.model.Category;
import ba.unsa.etf.ts.backend.model.Product;
import ba.unsa.etf.ts.backend.repository.CategoryRepository;
import ba.unsa.etf.ts.backend.repository.ProductRepository;
import ba.unsa.etf.ts.backend.request.GetProductsByCategoryName;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public Category addCategory(Category category){
        return categoryRepository.save(category);
    }

    public Category getCategory(Integer id){
        return categoryRepository.findById(id).orElseThrow(()-> new NotFoundException("Category by id:"+id+" does not exist."));
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public List<Product> getProductsByCategoryName(GetProductsByCategoryName name){
        return productRepository.findAllByCategory_Name(name.getName());
    }

    public String deleteCategory(Integer id){
        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null) throw new NotFoundException("Category by id:"+id+" does not exist");
        categoryRepository.deleteById(id);
        return "Successfully deleted!";
    }

    public String deleteAll(){
        categoryRepository.deleteAll();
        return "Successfully deleted!";
    }
}
