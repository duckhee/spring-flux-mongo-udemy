package kr.co.won.springfluxmongoudemy.configuration;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

//@Configuration
//@EnableReactiveMongoRepositories(basePackageClasses = {})
public class ReactiveMongoDBConfiguration extends AbstractReactiveMongoConfiguration {

    @Value("${udemy.mongodb.replicaset.name}")
    private String replicasetName;

    @Value("${udemy.mongodb.replicaset.username}")
    private String replicasetUserName;

    @Value("${udemy.mongodb.replicaset.password}")
    private String replicasetUserPassword;

    @Value("${udemy.mongodb.replicaset.primary}")
    private String replicasetPrimary;

    @Value("${udemy.mongodb.replicaset.port}")
    private String replicasetPort;

    @Value("${udemy.mongodb.replicaset.database}")
    private String database;

    @Value("${udemy.mongodb.replicaset.authentication-database}")
    private String replicasetAuthenticationDatabase;

    @Override
    public MongoClient reactiveMongoClient() {
        MongoClient mongoClient = MongoClients.create("mongodb://" + replicasetUserName + ":" + replicasetUserPassword + "@" + replicasetPrimary + ":" + replicasetPort + "/" + database + "?replicaSet=" + replicasetName + "&authSource=" + replicasetAuthenticationDatabase);

        return mongoClient;
    }

    @Override
    protected String getDatabaseName() {
        return database;
    }
}
