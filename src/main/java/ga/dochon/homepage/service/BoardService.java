package ga.dochon.homepage.service;

import ga.dochon.homepage.model.entity.Article;
import ga.dochon.homepage.model.entity.Board;
import ga.dochon.homepage.model.entity.Reply;
import ga.dochon.homepage.model.repository.ArticleRepository;
import ga.dochon.homepage.model.repository.BoardRepository;
import ga.dochon.homepage.model.repository.OrganizationRepository;
import ga.dochon.homepage.model.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Service
public class BoardService {
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    ReplyRepository replyRepository;

    public Board getBoard(int idBoard) {
        Example<Board> idExample = Example.of(new Board().setIdBoard(idBoard));
        return boardRepository.findOne(idExample).orElse(null); // getOne()은 Lazyloading 함...
    }

    @Transactional
    public boolean deleteBoard(int idBoard) {
        if (boardRepository.existsById(idBoard)) {
            // 연결된 게시글과 댓글을 아예 완전히 삭제한다.
            Example<Article> articleExample = Example.of(new Article().setIdBoard(idBoard));
            for (Article article : articleRepository.findAll(articleExample)) {
                Example<Reply> replyExample = Example.of(new Reply().setIdArticle(article.getIdArticle()));
                List<Reply> replies = replyRepository.findAll(replyExample);
                replyRepository.deleteAll(replies);

                articleRepository.delete(article);
            }

            boardRepository.deleteById(idBoard);
            return true;
        } else {
            return false;
        }
    }

    public Board createBoard(Board board) {
        return boardRepository.save(board);
    }

    @Transactional
    public boolean updateBoard(Board boardInput) {
        if (boardRepository.existsById(boardInput.getIdBoard())) {
            boardRepository.save(boardInput);

            return true;
        } else {
            return false;
        }
    }

    public List<Board> findBoards(String name, Integer idOrganization, Board.BoardType type) {
        Board boardForExample = new Board();
        if (Objects.nonNull(name))
            boardForExample.setName(name);
        if (Objects.nonNull(idOrganization))
            boardForExample.setIdOrganization(idOrganization);
        if (Objects.nonNull(type))
            boardForExample.setType(type);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", contains()); // exact match는 디폴트임. // contains 는 아마 LIKE 검색일 것 같은데.. 괜찮으려나
        Example<Board> example = Example.of(boardForExample, matcher);

        return boardRepository.findAll(example);
    }
}
