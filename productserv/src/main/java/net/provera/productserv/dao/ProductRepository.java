package net.provera.productserv.dao;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import net.provera.productserv.dao.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
	List<Product> findByCategoryId(String categoryId);

}
