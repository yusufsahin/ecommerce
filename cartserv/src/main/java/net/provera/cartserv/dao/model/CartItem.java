package net.provera.cartserv.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private String id;
    private String name;
    private int quantity;
    private double price;
}