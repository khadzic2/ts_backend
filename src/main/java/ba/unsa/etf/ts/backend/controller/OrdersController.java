package ba.unsa.etf.ts.backend.controller;

import ba.unsa.etf.ts.backend.request.AddOrdersRequest;
import ba.unsa.etf.ts.backend.request.UpdateOrdersRequest;
import ba.unsa.etf.ts.backend.services.OrdersService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:3000")
public class OrdersController {
    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllOrders(){
        return ResponseEntity.ok(ordersService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable Integer id){
        return ResponseEntity.ok(ordersService.getOrder(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Object> getOrdersByUserId(@PathVariable Integer userId){
        return ResponseEntity.ok(ordersService.getOrderByUserId(userId));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateOrderById(@PathVariable Integer id, @RequestBody @Valid UpdateOrdersRequest newOrder){
        return ResponseEntity.ok(ordersService.updateOrder(id,newOrder));
    }

    @PostMapping
    public ResponseEntity<Object> addOrder(@RequestBody @Valid AddOrdersRequest addOrdersRequest){
        return new ResponseEntity<>(ordersService.addOrder(addOrdersRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrders(@PathVariable Integer id){
        return ResponseEntity.ok(ordersService.deleteOrder(id));
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteAllOrders(){
        return ResponseEntity.ok(ordersService.deleteAll());
    }
}
