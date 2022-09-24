package com.example.sop_final_test_63070115;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Route(value = "index")
public class ProductView extends FormLayout {
    public String id;
    public ProductView(){
        VerticalLayout vl = new VerticalLayout();
        HorizontalLayout hl = new HorizontalLayout();

        ComboBox productList = new ComboBox("Product List");
        productList.setWidth("600px");

        TextField productName = new TextField("Product Name:");
        productName.setValue("");
        productName.setWidth("600px");

        NumberField productCost = new NumberField("Product Cost:");
        NumberField productProfit = new NumberField("Product Profit:");
        NumberField productPrice = new NumberField("Product Price:");
        productCost.setWidth("600px");
        productProfit.setWidth("600px");
        productPrice.setWidth("600px");
        productCost.setValue(0.0);
        productProfit.setValue(0.0);
        productPrice.setValue(0.0);
        productPrice.setReadOnly(true);

        Button add = new Button("Add Product");
        Button update = new Button("Update Product");
        Button delete = new Button("Delete Product");
        Button clear = new Button("Clear Product");
        hl.add(add, update, delete, clear);
        vl.add(productList, productName, productCost, productProfit, productPrice, hl);


        add(vl);

        productList.addFocusListener(event -> {
           List<Product> out = WebClient.create()
                   .get()
                   .uri("http://localhost:8080/getAllProduct")
                   .retrieve()
                   .bodyToMono(List.class)
                   .block();
            List<String> nameProduct = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();
           for(Object data: out){
               Product p = mapper.convertValue(data, Product.class);
               nameProduct.add(p.getProductName());
           }
           productList.setItems(nameProduct);
        });
        productList.addValueChangeListener(event -> {
            ObjectMapper mapper = new ObjectMapper();
            String n = mapper.convertValue(productList.getValue(), String.class);
            Product out = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getNameProduct/"+n)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .block();
            productName.setValue(out.getProductName());
            productCost.setValue((double) out.getProductCost());
            productProfit.setValue((double) out.getProductProfit());
            productPrice.setValue((double) out.getProductPrice());
            this.id = out.get_id();
        });
        productCost.addValueChangeListener(event -> {
            double cost = productCost.getValue();
            double profit = productProfit.getValue();
            double out = WebClient.create()
                   .get()
                    .uri("http://localhost:8080/getPrice/"+cost+"/"+profit)
                   .retrieve()
                   .bodyToMono(double.class)
                   .block();
           productPrice.setValue(out);
        });
        productProfit.addValueChangeListener(event -> {
            double cost = productCost.getValue();
            double profit = productProfit.getValue();
            double out = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getPrice/"+cost+"/"+profit)
                    .retrieve()
                    .bodyToMono(double.class)
                    .block();
            productPrice.setValue(out);
        });


        add.addClickListener(buttonClickEvent -> {
            String name = productName.getValue();
            int cost = productCost.getValue().intValue();
            int profit = productProfit.getValue().intValue();
            int price = productPrice.getValue().intValue();
           String out = WebClient.create()
                   .post()
                   .uri("http://localhost:8080/addProduct")
                   .body(Mono.just(new Product(null, name, cost, profit, price)), Product.class)
                   .retrieve()
                   .bodyToMono(String.class)
                   .block();
            double getprice = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getPrice/"+cost+"/"+profit)
                    .retrieve()
                    .bodyToMono(double.class)
                    .block();
            Product id = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getNameProduct/"+name)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .block();
            this.id = id.get_id();
            productPrice.setValue(getprice);
        });
        update.addClickListener(buttonClickEvent -> {
            String name = productName.getValue();
            int cost = productCost.getValue().intValue();
            int profit = productProfit.getValue().intValue();
            int price = productPrice.getValue().intValue();
            String out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/updateProduct")
                    .body(Mono.just(new Product(this.id, name, cost, profit, price)), Product.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        });
        delete.addClickListener(buttonClickEvent -> {
            String name = productName.getValue();
            int cost = productCost.getValue().intValue();
            int profit = productProfit.getValue().intValue();
            int price = productPrice.getValue().intValue();
            String out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/delProduct")
                    .body(Mono.just(new Product(this.id, name, cost, profit, price)), Product.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        });
        clear.addClickListener(buttonClickEvent -> {
            productName.setValue("");
            productCost.setValue(0.0);
            productProfit.setValue(0.0);
            productPrice.setValue(0.0);
            this.id = "";
        });

    }


}
