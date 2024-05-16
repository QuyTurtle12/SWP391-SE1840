package com.swp391.jewelrysalesystem.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//This class refers to "Products of Refund" table
//Main task is deliver data to different layers

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor

public class RefundDTO {
    private int refundID; //Link from Refund table
    private int productID; //Link from Product table
    private int amount; //Quantity of the product in the order
}
