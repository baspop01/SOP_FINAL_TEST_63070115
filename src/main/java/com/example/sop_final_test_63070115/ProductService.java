package com.example.sop_final_test_63070115;


import com.vaadin.flow.component.notification.Notification;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    @RabbitListener(queues = "AddProductQueue")
    public void addProduct(Product n){
//        System.out.println("test");
        repository.insert(n);
    }

    @RabbitListener(queues = "DeleteProductQueue")
    public void deleteProduct(Product n){
        repository.delete(n);
    }

    @RabbitListener(queues = "GetAllProductQueue")
    public List getAllProduct(){
        return repository.findAll();
    }

    @RabbitListener(queues = "GetNameProductQueue")
    public Product getProductByName (String n){
        return repository.getProductByName(n);
    }

    @RabbitListener(queues = "UpdateProductQueue")
    public void updateProduct(Product n){
        repository.save(n);
    }
}
