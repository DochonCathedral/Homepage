package ga.dochon.homepage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ga.dochon.homepage.entity.Organization;
import ga.dochon.homepage.service.OrganizationService;

@Controller
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
    	return organizationService.getOrganization(idOrganization);
    }

    @RequestMapping(value = "/organization/all", method = RequestMethod.GET)
    public List<Organization> getOrganization() {
        return organizationService.getAllOrganization();
    }
}
