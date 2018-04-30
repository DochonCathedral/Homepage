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
        return boardRepository.getOne(idBoard);
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

    public boolean updateBoard(Board boardInput) {
        if (boardRepository.existsById(boardInput.getIdBoard())) {
            boardRepository.save(boardInput);

            return true;
        } else {
            return false;
        }
    }

    public List<Board> findBoards(String name, Integer idOrganization, Short type) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", contains()); // exact match는 디폴트임. // contains 는 아마 LIKE 검색일 것 같은데.. 괜찮으려나
        Example<Board> example = Example.of(new Board().setName(name).setIdOrganization(idOrganization).setType(type), matcher);

        return boardRepository.findAll(example);
    }
}
