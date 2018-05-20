package ga.dochon.homepage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ga.dochon.homepage.model.entity.Article;
import ga.dochon.homepage.model.entity.Board;
import ga.dochon.homepage.service.ArticleService;
import ga.dochon.homepage.service.BoardService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
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

    @Autowired
    private ArticleService articleService;

    @Before
    public void setUp() {
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void boardTest() {
        boardGetTest();
        boardPostTest();
        boardPatchTest();
        boardDeleteTest();
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void articleTest() {
        articleGetTest();
        articlePostTest();
//        articlePatchTest();
//        articleDeleteTest();
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
            Assert.assertEquals(realBoard.getType(), board.getType());


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


    private void articleGetTest() {
        int idArticle;
        URI uri;
        Article realArticle;
        try {
            //////////// 정상 상황 1
            idArticle = 1;
            uri = URI.create("/article/" + idArticle);
            realArticle = articleService.getArticle(idArticle);
            mockMvc.perform(get(uri))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value(realArticle.getTitle()))
                    .andExpect(jsonPath("$.idUser").value(realArticle.getIdUser()));

            //////////// 정상 상황 2
            idArticle = 2;
            uri = URI.create("/article/" + idArticle);
            realArticle = articleService.getArticle(idArticle);
            mockMvc.perform(get(uri))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value(realArticle.getTitle()))
                    .andExpect(jsonPath("$.idUser").value(realArticle.getIdUser()));

            //////////// 정상 상황 3 : 없는 번호로 조회 -> HTTP Code 203 리턴됨, 결과값은 null.  이걸로 할지 4XX로 할지?
            idArticle = 100000000;
            uri = URI.create("/article/" + idArticle);
            mockMvc.perform(get(uri))
                    .andExpect(status().isNoContent());

            //////////// 음수를 넣으면 안됨 -> 4XX 에러 코드
            idArticle = -50;
            uri = URI.create("/article/" + idArticle);
            mockMvc.perform(get(uri))
                    .andExpect(status().is4xxClientError());

            //////////// 목록 조회 정상 상황 1
            uri = URI.create("/articles");
            List<Article> realArticles = articleService.findArticles(null, null, null, null);
            mockMvc.perform(get(uri))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[2].title").value(realArticles.get(2).getTitle()))
                    .andExpect(jsonPath("$[0].idUser").value(realArticles.get(0).getIdUser()));

            //////////// 목록 조회 정상 상황 2
            String nameSearch = "자유";
            uri = URI.create("/articles?title=" + nameSearch);
            realArticles = articleService.findArticles(nameSearch, null, null, null);
            mockMvc.perform(get(uri))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].title").value(realArticles.get(0).getTitle()));

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    private void articlePostTest() {
        try {
            ///////// 정상상황
            Article article = new Article()
                    .setTitle("테스트 게시글")
                    .setIdUser(1)
                    .setIdBoard(1)
                    .setContents("<p>하하하하</p>")
                    .setStatus(Article.ArticleStatus.CREATED)
                    .setType(Article.ArticleType.NORMAL)
                    //.setArticlePassword(1)
                    //.setThumbnail(1)
                    ;
            String articleJson = jsonStringFromObject(article);

            MvcResult result =  mockMvc.perform(post("/article").contentType("application/json").content(articleJson))
                    .andExpect(status().isOk())
                    .andReturn();

            int idArticle = Integer.valueOf(result.getResponse().getContentAsString());
            Article realArticle = articleService.getArticle(idArticle);

            Assert.assertEquals(realArticle.getTitle(), article.getTitle());
            Assert.assertEquals(realArticle.getStatus(), article.getStatus());
            Assert.assertEquals(realArticle.getContents(), article.getContents());


            ///////// title, IdBoard 등 필요 파라미터 없으면 실패
            Article failArticle = new Article()
                    //.setTitle("테스트 게시글")
                    .setIdUser(1)
                    .setIdBoard(1)
                    .setContents("<p>하하하하</p>")
                    .setStatus(Article.ArticleStatus.CREATED)
                    .setType(Article.ArticleType.NORMAL)
                    //.setArticlePassword(1)
                    //.setThumbnail(1)
                    ;
            String failArticleJson = jsonStringFromObject(failArticle);

            mockMvc.perform(post("/article").contentType("application/json").content(failArticleJson))
                    .andExpect(status().is4xxClientError());


            ///////// 없는 idBoard 만들면 실패
            failArticle = new Article()
                    .setTitle("테스트 게시글")
                    .setIdUser(1)
                    .setIdBoard(10000000)
                    .setContents("<p>하하하하</p>")
                    .setStatus(Article.ArticleStatus.CREATED)
                    .setType(Article.ArticleType.NORMAL)
                    //.setArticlePassword(1)
                    //.setThumbnail(1)
                    ;
            failArticleJson = jsonStringFromObject(failArticle);

            mockMvc.perform(post("/article").contentType("application/json").content(failArticleJson))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
