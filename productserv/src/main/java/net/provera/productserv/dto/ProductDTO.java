package net.provera.productserv.dto;

import lombok.Data;

@Data
public class ProductDTO {
	private String id;
    private String name;
    private String description;
    private float price;
    private String categoryId;

}
