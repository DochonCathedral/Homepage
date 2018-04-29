package ga.dochon.homepage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ga.dochon.homepage.entity.Organization;
import ga.dochon.homepage.service.OrganizationService;

@RestController
public class TestController {
    @Autowired
    OrganizationService organizationService;

    @RequestMapping("/")
    public String index(Model model) {
    	model.addAttribute("name", "우와~");
        return "index";
    }

    @RequestMapping(value = "/organization/{idOrganization}", method = RequestMethod.GET)
    public Organization getOrganization(@PathVariable int idOrganization) {
    	Organization a = organizationService.getOrganization(idOrganization);
        
    	return a;
        //return organization.getName() + " / " + organization.getDescription(); // 이렇게 안해도 나와야 되는데 으으으으으으으ㅡ으 몰라 내일
        // 안되는 이유는 자바 10 때문이었음. 스벌.
    }

    @RequestMapping(value = "/organization/all", method = RequestMethod.GET)
    public List<Organization> getOrganization() {
        return organizationService.getAllOrganization();
    }
}
