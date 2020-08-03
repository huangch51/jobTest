package executor.core.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Data
public class CkConfig {

    @Value("${datasource.clickhouse.driver-class-name}")
    private String driverClassName ;

    @Value("${datasource.clickhouse.jdbc-url}")
    private String url ;

    @Value("${datasource.clickhouse.username}")
    private String userName;

    @Value("${datasource.clickhouse.password}")
    private String password;

    @Value("${datasource.clickhouse.initialSize}")
    private Integer initialSize ;
    @Value("${datasource.clickhouse.maxActive}")
    private Integer maxActive ;
    @Value("${datasource.clickhouse.minIdle}")
    private Integer minIdle ;
    @Value("${datasource.clickhouse.maxWait}")
    private Integer maxWait ;
}

