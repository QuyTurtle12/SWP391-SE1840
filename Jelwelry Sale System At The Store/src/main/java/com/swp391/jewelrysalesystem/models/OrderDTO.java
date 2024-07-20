package com.swp391.jewelrysalesystem.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//This class refers to "Products of Order" table
//Main task is deliver data to different layers

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class OrderDTO {
    private int orderID; //Link from Order table
    private int productID; //Link from Product table
    private double productPrice;
    private double productOriginalPrice; //Price that not applied discount
    private int amount; //Quantity of the product in the order
}
