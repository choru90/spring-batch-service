package com.choru.batch.config;


import com.choru.batch.domain.User;
import com.choru.batch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig {


    @Bean
    public Job userDeleteJob(JobRepository jobRepository, Step userDeleteStep){
        return new JobBuilder("userDeleteJob", jobRepository)
                .start(userDeleteStep)
                .build();
    }

    @Bean
    @JobScope
    public Step userDeleteStep(@Value("#{jobParameters[runDate]}")String runDate, JobRepository jobRepository, Tasklet testTasklet, PlatformTransactionManager transactionManager){
        return new StepBuilder("userDeleteStep", jobRepository)
                .tasklet(testTasklet, transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet testTasklet(UserRepository  userRepository){
        return ((contribution, chunkContext) -> {
            User user1 = User.of("정대만", "aaa@gmail.com", "01012341234", "불꽃남자", "정대만 사진", LocalDate.now());
            User user2 = User.of("송태섭", "sss@gmail.com", "01011112222", "키작은 남자", "송태섭 사진", LocalDate.now());
            user1.delete();
            userRepository.saveAll(List.of(user1, user2));
            return RepeatStatus.FINISHED;
        });
    }


}
