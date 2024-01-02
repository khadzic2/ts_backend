package ba.unsa.etf.ts.backend.services;

import ba.unsa.etf.ts.backend.exception.NotFoundException;
import ba.unsa.etf.ts.backend.model.Cart;
import ba.unsa.etf.ts.backend.model.Orders;
import ba.unsa.etf.ts.backend.repository.OrdersRepository;
import ba.unsa.etf.ts.backend.request.AddOrderProductRequest;
import ba.unsa.etf.ts.backend.request.UpdateOrdersRequest;
import ba.unsa.etf.ts.backend.response.GetCartProductsResponse;
import ba.unsa.etf.ts.backend.security.entity.User;
import ba.unsa.etf.ts.backend.security.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final UserRepository userRepository;

    private final CartService cartService;
    private final OrdersProductService ordersProductService;

    public OrdersService(OrdersRepository ordersRepository, CartService cartService, UserRepository userRepository, OrdersProductService ordersProductService) {
        this.ordersRepository = ordersRepository;
        this.cartService = cartService;
        this.userRepository = userRepository;
        this.ordersProductService = ordersProductService;
    }

    public List<Orders> getAllOrders(){
        return ordersRepository.findAll();
    }

    public Orders getOrder(Integer id){
        return ordersRepository.findById(id).orElseThrow(()->new NotFoundException("Order by id:"+id+" does not exist."));
    }

    public Orders createOrder(String email){
        User user = userRepository.findByEmail(email).orElseThrow(()->new NotFoundException("User by email:"+email+" does not exist."));
        GetCartProductsResponse getCartProductsResponse = cartService.getCartByUser(email);
        List<Cart> products = getCartProductsResponse.getProducts();

        Orders order = new Orders();
        order.setCode("O"+LocalDateTime.now().getYear()+LocalDateTime.now().getMonthValue()+LocalDateTime.now().getDayOfYear()+"T"+LocalDateTime.now().getHour()+LocalDateTime.now().getMinute());
        order.setDateAndTime(LocalDateTime.now());
        order.setShipped(false);
        order.setDelivered(false);
        order.setAmount(getCartProductsResponse.getAmount());
        order.setUser(user);

        order = ordersRepository.save(order);

        cartService.deleteUsercartByUserId(email);
        for (Cart cart : products) {
            ordersProductService.addOrdersproduct(new AddOrderProductRequest(order.getId(), cart.getProduct().getId(), cart.getQuantity(), cart.getSize()));
        }

        return order;
    }

    public Orders updateOrder(Integer id, UpdateOrdersRequest newOrder){
        Orders updateOrder = ordersRepository.findById(id).orElseThrow(()->new NotFoundException("Order by id:"+id+" does not exist."));

        updateOrder.setDelivered(newOrder.getDelivered());
        updateOrder.setShipped(newOrder.getShipped());

        return ordersRepository.save(updateOrder);
    }

//    public String deleteOrder(Integer id){
//        Orders order = ordersRepository.findById(id).orElse(null);
//        if(order == null) throw new NotFoundException("Order by id:"+id+" does not exist.");
//        ordersRepository.deleteById(id);
//        return "Order successfully deleted!";
//    }

//    public String deleteAll(){
//        ordersRepository.deleteAll();
//        return "Successfully deleted!";
//    }

    public List<Orders> getOrderByUser(String email) {
        return ordersRepository.findOrdersByUser_Email(email);
    }
}
