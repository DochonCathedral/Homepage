package ga.dochon.homepage.api.controller;

import ga.dochon.homepage.model.entity.Reply;
import ga.dochon.homepage.service.ReplyService;
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
public class ReplyController {
    @Autowired
    ReplyService replyService;

    @PostMapping("/reply")
    @ResponseBody
    public ResponseEntity<?> createReply(@Valid @RequestBody Reply reply) {
        try {
            return new ResponseEntity<>(replyService.createReply(reply).getIdReply(),HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/reply/{idReply}")
    @ResponseBody
    public ResponseEntity<?> getReply(@PathVariable int idReply) {
        if (idReply <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Reply reply = replyService.getReply(idReply);

        if (Objects.nonNull(reply)) {
            return new ResponseEntity<>(reply, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PatchMapping("/reply")
    @ResponseBody
    public ResponseEntity<Boolean> updateReply(@Valid @RequestBody Reply reply) {
        try {
            return new ResponseEntity<>(replyService.updateReply(reply), HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/reply/{idReply}")
    @ResponseBody
    public ResponseEntity<Boolean> deleteReply(@PathVariable int idReply) {
        if (idReply <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(replyService.deleteReply(idReply), HttpStatus.OK);
    }

    @GetMapping("/replies")
    @ResponseBody
    public ResponseEntity<List<Reply>> findReplies(@RequestParam(value = "idUser", required=false) Integer idUser,
                                                  @RequestParam(value = "contents", required=false) String contents) {
        return new ResponseEntity<>(replyService.findReplies(idUser, contents), HttpStatus.OK);
    }
}
