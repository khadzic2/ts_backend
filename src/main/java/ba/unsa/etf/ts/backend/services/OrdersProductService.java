package ba.unsa.etf.ts.backend.services;

import ba.unsa.etf.ts.backend.exception.NotFoundException;
import ba.unsa.etf.ts.backend.model.Orders;
import ba.unsa.etf.ts.backend.model.Ordersproduct;
import ba.unsa.etf.ts.backend.model.Product;
import ba.unsa.etf.ts.backend.repository.OrdersProductRepository;
import ba.unsa.etf.ts.backend.repository.OrdersRepository;
import ba.unsa.etf.ts.backend.repository.ProductRepository;
import ba.unsa.etf.ts.backend.request.AddOrderProductRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersProductService {

    private final OrdersProductRepository ordersProductRepository;
    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;

    public OrdersProductService(OrdersProductRepository ordersProductRepository, OrdersRepository ordersRepository, ProductRepository productRepository) {
        this.ordersProductRepository = ordersProductRepository;
        this.ordersRepository = ordersRepository;
        this.productRepository = productRepository;
    }

    public List<Ordersproduct> getAllOrdersproductsByOrderId(Integer orderId){
        return ordersProductRepository.findOrdersproductByOrders_Id(orderId);
    }

    public Ordersproduct getOrdersproduct(Integer id){
        return ordersProductRepository.findById(id).orElseThrow(()->new NotFoundException("Orders product by id:"+id+"does not exist."));
    }

    public Ordersproduct addOrdersproduct(AddOrderProductRequest addOrderProductRequest){
        Orders order = ordersRepository.findById(addOrderProductRequest.getOrderId()).orElseThrow(()->new NotFoundException("Order by id:"+addOrderProductRequest.getOrderId()+" does not exist."));
        Product product = productRepository.findById(addOrderProductRequest.getProductId()).orElseThrow(()->new NotFoundException("Product by id:"+addOrderProductRequest.getProductId()+" does not exist."));
        Ordersproduct newOrder = new Ordersproduct();

        Integer saledQuantity = product.getSaledQuantity() + addOrderProductRequest.getQuantity();
        Integer quantity = product.getQuantity() - addOrderProductRequest.getQuantity();
        product.setSaledQuantity(saledQuantity);
        product.setQuantity(quantity);
        Product newProduct = productRepository.save(product);

        newOrder.setSize(addOrderProductRequest.getSize());
        newOrder.setQuantity(addOrderProductRequest.getQuantity());
        newOrder.setOrders(order);
        newOrder.setProduct(newProduct);

        return ordersProductRepository.save(newOrder);
    }

    public String deleteOrdersproduct(Integer id){
        Ordersproduct ordersproduct = ordersProductRepository.findById(id).orElse(null);
        if(ordersproduct == null){
            throw new NotFoundException("Orders product by id:"+id+" does not exist.");
        }
        ordersProductRepository.delete(ordersproduct);
        return "Orders product successfully deleted!";
    }
}
