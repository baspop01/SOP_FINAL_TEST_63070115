package com.example.sop_final_test_63070115;

import org.springframework.stereotype.Service;

@Service
public class CalculatorPriceService {
    public double getPrice(double cost, double profit){
        return cost+profit;
    }
}
