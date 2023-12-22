package ba.unsa.etf.ts.backend.services;

import ba.unsa.etf.ts.backend.exception.NotFoundException;
import ba.unsa.etf.ts.backend.model.Category;
import ba.unsa.etf.ts.backend.model.Image;
import ba.unsa.etf.ts.backend.model.Product;
import ba.unsa.etf.ts.backend.repository.CategoryRepository;
import ba.unsa.etf.ts.backend.repository.ImageRepository;
import ba.unsa.etf.ts.backend.repository.ProductRepository;
import ba.unsa.etf.ts.backend.request.AddProductRequest;
import ba.unsa.etf.ts.backend.request.UpdateProductRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product addProduct(AddProductRequest addProductRequest){
        Category category = categoryRepository.findById(addProductRequest.getCategoryId()).orElseThrow(()->new NotFoundException("Category by id:"+addProductRequest.getCategoryId()+" does not exist."));
        Image image = imageRepository.findById(addProductRequest.getImageId()).orElseThrow(()->new NotFoundException("Image by id:"+addProductRequest.getImageId()+" does not exist."));
        Product product = new Product();
        product.setName(addProductRequest.getName());
        product.setInfo(addProductRequest.getInfo());
        product.setPrice(addProductRequest.getPrice());
        product.setQuantity(addProductRequest.getQuantity());
        product.setSize(addProductRequest.getSize());
        product.setColor(addProductRequest.getColor());
        product.setDate(LocalDateTime.now());
        product.setCategory(category);
        product.setImage(image);
        return productRepository.save(product);
    }

    public Product getProduct(Integer id){
        return productRepository.findById(id).orElseThrow(()->new NotFoundException("Product by id:"+id+" does not exist."));
    }

    public Product updateProduct(Integer id, UpdateProductRequest newProduct){
        Product updateProd = productRepository.findById(id).orElseThrow(()-> new NotFoundException("Product by id:"+id+" does not exist."));

        updateProd.setInfo(newProduct.getInfo());
        updateProd.setColor(newProduct.getColor());
        updateProd.setName(newProduct.getName());
        updateProd.setPrice(newProduct.getPrice());
        updateProd.setQuantity(newProduct.getQuantity());
        updateProd.setSize(newProduct.getSize());

        return productRepository.save(updateProd);
    }

    public String deleteProduct(Integer id){
        Product product = productRepository.findById(id).orElse(null);
        if(product == null) throw new NotFoundException("Product by id:"+id+" does not exist.");
        productRepository.deleteById(id);
        return "Product successfully deleted!";
    }

    public String deleteAll(){
        productRepository.deleteAll();
        return "Successfully deleted!";
    }

//    public String deleteAllByCategoryId(Integer categoryId){
//        List<Product> products = productRepository.findAll().stream().filter(product -> product.getCategory().getId().equals(categoryId)).collect(Collectors.toList());
//        productRepository.deleteAll(products);
//        return "Successfully deleted!";
//    }
}
