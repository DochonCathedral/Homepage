package ga.dochon.homepage;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.ArrayList;
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
import com.oracle.jrockit.jfr.ContentType;

import ga.dochon.homepage.model.entity.Board;
import ga.dochon.homepage.model.entity.Organization;
import ga.dochon.homepage.service.OrganizationService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class OrganizationTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private OrganizationService organizationService;
	
//	@Test
//	public void organizationTests() {
//		
//		organizationPostTest();
////		organizationGetTest();
////		organizationGetListTest();
////		organizationDeleteTest();
////		organizationPatchTest();
//		
//		
//	}
	
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
	public void organizationPostTest() {
		
		try {
			////////////// 1. 정상상황 //////////////

			// 값 셋팅
			Organization organization =  new Organization()
					.setName("임원")
					.setIdParent(1)
					.setDescription("임원입니당");
			
			String json = objectToJson(organization);
			MvcResult postResult = this.mockMvc.perform(post("/organization").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk())
				.andReturn();
			// post 응답값
			Integer.parseInt(postResult.getResponse().getContentAsString());
			
			//////////////2. 예외상황 //////////////
			//////////////2-1. name 값 null인 경우 //////////////
			// 값 셋팅
			Organization nameNullOrganization = new Organization()
													.setIdParent(1)
													.setDescription("임원입니당");
			json = objectToJson(nameNullOrganization);
			
			this.mockMvc.perform(post("/organization").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().is4xxClientError())
				.andReturn();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void organizationGetTest() {
		
		try {
			
			// 정상 케이스
			int idOrganization = 1;
			URI uri = URI.create("/organization/" + idOrganization);
			List<Organization> serviceBoard = organizationService.findOrganizations(idOrganization, null, null, null);

			this.mockMvc.perform(get(uri))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.idOrganization").value(serviceBoard.get(0).getIdOrganization()))
						.andExpect(jsonPath("$.name").value(serviceBoard.get(0).getName()))
						.andExpect(jsonPath("$.description").value(serviceBoard.get(0).getDescription()));
			
			// 예외 케이스 1
			// DB에 없는 idOrganization
			idOrganization = 100;
			uri = URI.create("/organization/" + idOrganization);
			
			this.mockMvc.perform(get(uri))
						.andExpect(status().isNoContent());
			
			// 예외 케이스 2
			// idOrganization이 음수인 경우
			idOrganization = -10;
			uri = URI.create("/organization/" + idOrganization);
			
			this.mockMvc.perform(get(uri))
						.andExpect(status().is4xxClientError());
			
			// 정상 케이스
			// findAll
			uri = URI.create("/organizations/");
			List<Organization> findAll = organizationService.findOrganizations(null, null, null, null);
			
			this.mockMvc.perform(get(uri))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$[0].idOrganization").value(findAll.get(0).getIdOrganization()))
						.andExpect(jsonPath("$[1].name").value(findAll.get(1).getName()));
			
			// 정상케이스
			String query1 = "name=";
			String value1 = "테스트";
	
			uri = URI.create("/organizations?" + query1 + value1);
			
			findAll = organizationService.findOrganizations(null, null, value1, null);
			
			this.mockMvc.perform(get(uri))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$[0].idOrganization").value(findAll.get(0).getIdOrganization()))
						.andExpect(jsonPath("$[1].idOrganization").value(findAll.get(1).getIdOrganization()));
			
			// 정상케이스
			String query2 = "&idOrganization=";
			int value2 = 1;
			
			uri = URI.create("/organizations?" + query1 + value1 + query2 + value2);
			
			findAll = organizationService.findOrganizations(value2, null, value1, null);
			
			this.mockMvc.perform(get(uri))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$[0].idOrganization").value(findAll.get(0).getIdOrganization()));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void organizationDeleteTest() {
		
		int idOrganization = 1;
		URI uri = URI.create("/organization/" + idOrganization);
		
		try {
			MvcResult result = this.mockMvc.perform(delete(uri))
											.andExpect(status().isOk())
											.andReturn();
			
			boolean isSucceeded = Boolean.valueOf(result.getResponse().getContentAsString());
			Assert.assertTrue(isSucceeded);
			
			Organization findOrganization = organizationService.findOrganization(idOrganization);
			Assert.assertNull(findOrganization);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void organizationPatchTest() {
		
		URI uri = URI.create("/organization");
		
		int idOrganization = 1;
		Organization realOrganization = organizationService.findOrganization(idOrganization);
		
		// 정상상황
		Organization organizationToUpdate = realOrganization.setName("테스트 게시판이에용").setIdOrganization(2);
		String organizationJson = objectToJson(organizationToUpdate);
		
		try {
			MvcResult result = this.mockMvc.perform(patch(uri).contentType("application/json").content(organizationJson))
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