package ba.unsa.etf.ts.backend.controller;

import ba.unsa.etf.ts.backend.request.AddCartProductRequest;
import ba.unsa.etf.ts.backend.request.UpdateCartProductRequest;
import ba.unsa.etf.ts.backend.services.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/cart", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Object> getAllByUserId(@PathVariable Integer userId){
        return ResponseEntity.ok(cartService.getAllUsercartsByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Object> addUsercart(@RequestBody @Valid AddCartProductRequest addCartProductRequest){
        return new ResponseEntity<>(cartService.addUsercart(addCartProductRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUsercartById(@PathVariable Integer id){
        return ResponseEntity.ok(cartService.getUsercart(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUsercart(@PathVariable Integer id, @RequestBody @Valid UpdateCartProductRequest newCart){
        return ResponseEntity.ok(cartService.updateUsercart(id,newCart));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUsercart(@PathVariable Integer id){
        return ResponseEntity.ok(cartService.deleteUsercart(id));
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Object> deleteAllByUserId(@PathVariable Integer userId){
        return ResponseEntity.ok(cartService.deleteUsercartByUserId(userId));
    }
}
