package com.example.reactive.mongodb.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.example.reactive.mongodb.repository")
public class MongoReactiveConfig extends AbstractReactiveMongoConfiguration {

    @Autowired
    private MongoProperties mongoProperties; //To read mongo properties in application.properties

    @Override   //mandatory
    public MongoClient reactiveMongoClient() {
        return MongoClients.create(mongoProperties.getUri());
    }

    @Override    //mandatory
    protected String getDatabaseName() {
        return mongoProperties.getDatabase();
    }

    @Bean //mandatory for @Transactional
    public TransactionalOperator transactionalOperator(ReactiveMongoTransactionManager transactionManager) {
        //you can provide Tx type like later TransactionDefinition.ISOLATION_SERIALIZABLE
        return TransactionalOperator.create(transactionManager, TransactionDefinition.withDefaults());
    }

    @Bean //mandatory for @Transactional
    public ReactiveMongoTransactionManager getReactiveMongoTxManager(ReactiveMongoDatabaseFactory manager) {
        return new ReactiveMongoTransactionManager(manager);
    }

    //@Bean //not mandatory if Factory beans are created
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        //Use any one of them //return new ReactiveMongoTemplate(reactiveMongoClient(),getDatabaseName());
        return new ReactiveMongoTemplate(new SimpleReactiveMongoDatabaseFactory(reactiveMongoClient(),getDatabaseName()));
    }

}
