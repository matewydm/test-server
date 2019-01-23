package pl.darenie.dns.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
public class DataSourceConfig {

    @Value("${db.url}")
    public String DB_URL;
    @Value("${db.driverClass}")
    public String DB_DRIVER_CLASS;
    @Value("${db.username}")
    public String DB_USERNAME;
    @Value("${db.password}")
    public String DB_PASSWORD;

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(DB_DRIVER_CLASS);
        dataSourceBuilder.url(DB_URL);
        dataSourceBuilder.username(DB_USERNAME);
        dataSourceBuilder.password(DB_PASSWORD);
        return dataSourceBuilder.build();
    }
}