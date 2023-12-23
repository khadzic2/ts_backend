package ba.unsa.etf.ts.backend.controller;

import ba.unsa.etf.ts.backend.request.AddCartProductRequest;
import ba.unsa.etf.ts.backend.request.UpdateCartProductRequest;
import ba.unsa.etf.ts.backend.services.UsercartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/usercart", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsercartController {

    private final UsercartService usercartService;

    public UsercartController(UsercartService usercartService) {
        this.usercartService = usercartService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Object> getAllByUserId(@PathVariable Integer userId){
        return ResponseEntity.ok(usercartService.getAllUsercartsByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Object> addUsercart(@RequestBody @Valid AddCartProductRequest addCartProductRequest){
        return new ResponseEntity<>(usercartService.addUsercart(addCartProductRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUsercartById(@PathVariable Integer id){
        return ResponseEntity.ok(usercartService.getUsercart(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUsercart(@PathVariable Integer id, @RequestBody @Valid UpdateCartProductRequest newUsercart){
        return ResponseEntity.ok(usercartService.updateUsercart(id,newUsercart));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUsercart(@PathVariable Integer id){
        return ResponseEntity.ok(usercartService.deleteUsercart(id));
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Object> deleteAllByUserId(@PathVariable Integer userId){
        return ResponseEntity.ok(usercartService.deleteUsercartByUserId(userId));
    }
}
