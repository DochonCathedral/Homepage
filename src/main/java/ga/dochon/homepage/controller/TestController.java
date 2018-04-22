package ga.dochon.homepage.controller;

import ga.dochon.homepage.entity.Organization;
import ga.dochon.homepage.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    OrganizationService organizationService;

    @RequestMapping("/")
    public String index() {
        return "Dochon!!!";
    }

    @RequestMapping(value = "/organization/{idOrganization}", method = RequestMethod.GET)
    public Organization getOrganization(@PathVariable int idOrganization) {
        return organizationService.getOrganization(idOrganization);
        //return organization.getName() + " / " + organization.getDescription(); // 이렇게 안해도 나와야 되는데 으으으으으으으ㅡ으 몰라 내일
        // 안되는 이유는 자바 10 때문이었음. 스벌.
    }

    @RequestMapping(value = "/organization/all", method = RequestMethod.GET)
    public List<Organization> getOrganization() {
        return organizationService.getAllOrganization();
    }
}
