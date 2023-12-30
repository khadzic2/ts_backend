package ba.unsa.etf.ts.backend.controller;

import ba.unsa.etf.ts.backend.request.UpdateOrdersRequest;
import ba.unsa.etf.ts.backend.security.service.JwtService;
import ba.unsa.etf.ts.backend.services.OrdersService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:3000")
public class OrdersController {
    private final OrdersService ordersService;
    private final JwtService jwtService;
    public OrdersController(OrdersService ordersService, JwtService jwtService) {
        this.ordersService = ordersService;
        this.jwtService = jwtService;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<Object> getAllOrders(){
        return ResponseEntity.ok(ordersService.getAllOrders());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable Integer id){
        return ResponseEntity.ok(ordersService.getOrder(id));
    }
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/user")
    public ResponseEntity<Object> getOrdersByUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        String email = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(ordersService.getOrderByUser(email));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateOrderById(@PathVariable Integer id, @RequestBody @Valid UpdateOrdersRequest newOrder){
        return ResponseEntity.ok(ordersService.updateOrder(id,newOrder));
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ResponseEntity<Object> createOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        String email = jwtService.extractUsername(token.substring(7));
        return new ResponseEntity<>(ordersService.createOrder(email), HttpStatus.CREATED);
    }
//    @PreAuthorize("hasAuthority('USER')")
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Object> deleteOrders(@PathVariable Integer id){
//        return ResponseEntity.ok(ordersService.deleteOrder(id));
//    }
}
