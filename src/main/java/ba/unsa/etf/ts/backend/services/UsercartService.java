package ba.unsa.etf.ts.backend.services;

import ba.unsa.etf.ts.backend.exception.NotFoundException;
import ba.unsa.etf.ts.backend.model.Product;
import ba.unsa.etf.ts.backend.model.User;
import ba.unsa.etf.ts.backend.model.Usercart;
import ba.unsa.etf.ts.backend.repository.ProductRepository;
import ba.unsa.etf.ts.backend.repository.UserRepository;
import ba.unsa.etf.ts.backend.repository.UsercartRepository;
import ba.unsa.etf.ts.backend.request.AddCartProductRequest;
import ba.unsa.etf.ts.backend.request.UpdateCartProductRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsercartService {

    private final UsercartRepository usercartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public UsercartService(UsercartRepository usercartRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.usercartRepository = usercartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<Usercart> getAllUsercartsByUserId(Integer id){
        return usercartRepository.findAllByUser_Id(id);
    }

    public Usercart addUsercart(AddCartProductRequest addCartProductRequest){
        User user = userRepository.findById(addCartProductRequest.getUserId()).orElseThrow(()->new NotFoundException("User by id:"+addCartProductRequest.getUserId()+" does not exist"));
        Product product = productRepository.findById(addCartProductRequest.getProductId()).orElseThrow(()->new NotFoundException("Product by id:"+addCartProductRequest.getProductId()+" does not exist"));
        Usercart usercart = new Usercart(0,product,user, addCartProductRequest.getQuantity(), addCartProductRequest.getSize());
        return usercartRepository.save(usercart);
    }

    public Usercart getUsercart(Integer id){
        return usercartRepository.findById(id).orElseThrow(()->new NotFoundException("Usercart by id:"+id+" does not exist."));
    }

    public Usercart updateUsercart(Integer id, UpdateCartProductRequest newUsercart){
        Usercart updateUsercart = usercartRepository.findById(id).orElseThrow(()->new NotFoundException("Usercart by id:"+id+" does not exist."));

        updateUsercart.setQuantity(newUsercart.getQuantity());
        updateUsercart.setSize(newUsercart.getSize());

        return usercartRepository.save(updateUsercart);
    }

    public String deleteUsercart(Integer id){
        Usercart usercart = usercartRepository.findById(id).orElse(null);
        if(usercart == null){
            throw new NotFoundException("Usercart by id:"+id+" does not exist");
        }
        usercartRepository.delete(usercart);
        return "Usercart successfully deleted!";
    }

    public String deleteUsercartByUserId(Integer userId){
        List<Usercart> usercarts = usercartRepository.findAll().stream().filter(usercart -> usercart.getUser().getId().equals(userId)).collect(Collectors.toList());
        usercartRepository.deleteAll(usercarts);
        return "Products from user's cart successfully deleted!";
    }
}
