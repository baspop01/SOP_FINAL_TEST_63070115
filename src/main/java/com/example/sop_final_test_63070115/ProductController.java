package com.example.sop_final_test_63070115;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class ProductController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private ProductService productService;

    @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
    public void serviceAddProduct(@RequestBody Product n){
        rabbitTemplate.convertAndSend("ProductExchange", "add", n);
    }

    @RequestMapping( value = "/delProduct", method = RequestMethod.POST)
    public void serviceDeleteProduct(@RequestBody Product n){
        rabbitTemplate.convertAndSend("ProductExchange", "delete", n);
    }

    @RequestMapping( value = "/getAllProduct", method = RequestMethod.GET)
    public ArrayList serviceGetAllProduct(){
        Object p = rabbitTemplate.convertSendAndReceive("ProductExchange", "getall", "");
        return (ArrayList) p;
    }

    @RequestMapping( value = "/getNameProduct/{name}", method = RequestMethod.GET)
    public Product serviceGetProductName(@PathVariable("name") String n){
        Object p = rabbitTemplate.convertSendAndReceive("ProductExchange", "getname", n);
        return (Product) p;
    }

    @RequestMapping( value = "/updateProduct", method = RequestMethod.POST)
    public void serviceUpdateProduct(@RequestBody Product n){
        rabbitTemplate.convertAndSend("ProductExchange", "update", n);
    }
}
