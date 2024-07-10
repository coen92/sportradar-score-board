package com.coen92.job.sportradarscoreboard.infrastructure.config;

import com.coen92.job.sportradarscoreboard.application.ScoreBoardService;
import com.coen92.job.sportradarscoreboard.infrastructure.InMemoryScoreBoardRepositoryImpl;
import com.coen92.job.sportradarscoreboard.infrastructure.ScoreBoardRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public ScoreBoardService scoreBoardService(
            ScoreBoardRepository scoreBoardRepository
    ) {
        return new ScoreBoardService(scoreBoardRepository);
    }

    @Bean
    public ScoreBoardRepository scoreBoardRepository() {
        return new InMemoryScoreBoardRepositoryImpl();
    }
}
