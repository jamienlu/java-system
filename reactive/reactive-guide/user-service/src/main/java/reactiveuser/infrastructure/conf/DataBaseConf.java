package reactiveuser.infrastructure.conf;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
public class DataBaseConf {

	@Bean
	public ConnectionFactory connectionFactory() {
		ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
				.option(DRIVER, "mysql")
				.option(HOST, "192.168.0.102")
				.option(USER, "root")
				.option(PORT, 3306)
				.option(PASSWORD, "root")
				.option(DATABASE, "asura_user")
				.option(CONNECT_TIMEOUT, Duration.ofSeconds(3))
				.build();
		ConnectionFactory connectionFactory = ConnectionFactories.get(options);
		ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration.builder(connectionFactory)
				.maxIdleTime(Duration.ofMillis(1000))
				.initialSize(10)
				.maxSize(20).build();
		// ConnectionPool实现了ConnectionFactory接口，使用ConnectionFactory替换ConnectionFactory
		return new ConnectionPool(configuration);
	}
}
