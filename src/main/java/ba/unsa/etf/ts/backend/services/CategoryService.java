package ba.unsa.etf.ts.backend.services;

import ba.unsa.etf.ts.backend.exception.NotFoundException;
import ba.unsa.etf.ts.backend.model.Category;
import ba.unsa.etf.ts.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
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
