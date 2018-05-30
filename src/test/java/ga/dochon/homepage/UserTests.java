package ga.dochon.homepage;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ga.dochon.homepage.model.entity.User;
import ga.dochon.homepage.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserService userService;
	
	@Test
	public void organizationTests() {
		
		userPostTest();
		userGetTest();
		userDeleteTest();
		userPatchTest();
		
	}
	
	private String objectToJson(Object obj){
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.getMessage();
			return null;
		}
		
	}

	@Test
	public void userPostTest() {
		
		try {
			////////////// 1. 정상상황 //////////////
			// 값 셋팅
			User user =  new User()
							.setIdKakao(123123123)
							.setName("테스트")
							.setNickname("토마스")
							.setDateJoined(new Date());
					
			String json = objectToJson(user);
			
			MvcResult postResult = this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk())
				.andReturn();
			// post 응답값
			Integer.parseInt(postResult.getResponse().getContentAsString());
			
			//////////////2. 예외상황 //////////////
			//////////////2-1. idKakao 값 null인 경우 //////////////
			// 값 셋팅
			User idKakaoNullUser = new User()
									   .setName("테스트2")
									   .setNickname("토마스테스트")
									   .setDateJoined(new Date());
			json = objectToJson(idKakaoNullUser);
			
			this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().is4xxClientError())
				.andReturn();

			//////////////2-2. name 값 null인 경우 //////////////
			// 값 셋팅
			User nameNullUser = new User()
					.setIdKakao(123456789)
//					.setName("테스트2")
					.setNickname("토마스테스트")
					.setDateJoined(new Date());
			json = objectToJson(nameNullUser);
			
			this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(json))
			.andExpect(status().is4xxClientError())
			.andReturn();

			//////////////2-3. nickName 값 null인 경우 //////////////
			// 값 셋팅
			User nickNameNullUser = new User()
					.setIdKakao(123456789)
					.setName("테스트2")
//					.setNickname("토마스테스트")
					.setDateJoined(new Date());
			json = objectToJson(nickNameNullUser);
			
			this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(json))
			.andExpect(status().is4xxClientError())
			.andReturn();
			
			//////////////2-4. dateJoined 값 null인 경우 //////////////
			// 값 셋팅
			User dateJoinedNullUser = new User()
					.setIdKakao(123456789)
					.setName("테스트2")
					.setNickname("토마스테스트");
//					.setDateJoined(new Date());
			json = objectToJson(dateJoinedNullUser);
			
			this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(json))
			.andExpect(status().is4xxClientError())
			.andReturn();
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void userGetTest() {
		
		try {
			// 정상 케이스
			int idUser = 1;
			URI uri = URI.create("/user/" + idUser);
			User serviceUser = userService.findUser(idUser);

			this.mockMvc.perform(get(uri))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.idUser").value(serviceUser.getIdUser()))
						.andExpect(jsonPath("$.idKakao").value(serviceUser.getIdKakao()))
						.andExpect(jsonPath("$.name").value(serviceUser.getName()))
						.andExpect(jsonPath("$.nickname").value(serviceUser.getNickname()));
			
			// 예외 케이스 1
			// DB에 없는 idOrganization
			idUser = 100;
			uri = URI.create("/user/" + idUser);
			
			this.mockMvc.perform(get(uri))
						.andExpect(status().isNoContent());
			
			// 예외 케이스 2
			// idOrganization이 음수인 경우
			idUser = -10;
			uri = URI.create("/user/" + idUser);
			
			this.mockMvc.perform(get(uri))
						.andExpect(status().is4xxClientError());
			
			// 정상 케이스
			// findAll
			uri = URI.create("/users/");
			List<User> findAll = userService.findUsers(null, null, null, null);
			
			this.mockMvc.perform(get(uri))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$[0].idKakao").value(findAll.get(0).getIdKakao()))
						.andExpect(jsonPath("$[1].name").value(findAll.get(1).getName()));
			
			// 정상케이스
			String query1 = "name=";
			String value1 = "이기";
	
			uri = URI.create("/users?" + query1 + value1);
			
			findAll = userService.findUsers(null, null, value1, null);
			
			this.mockMvc.perform(get(uri))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$[0].name").value(findAll.get(0).getName()))
						.andExpect(jsonPath("$[1].name").value(findAll.get(1).getName()));
			
			// 정상케이스
			String query2 = "&idUser=";
			int value2 = 2;
			
			uri = URI.create("/users?" + query1 + value1 + query2 + value2);
			
			findAll = userService.findUsers(value2, null, value1, null);
			
			this.mockMvc.perform(get(uri))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$[0].idUser").value(findAll.get(0).getIdUser()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void userDeleteTest() {
		
		int idUser = 1;
		URI uri = URI.create("/user/" + idUser);
		
		try {
			MvcResult result = this.mockMvc.perform(delete(uri))
											.andExpect(status().isOk())
											.andReturn();
			
			boolean isSucceeded = Boolean.valueOf(result.getResponse().getContentAsString());
			Assert.assertTrue(isSucceeded);
			
			User findUser = userService.findUser(idUser);
			Assert.assertNull(findUser);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void userPatchTest() {
		
		URI uri = URI.create("/user");
		
		int idUser = 2;
		User realUser = userService.findUser(idUser);
		
		// 정상상황
		User userToUpdate = realUser
							.setNickname("아퀴나스").setIdUser(2);
		String userJson = objectToJson(userToUpdate);
		
		try {
			MvcResult result = this.mockMvc.perform(patch(uri).contentType("application/json").content(userJson))
						.andExpect(status().isOk())
						.andReturn();
			Boolean isSucceeded = Boolean.valueOf(result.getResponse().getContentAsString());
			Assert.assertTrue(isSucceeded);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}