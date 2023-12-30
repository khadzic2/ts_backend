package ba.unsa.etf.ts.backend.services;

import ba.unsa.etf.ts.backend.exception.BadRequestException;
import ba.unsa.etf.ts.backend.exception.NotFoundException;
import ba.unsa.etf.ts.backend.model.Product;
import ba.unsa.etf.ts.backend.model.Cart;
import ba.unsa.etf.ts.backend.repository.ProductRepository;
import ba.unsa.etf.ts.backend.repository.CartRepository;
import ba.unsa.etf.ts.backend.request.AddCartProductRequest;
import ba.unsa.etf.ts.backend.request.UpdateCartProductRequest;
import ba.unsa.etf.ts.backend.response.GetCartProductsResponse;
import ba.unsa.etf.ts.backend.security.entity.User;
import ba.unsa.etf.ts.backend.security.repository.UserRepository;
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

    private List<Cart> getAllUsercartsByUserId(String email){
        User user = userRepository.findByEmail(email).orElseThrow();
        return cartRepository.findAllByUser_Id(user.getId());
    }

    public GetCartProductsResponse getCartByUser(String email){
        double amount = 0.0;
        List<Cart> products = getAllUsercartsByUserId(email);
        for (Cart cart: products) {
            amount += cart.getQuantity()*cart.getProduct().getPrice();
        }

        return new GetCartProductsResponse(products,amount);
    }

    public Cart addUsercart(AddCartProductRequest addCartProductRequest){
        User user = userRepository.findByEmail(addCartProductRequest.getUserEmail()).orElseThrow();
        Product product = productRepository.findById(addCartProductRequest.getProductId()).orElseThrow(()->new NotFoundException("Product by id:"+addCartProductRequest.getProductId()+" does not exist"));

        Cart exist = null;
        List<Cart> products = getAllUsercartsByUserId(addCartProductRequest.getUserEmail());
        if(products.size() > 0) {
            List<Cart> filterProducts = products.stream().filter(cart -> cart.getProduct().getId().equals(product.getId()) && cart.getSize().equals(addCartProductRequest.getSize())).toList();
            if (filterProducts.size() > 0)
                exist = filterProducts.get(0);
        }

        Cart usercart = new Cart(0,product,user, addCartProductRequest.getQuantity(), addCartProductRequest.getSize());
        if(exist != null){
            usercart.setId(exist.getId());
            usercart.setQuantity(usercart.getQuantity() + exist.getQuantity());
        }

        if(usercart.getQuantity() > usercart.getProduct().getQuantity()) throw new BadRequestException("The desired quantity of products exceeds the number of products in the stock.");

        return cartRepository.save(usercart);
    }

    public Cart getUsercart(Integer id){
        return cartRepository.findById(id).orElseThrow(()->new NotFoundException("Usercart by id:"+id+" does not exist."));
    }

//    public Cart updateUsercart(Integer id, UpdateCartProductRequest newUsercart){
//        Cart updateCart = cartRepository.findById(id).orElseThrow(()->new NotFoundException("Usercart by id:"+id+" does not exist."));
//
//        updateCart.setQuantity(newUsercart.getQuantity());
//
//        return cartRepository.save(updateCart);
//    }

    public Cart updateCartProductUser(Integer productId, UpdateCartProductRequest newUsercart, String email){
        List<Cart> products = getAllUsercartsByUserId(email);
        System.out.println(products.size());

        Cart updateCart = null;

        if(products.size() > 0){
            List<Cart> filterProducts = products.stream().filter(cart -> cart.getProduct().getId().equals(productId)).toList();
            if (filterProducts.size() > 0){
                updateCart = filterProducts.get(0);
            }
        }

        if(updateCart == null)
            throw new NotFoundException("Usercart by product id:" + productId + " does not exist.");

        updateCart.setQuantity(newUsercart.getQuantity());

        if(updateCart.getQuantity() > updateCart.getProduct().getQuantity()) throw new BadRequestException("The desired quantity of products exceeds the number of products in the stock.");

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

    public String deleteUsercartByUserId(String email){
        List<Cart> usercarts = getAllUsercartsByUserId(email);
        cartRepository.deleteAll(usercarts);
        return "Products from user's cart successfully deleted!";
    }
}
