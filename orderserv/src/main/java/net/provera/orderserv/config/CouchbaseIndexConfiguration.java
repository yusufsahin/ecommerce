package net.provera.orderserv.config;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryResult;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CouchbaseIndexConfiguration {

    @Bean
    public CommandLineRunner createPrimaryIndexIfNotExists(Cluster couchbaseCluster) {
        return args -> {
            String checkPrimaryIndexQuery = "SELECT COUNT(*) AS indexCount FROM system:indexes WHERE keyspace_id='order-bucket' AND `using`='GSI' AND is_primary=true;";
            QueryResult checkIndexResult = couchbaseCluster.query(checkPrimaryIndexQuery, QueryOptions.queryOptions());
            long indexCount = checkIndexResult.rowsAsObject().get(0).getLong("indexCount");

            if (indexCount == 0) {
                String createPrimaryIndexQuery = "CREATE PRIMARY INDEX ON `order-bucket`;";
                couchbaseCluster.query(createPrimaryIndexQuery, QueryOptions.queryOptions());
                System.out.println("Primary index created on 'order-bucket'");
            } else {
                System.out.println("Primary index already exists on 'order-bucket'");
            }
        };
}
}