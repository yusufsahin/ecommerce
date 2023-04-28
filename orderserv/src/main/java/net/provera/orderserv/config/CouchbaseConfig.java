package net.provera.orderserv.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;


@Configuration
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    static String connectionString = "couchbase://localhost";
    static String username = "Administrator";
    static String password = "123456";
    static String bucketName = "order-bucket";

    @Override
    public String getConnectionString() {
        return connectionString;
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getBucketName() {
        return bucketName;
    }
}
