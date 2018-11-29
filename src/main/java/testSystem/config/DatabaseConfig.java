package testSystem.config;

import com.sun.istack.internal.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:/config.properties")
public class DatabaseConfig {

    @Value("${bd.driver}")
    private String driver;
    @Value("${bd.url}")
    private String url;
    @Value("${bd.username}")
    private String username;
    @Value("${bd.password}")
    private String password;

    @NotNull
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource=new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(driver);
        driverManagerDataSource.setUrl(url);
        driverManagerDataSource.setUsername(username);
        driverManagerDataSource.setPassword(password);
        return  driverManagerDataSource;
    }
}
