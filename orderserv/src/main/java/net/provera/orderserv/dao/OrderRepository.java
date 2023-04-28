package net.provera.orderserv.dao;

import net.provera.orderserv.dao.model.Order;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CouchbaseRepository<Order, String> {

    List<Order> findByCustomerId(String customerId);
}
