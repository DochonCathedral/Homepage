package ga.dochon.homepage.api.controller;

import ga.dochon.homepage.model.entity.Article;
import ga.dochon.homepage.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Controller
public class ArticleController {
    @Autowired
    ArticleService articleService;

    @PostMapping("/article")
    @ResponseBody
    public ResponseEntity<?> createArticle(@Valid @RequestBody Article article) {
        try {
            return new ResponseEntity<>(articleService.createArticle(article).getIdArticle(),HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/article/{idArticle}")
    @ResponseBody
    public ResponseEntity<?> getArticle(@PathVariable int idArticle) {
        if (idArticle <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Article article = articleService.getArticle(idArticle);

        if (Objects.nonNull(article)) {
            return new ResponseEntity<>(article, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PatchMapping("/article")
    @ResponseBody
    public ResponseEntity<Boolean> updateArticle(@Valid @RequestBody Article article) {
        try {
            return new ResponseEntity<>(articleService.updateArticle(article), HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/article/{idArticle}")
    @ResponseBody
    public ResponseEntity<Boolean> deleteArticle(@PathVariable int idArticle) {
        if (idArticle <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(articleService.deleteArticle(idArticle), HttpStatus.OK);
    }

    @GetMapping("/articles")
    @ResponseBody
    public ResponseEntity<List<Article>> findArticles(@RequestParam(value = "title", required=false) String title,
                                                    @RequestParam(value = "idUser", required=false) Integer idUser,
                                                    @RequestParam(value = "idBoard", required=false) Integer idBoard,
                                                    @RequestParam(value = "contents", required=false) String contents) {
        return new ResponseEntity<>(articleService.findArticles(title, idUser, idBoard, contents), HttpStatus.OK);
    }
}
