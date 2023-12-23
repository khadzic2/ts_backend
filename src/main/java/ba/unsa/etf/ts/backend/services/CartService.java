package ba.unsa.etf.ts.backend.services;

import ba.unsa.etf.ts.backend.exception.NotFoundException;
import ba.unsa.etf.ts.backend.model.Product;
import ba.unsa.etf.ts.backend.model.User;
import ba.unsa.etf.ts.backend.model.Cart;
import ba.unsa.etf.ts.backend.repository.ProductRepository;
import ba.unsa.etf.ts.backend.repository.UserRepository;
import ba.unsa.etf.ts.backend.repository.CartRepository;
import ba.unsa.etf.ts.backend.request.AddCartProductRequest;
import ba.unsa.etf.ts.backend.request.UpdateCartProductRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<Cart> getAllUsercartsByUserId(Integer id){
        return cartRepository.findAllByUser_Id(id);
    }

    public Cart addUsercart(AddCartProductRequest addCartProductRequest){
        User user = userRepository.findById(addCartProductRequest.getUserId()).orElseThrow(()->new NotFoundException("User by id:"+addCartProductRequest.getUserId()+" does not exist"));
        Product product = productRepository.findById(addCartProductRequest.getProductId()).orElseThrow(()->new NotFoundException("Product by id:"+addCartProductRequest.getProductId()+" does not exist"));
        Cart usercart = new Cart(0,product,user, addCartProductRequest.getQuantity(), addCartProductRequest.getSize());
        return cartRepository.save(usercart);
    }

    public Cart getUsercart(Integer id){
        return cartRepository.findById(id).orElseThrow(()->new NotFoundException("Usercart by id:"+id+" does not exist."));
    }

    public Cart updateUsercart(Integer id, UpdateCartProductRequest newUsercart){
        Cart updateCart = cartRepository.findById(id).orElseThrow(()->new NotFoundException("Usercart by id:"+id+" does not exist."));

        updateCart.setQuantity(newUsercart.getQuantity());
        updateCart.setSize(newUsercart.getSize());

        return cartRepository.save(updateCart);
    }

    public String deleteUsercart(Integer id){
        Cart usercart = cartRepository.findById(id).orElse(null);
        if(usercart == null){
            throw new NotFoundException("Usercart by id:"+id+" does not exist");
        }
        cartRepository.delete(usercart);
        return "Usercart successfully deleted!";
    }

    public String deleteUsercartByUserId(Integer userId){
        List<Cart> usercarts = cartRepository.findAll().stream().filter(usercart -> usercart.getUser().getId().equals(userId)).collect(Collectors.toList());
        cartRepository.deleteAll(usercarts);
        return "Products from user's cart successfully deleted!";
    }
}
