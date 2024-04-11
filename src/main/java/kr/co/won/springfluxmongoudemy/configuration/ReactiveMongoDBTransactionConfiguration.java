package kr.co.won.springfluxmongoudemy.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;

@Configuration
public class ReactiveMongoDBTransactionConfiguration {


    @Bean
    public ReactiveMongoTransactionManager transactionManager(ReactiveMongoDatabaseFactory factory) {
        return new ReactiveMongoTransactionManager(factory);
    }

    /**
     * Reactive GridFS 사용을 위한 template 정의
     * @param factory
     * @param mappingMongoConverter
     * @return
     */
    @Bean
    public ReactiveGridFsTemplate reactiveGridFsTemplate(ReactiveMongoDatabaseFactory factory, MappingMongoConverter mappingMongoConverter) {
        return new ReactiveGridFsTemplate(factory, mappingMongoConverter);
    }
}
