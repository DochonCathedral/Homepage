package ga.dochon.homepage.api.controller;

import ga.dochon.homepage.model.entity.Board;
import ga.dochon.homepage.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class BoardController {
    @Autowired
    BoardService boardService;

    @PostMapping("/board")
    @ResponseBody
    public int createBoard(@Valid @RequestBody Board board) {
        return boardService.createBoard(board).getIdBoard();
    }

    @GetMapping("/board/{idBoard}")
    @ResponseBody
    public Board getBoard(@PathVariable int idBoard) {
        return boardService.getBoard(idBoard);
    }

    @PatchMapping("/board")
    @ResponseBody
    public boolean updateBoard(@Valid @RequestBody Board board) {
        return boardService.updateBoard(board);
    }

    @DeleteMapping("/board/{idBoard}")
    @ResponseBody
    public boolean deleteBoard(@PathVariable int idBoard) {
        return boardService.deleteBoard(idBoard);
    }

    @GetMapping("/boards")
    @ResponseBody
    public List<Board> findBoards(@RequestParam(value = "name", required=false) String name,
                                   @RequestParam(value = "idOrganization", required=false) Integer idOrganization,
                                   @RequestParam(value = "type", required=false) Short type) {
        return boardService.findBoards(name, idOrganization, type);
    }
}
