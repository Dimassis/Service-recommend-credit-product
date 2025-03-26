package sky.pro.recomendService.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    @Primary
    @Bean(name = "defaultDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.default")
    public DataSource defaultDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "rulesDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.rules")
    public DataSource rulesDataSource() {
        return DataSourceBuilder.create().build();
    }
}
