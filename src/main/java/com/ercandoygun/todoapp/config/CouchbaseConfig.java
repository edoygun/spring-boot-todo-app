package com.ercandoygun.todoapp.config;

import com.couchbase.client.core.error.BucketNotFoundException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.env.ClusterEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

@Slf4j
@Configuration
@EnableCouchbaseRepositories
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    @Value("#{'${spring.couchbase.bucket.user:Administrator}'}")
    private String username;

    @Override
    public String getConnectionString() {
        return "couchbase-server";
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public String getPassword() {
        return "password";
    }

    @Override
    public String getBucketName() {
        return "ToDo";
    }

    @Override
    @Bean(destroyMethod = "disconnect")
    public Cluster couchbaseCluster(ClusterEnvironment couchbaseClusterEnvironment) {
        try {
            log.debug("Connecting to Couchbase cluster");
            return Cluster.connect(getConnectionString(), getUserName(), getPassword());
        } catch (Exception e) {
            log.error("Error connecting to Couchbase cluster", e);
            throw e;
        }
    }

    @Bean
    public Bucket getCouchbaseBucket(Cluster cluster) {
        try {
            if (!cluster.buckets().getAllBuckets().containsKey(getBucketName())) {
                log.error("Bucket with name {} does not exist. Creating it now", getBucketName());
                throw new BucketNotFoundException(getBucketName());
            }
            return cluster.bucket(getBucketName());
        } catch (Exception e) {
            log.error("Error getting bucket", e);
            throw e;
        }
    }

}
