package ga.dochon.homepage.service;

import ga.dochon.homepage.model.entity.Reply;
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
public class ReplyService {
    @Autowired
    private ReplyRepository replyRepository;

    @Transactional
    public Reply getReply(int idReply) {
        Example<Reply> idExample = Example.of(new Reply().setIdReply(idReply));
        return replyRepository.findOne(idExample).orElse(null); // getOne()은 Lazyloading 함...
    }

    @Transactional
    public boolean deleteReply(int idReply) {
        if (replyRepository.existsById(idReply)) {
            Reply reply = this.getReply(idReply);
            reply.setStatus(Reply.ReplyStatus.DELETED);
            replyRepository.save(reply);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean removeReply(int idReply) {
        if (replyRepository.existsById(idReply)) {
            replyRepository.deleteById(idReply);
            return true;
        } else {
            return false;
        }
    }

    public Reply createReply(Reply reply) {
        return replyRepository.save(reply);
    }

    @Transactional
    public boolean updateReply(Reply replyInput) {
        if (replyRepository.existsById(replyInput.getIdReply())) {
            replyRepository.save(replyInput);

            return true;
        } else {
            return false;
        }
    }

    public List<Reply> findReplies(Integer idArticle, Integer idUser, String contents) {
        Reply replyForExample = new Reply();
        if (Objects.nonNull(idArticle))
            replyForExample.setIdArticle(idArticle);
        if (Objects.nonNull(idUser))
            replyForExample.setIdUser(idUser);
        if (Objects.nonNull(contents))
            replyForExample.setContents(contents);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("contents", contains());
        Example<Reply> example = Example.of(replyForExample, matcher);

        return replyRepository.findAll(example);
    }
}
