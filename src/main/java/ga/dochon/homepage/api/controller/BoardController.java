package ga.dochon.homepage.api.controller;

import ga.dochon.homepage.model.entity.Board;
import ga.dochon.homepage.service.BoardService;
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
public class BoardController {
    @Autowired
    BoardService boardService;

    @PostMapping("/board")
    @ResponseBody
    public ResponseEntity<?> createBoard(@Valid @RequestBody Board board) {
        try {
            return new ResponseEntity<>(boardService.createBoard(board).getIdBoard(),HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/board/{idBoard}")
    @ResponseBody
    public ResponseEntity<?> getBoard(@PathVariable int idBoard) {
        if (idBoard <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Board board = boardService.getBoard(idBoard);

        if (Objects.nonNull(board)) {
            return new ResponseEntity<>(board,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PatchMapping("/board")
    @ResponseBody
    public ResponseEntity<Boolean> updateBoard(@Valid @RequestBody Board board) {
        try {
            return new ResponseEntity<>(boardService.updateBoard(board), HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/board/{idBoard}")
    @ResponseBody
    public ResponseEntity<Boolean> deleteBoard(@PathVariable int idBoard) {
        if (idBoard <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(boardService.deleteBoard(idBoard), HttpStatus.OK);
    }

    @GetMapping("/boards")
    @ResponseBody
    public ResponseEntity<List<Board>> findBoards(@RequestParam(value = "name", required=false) String name,
                                   @RequestParam(value = "idOrganization", required=false) Integer idOrganization,
                                   @RequestParam(value = "type", required=false) Board.BoardType type) {
        return new ResponseEntity<>(boardService.findBoards(name, idOrganization, type), HttpStatus.OK);
    }
}
