package ga.dochon.homepage.model.repository;

import ga.dochon.homepage.model.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

}
