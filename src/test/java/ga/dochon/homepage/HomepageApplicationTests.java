package ga.dochon.homepage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ga.dochon.homepage.model.entity.Article;
import ga.dochon.homepage.model.entity.Board;
import ga.dochon.homepage.service.BoardService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class HomepageApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardService boardService;

    @Before
    public void setUp() {
    }

    @Test
    public void boardTest() {
        boardGetTest();
        boardPostTest();
        boardPatchTest();
        boardDeleteTest();
    }

    // Object to String
    private String jsonStringFromObject(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException jpe) {
            jpe.getMessage();
            return null;
        }
    }

    private void boardGetTest() {
        int idBoard;
        URI uri;
        Board realBoard;
        try {
            //////////// 정상 상황 1
            idBoard = 1;
            uri = URI.create("/board/" + idBoard);
            realBoard = boardService.getBoard(idBoard);
            mockMvc.perform(get(uri))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value(realBoard.getName()))
                    .andExpect(jsonPath("$.description").value(realBoard.getDescription()));

            //////////// 정상 상황 2
            idBoard = 2;
            uri = URI.create("/board/" + idBoard);
            realBoard = boardService.getBoard(idBoard);
            mockMvc.perform(get(uri))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value(realBoard.getName()))
                    .andExpect(jsonPath("$.description").value(realBoard.getDescription()));

            //////////// 정상 상황 3 : 없는 번호로 조회 -> HTTP Code 203 리턴됨, 결과값은 null.  이걸로 할지 4XX로 할지?
            idBoard = 100000000;
            uri = URI.create("/board/" + idBoard);
            mockMvc.perform(get(uri))
                    .andExpect(status().isNoContent());

            //////////// 음수를 넣으면 안됨 -> 4XX 에러 코드
            idBoard = -50;
            uri = URI.create("/board/" + idBoard);
            realBoard = boardService.getBoard(idBoard);
            mockMvc.perform(get(uri))
                    .andExpect(status().is4xxClientError());

            //////////// 목록 조회 정상 상황 1
            uri = URI.create("/boards");
            List<Board> realBoards = boardService.findBoards(null, null, null);
            mockMvc.perform(get(uri))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[2].name").value(realBoards.get(2).getName()))
                    .andExpect(jsonPath("$[0].description").value(realBoards.get(0).getDescription()));

            //////////// 목록 조회 정상 상황 2
            String nameSearch = "자유";
            uri = URI.create("/boards?name=" + nameSearch);
            realBoards = boardService.findBoards(nameSearch, null, null);
            mockMvc.perform(get(uri))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].description").value(realBoards.get(0).getDescription()));

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    private void boardPostTest() {
        try {
            ///////// 정상상황
            Board board = new Board()
                    .setName("test name")
                    .setDescription("test description")
                    .setIdOrganization(1)
                    .setType(Board.BoardType.NORMAL);
            String boardJson = jsonStringFromObject(board);

            MvcResult result =  mockMvc.perform(post("/board").contentType("application/json").content(boardJson))
                    .andExpect(status().isOk())
                    .andReturn();

            int idBoard = Integer.valueOf(result.getResponse().getContentAsString());
            Board realBoard = boardService.getBoard(idBoard);

            Assert.assertEquals(realBoard.getName(), board.getName());
            Assert.assertEquals(realBoard.getIdOrganization(), board.getIdOrganization());
            Assert.assertEquals(realBoard.getDescription(), board.getDescription());


            ///////// name, description, idOrganization, type 중 하나가 없으면 실패
            Board failBoard = new Board()
                    .setName("test name")
                    .setDescription("test description")
                    .setIdOrganization(1);
                    //.setType(Board.BoardType.NORMAL);
            String failBoardJson = jsonStringFromObject(failBoard);

            mockMvc.perform(post("/board").contentType("application/json").content(failBoardJson))
                    .andExpect(status().is4xxClientError());


            ///////// 없는 idOrganization 만들면 실패
            failBoard = new Board()
                    .setName("test name")
                    .setDescription("test description")
                    .setIdOrganization(100000000)
                    .setType(Board.BoardType.NORMAL);
            failBoardJson = jsonStringFromObject(failBoard);

            mockMvc.perform(post("/board").contentType("application/json").content(failBoardJson))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    private void boardPatchTest() {
        try {
            int idBoard = 1;
            Board realBoard = boardService.getBoard(idBoard);

            ///////// 정상상황
            Board boardToUpdate = realBoard.setName("수정 테스트 게시판").setIdOrganization(2);
            String boardJson = jsonStringFromObject(boardToUpdate);

            MvcResult result =  mockMvc.perform(patch("/board").contentType("application/json").content(boardJson))
                    .andExpect(status().isOk())
                    .andReturn();

            boolean isSucceeded = Boolean.valueOf(result.getResponse().getContentAsString());
            Assert.assertTrue(isSucceeded);

            realBoard = boardService.getBoard(idBoard);
            Assert.assertEquals(realBoard, boardToUpdate);

            ///////// 없는 조직번호로 수정하면 서버 에러!
            boardToUpdate = realBoard.setName("수정 테스트 게시판").setIdOrganization(100000000);
            boardJson = jsonStringFromObject(boardToUpdate);
            result = mockMvc.perform(patch("/board").contentType("application/json").content(boardJson))
                    .andExpect(status().is5xxServerError())
                    .andReturn();

            isSucceeded = Boolean.valueOf(result.getResponse().getContentAsString());
            Assert.assertFalse(isSucceeded);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }


    private void boardDeleteTest() {
        try {
            //////////// 정상 상황
            int idBoard = 1;
            MvcResult result =  mockMvc.perform(delete("/board/" + idBoard))
                    .andExpect(status().isOk())
                    .andReturn();

            boolean isSucceeded = Boolean.valueOf(result.getResponse().getContentAsString());
            Assert.assertTrue(isSucceeded);

            Board realBoard = boardService.getBoard(idBoard);
            Assert.assertNull(realBoard);
            // ToDo(@기현) : 이외에도 정말 게시글, 댓글까지 모두 삭제되었는지 확인해야 함.
            // ArticleService, CommentService 만들어지면 그것까지 검사하자..

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
