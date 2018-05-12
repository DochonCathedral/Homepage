package ga.dochon.homepage.service;

import ga.dochon.homepage.model.entity.Article;
import ga.dochon.homepage.model.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Service
public class ArticleService {
    @Autowired
    ArticleRepository articleRepository;

    @Transactional
    public Article getArticle(int idArticle) {
        Example<Article> idExample = Example.of(new Article().setIdArticle(idArticle));
        Article article = articleRepository.findOne(idExample).orElse(null); // getOne()은 Lazyloading 함...
        if (Objects.nonNull(article)) {
            articleRepository.save(article.incrementHit());
        }
        return article;
    }

    @Transactional
    public boolean deleteArticle(int idArticle) {
        if (articleRepository.existsById(idArticle)) {
            Article article = this.getArticle(idArticle);
            article.setStatus(Article.ArticleStatus.DELETED);
            articleRepository.save(article);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean removeArticle(int idArticle) {
        if (articleRepository.existsById(idArticle)) {
            articleRepository.deleteById(idArticle);
            return true;
        } else {
            return false;
        }
    }

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    @Transactional
    public boolean updateArticle(Article articleInput) {
        if (articleRepository.existsById(articleInput.getIdArticle())) {
            articleRepository.save(articleInput);

            return true;
        } else {
            return false;
        }
    }

    public List<Article> findArticles(String title, Integer idUser, Integer idBoard, String contents) {
        Article articleForExample = new Article();
        if (Objects.nonNull(title))
            articleForExample.setTitle(title);
        if (Objects.nonNull(idUser))
            articleForExample.setIdUser(idUser);
        if (Objects.nonNull(idBoard))
            articleForExample.setIdBoard(idBoard);
        if (Objects.nonNull(contents))
            articleForExample.setContents(contents);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("title", contains())
                .withMatcher("contents", contains());
        Example<Article> example = Example.of(articleForExample, matcher);

        return articleRepository.findAll(example);
    }
}
