package ga.dochon.homepage;

import ga.dochon.homepage.model.entity.Board;
import ga.dochon.homepage.service.BoardService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomepageApplicationTests {
    @LocalServerPort
    int port;

    private RestTemplate restTemplate = new RestTemplate();
    private String baseUrl;

    @Autowired
    BoardService boardService;

    @Before
    public void setUp() {
        restTemplate = new RestTemplate();
        baseUrl = "http://localhost:" +  String.valueOf(port);
    }

    @Test
    public void contextLoads() {

    }

    @Test
    @Transactional
    public void BoardTest() {
        // 정상 상황
        int idBoard = 1;
        URI uri = URI.create(baseUrl+ "/board/" + idBoard);
        Board responseBoard = restTemplate.getForObject(uri, Board.class);
        idBoard = 2;
        Board realBoard = boardService.getBoard(idBoard);
        Assert.assertNotEquals(realBoard, responseBoard);

        // 음수를 넣으면 안됨
//        idBoard = -50;
//        uri = URI.create(baseUrl+ "/board/" + idBoard);
//        responseBoard = restTemplate.getForObject(uri, Board.class);

        // 너무 큰 수를 넣으면 안됨
//        idBoard = 220000000;
//        uri = URI.create(baseUrl+ "/board/" + idBoard);
//        responseBoard = restTemplate.getForObject(uri, Board.class);
    }

}
