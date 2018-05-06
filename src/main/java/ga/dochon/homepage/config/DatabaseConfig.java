package ga.dochon.homepage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
public class DatabaseConfig {
    @EnableJpaRepositories(basePackages = "ga.dochon.homepage.model.repository") // 이걸 넣어줌으로써 repository들이 컨테이너에 들어감
    static class JpaRepositoryConfig {}
}
