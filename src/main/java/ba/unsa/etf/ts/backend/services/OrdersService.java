package ba.unsa.etf.ts.backend.services;

import ba.unsa.etf.ts.backend.exception.NotFoundException;
import ba.unsa.etf.ts.backend.model.Orders;
import ba.unsa.etf.ts.backend.model.User;
import ba.unsa.etf.ts.backend.repository.OrdersRepository;
import ba.unsa.etf.ts.backend.repository.UserRepository;
import ba.unsa.etf.ts.backend.request.AddOrdersRequest;
import ba.unsa.etf.ts.backend.request.UpdateOrdersRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final UserRepository userRepository;
    public OrdersService(OrdersRepository ordersRepository, UserRepository userRepository) {
        this.ordersRepository = ordersRepository;
        this.userRepository = userRepository;
    }

    public List<Orders> getAllOrders(){
        return ordersRepository.findAll();
    }

    public Orders getOrder(Integer id){
        return ordersRepository.findById(id).orElseThrow(()->new NotFoundException("Order by id:"+id+" does not exist."));
    }

    public Orders addOrder(AddOrdersRequest newOrder){
        User user = userRepository.findById(newOrder.getUserId()).orElseThrow(()->new NotFoundException("User by id:"+newOrder.getUserId()+" does not exist."));
        Orders order = new Orders();
        order.setCode("O"+LocalDateTime.now().getYear()+LocalDateTime.now().getMonthValue()+LocalDateTime.now().getDayOfYear()+"T"+LocalDateTime.now().getHour()+LocalDateTime.now().getMinute());
        order.setDateAndTime(LocalDateTime.now());
        order.setShipped(false);
        order.setDelivered(false);
        order.setAmount(newOrder.getAmount());
        order.setUser(user);

        return ordersRepository.save(order);
    }

    public Orders updateOrder(Integer id, UpdateOrdersRequest newOrder){
        Orders updateOrder = ordersRepository.findById(id).orElseThrow(()->new NotFoundException("Order by id:"+id+" does not exist."));

        updateOrder.setDelivered(newOrder.getDelivered());
        updateOrder.setShipped(newOrder.getShipped());

        return ordersRepository.save(updateOrder);
    }

    public String deleteOrder(Integer id){
        Orders order = ordersRepository.findById(id).orElse(null);
        if(order == null) throw new NotFoundException("Order by id:"+id+" does not exist.");
        ordersRepository.deleteById(id);
        return "Order successfully deleted!";
    }

    public String deleteAll(){
        ordersRepository.deleteAll();
        return "Successfully deleted!";
    }

    public List<Orders> getOrderByUserId(Integer userId) {
        return ordersRepository.findOrdersByUser_Id(userId);
    }
}
