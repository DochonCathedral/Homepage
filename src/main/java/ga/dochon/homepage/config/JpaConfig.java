package ga.dochon.homepage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "ga.dochon.homepage.model.repository")
@Configuration
public class JpaConfig {
}
