package ba.unsa.etf.ts.backend.controller;

import ba.unsa.etf.ts.backend.request.AddCartProductRequest;
import ba.unsa.etf.ts.backend.request.UpdateCartProductRequest;
import ba.unsa.etf.ts.backend.security.service.JwtService;
import ba.unsa.etf.ts.backend.services.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/cart", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:3000")
public class CartController {

    private final CartService cartService;
    private final JwtService jwtService;

    public CartController(CartService cartService, JwtService jwtService) {
        this.cartService = cartService;
        this.jwtService = jwtService;
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/user")
    public ResponseEntity<Object> getAllByUserId(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        var email = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(cartService.getCartByUser(email));
    }
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ResponseEntity<Object> addUsercart(@RequestBody @Valid AddCartProductRequest addCartProductRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        var email = jwtService.extractUsername(token.substring(7));
        addCartProductRequest.setUserEmail(email);
        return new ResponseEntity<>(cartService.addUsercart(addCartProductRequest), HttpStatus.CREATED);
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUsercartById(@PathVariable Integer id){
        return ResponseEntity.ok(cartService.getUsercart(id));
    }

//    @PreAuthorize("hasAuthority('USER')")
//    @PutMapping("/{id}")
//    public ResponseEntity<Object> updateUsercart(@PathVariable Integer id, @RequestBody @Valid UpdateCartProductRequest newCart){
//        return ResponseEntity.ok(cartService.updateUsercart(id,newCart));
//    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/product/{productId}")
    public ResponseEntity<Object> updateCartProductUser(@PathVariable Integer productId, @RequestBody @Valid UpdateCartProductRequest newCart, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        var email = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(cartService.updateCartProductUser(productId,newCart,email));
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUsercart(@PathVariable Integer id){
        return ResponseEntity.ok(cartService.deleteUsercart(id));
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/user")
    public ResponseEntity<Object> deleteAllByUserId(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        var email = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(cartService.deleteUsercartByUserId(email));
    }
}
