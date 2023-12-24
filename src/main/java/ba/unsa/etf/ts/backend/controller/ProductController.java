package ba.unsa.etf.ts.backend.controller;

import ba.unsa.etf.ts.backend.request.AddProductRequest;
import ba.unsa.etf.ts.backend.request.UpdateProductRequest;
import ba.unsa.etf.ts.backend.services.ProductService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product")
    public ResponseEntity<Object> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable Integer id){
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<Object> updateProductById(@PathVariable Integer id, @RequestBody @Valid UpdateProductRequest newProduct){
        return ResponseEntity.ok(productService.updateProduct(id,newProduct));
    }

    @PostMapping("/product")
    public ResponseEntity<Object> addProduct(@RequestBody @Valid AddProductRequest addProductRequest){
        return new ResponseEntity<>(productService.addProduct(addProductRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Integer id){
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @DeleteMapping("/product")
    public ResponseEntity<Object> deleteAllProducts(){
        return ResponseEntity.ok(productService.deleteAll());
    }

//    @DeleteMapping("/product/category/{id}")
//    public ResponseEntity<Object> deleteAllProductsByCategoryId(@PathVariable Integer id){
//        return ResponseEntity.ok(productService.deleteAllByCategoryId(id));
//    }
}
