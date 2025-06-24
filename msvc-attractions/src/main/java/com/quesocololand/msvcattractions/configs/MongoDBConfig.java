package com.quesocololand.msvcattractions.configs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MongoJobExplorerFactoryBean;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MongoJobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

@Configuration
public class MongoDBConfig {
    //Fields of MongoDBConfig
    //Constructors of MongoDBConfig
    //Field setters of MongoDBConfig (setters)
        //Bean getters of MongoDBConfig (getters)
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb+srv://santimh04:FXsjq4tEG2aiyOLA@personalcluster.kgguhd0.mongodb.net/QuesoColoLand?retryWrites=true&w=majority");
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        var mongoTemplate = new MongoTemplate(mongoClient, "QuesoColoLand");
        var mappingMongoConverter = (MappingMongoConverter) mongoTemplate.getConverter();
            mappingMongoConverter.setMapKeyDotReplacement(".");
            mappingMongoConverter.afterPropertiesSet();
        return mongoTemplate;
    }

    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTransactionManager(mongoDatabaseFactory);
    }

    @Bean
    public JobRepository jobRepository(MongoTemplate mongoTemplate, MongoTransactionManager transactionManager) throws Exception {
        MongoJobRepositoryFactoryBean jobRepositoryFactoryBean = new MongoJobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setMongoOperations(mongoTemplate);
        jobRepositoryFactoryBean.setTransactionManager(transactionManager);
        jobRepositoryFactoryBean.afterPropertiesSet();
        return jobRepositoryFactoryBean.getObject();
    }

    @Bean
    public JobExplorer jobExplorer(MongoTemplate mongoTemplate, MongoTransactionManager transactionManager) throws Exception {
        var mongoExplorerFactoryBean = new MongoJobExplorerFactoryBean();
        mongoExplorerFactoryBean.setMongoOperations(mongoTemplate);
        mongoExplorerFactoryBean.setTransactionManager(transactionManager);
        mongoExplorerFactoryBean.afterPropertiesSet();
        return mongoExplorerFactoryBean.getObject();
    }

}
