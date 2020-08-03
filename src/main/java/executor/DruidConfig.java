package executor;

import com.alibaba.druid.pool.DruidDataSource;
import executor.core.config.CkConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DruidConfig {

    @Autowired
    private CkConfig ckConfig;

    @Bean
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(ckConfig.getUrl());
        datasource.setDriverClassName(ckConfig.getDriverClassName());
        datasource.setInitialSize(ckConfig.getInitialSize());
        datasource.setMinIdle(ckConfig.getMinIdle());
        datasource.setMaxActive(ckConfig.getMaxActive());
        datasource.setMaxWait(ckConfig.getMaxWait());
        return datasource;
    }
}
