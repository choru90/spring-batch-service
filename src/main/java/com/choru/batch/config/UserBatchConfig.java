package com.choru.batch.config;

import com.choru.batch.domain.User;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserBatchConfig {

    private final int CHUNK_SIZE = 10;


    @Bean
    public Job jpaPagingItemReaderJob(JobRepository jobRepository, Step jpaPagingItemReaderStep){
        return new JobBuilder("userUpdateJob", jobRepository)
                .start(jpaPagingItemReaderStep)
                .build();
    }

    @Bean
    @JobScope
    public Step jpaPagingItemReaderStep(JobRepository jobRepository,
                                        JpaPagingItemReader<User> userUpdatePagingItemReader,
                                        ItemWriter<User> userUpdatePagingItemWriter,
                                        PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("userUpdateJob", jobRepository)
                .<User, User>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(userUpdatePagingItemReader)
                .writer(userUpdatePagingItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<User> userUpdatePagingItemReader(EntityManagerFactory entityManagerFactory){
        return new JpaPagingItemReaderBuilder<User>().name("userUpdatePagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(CHUNK_SIZE)
                .queryString("SELECT u FROM User u")
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<User> userUpdatePagingItemWriter(){
        return users -> {
            for (User user : users) {
                user.delete();
            }
        };
    }

}
