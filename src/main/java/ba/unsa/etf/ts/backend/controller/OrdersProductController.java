package ba.unsa.etf.ts.backend.controller;

import ba.unsa.etf.ts.backend.request.AddOrderProductRequest;
import ba.unsa.etf.ts.backend.services.OrdersProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/orders_product", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:3000")
public class OrdersProductController {

    private final OrdersProductService ordersProductService;

    public OrdersProductController(OrdersProductService ordersProductService) {
        this.ordersProductService = ordersProductService;
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Object> getAllByOrderId(@PathVariable Integer orderId){
        return ResponseEntity.ok(ordersProductService.getAllOrdersproductsByOrderId(orderId));
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrdersproductById(@PathVariable Integer id){
        return ResponseEntity.ok(ordersProductService.getOrdersproduct(id));
    }
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ResponseEntity<Object> addOrdersproduct(@RequestBody @Valid AddOrderProductRequest newOrderproduct){
        return new ResponseEntity<>(ordersProductService.addOrdersproduct(newOrderproduct), HttpStatus.CREATED);
    }
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrdersproduct(@PathVariable Integer id){
        return  ResponseEntity.ok(ordersProductService.deleteOrdersproduct(id));
    }
}
