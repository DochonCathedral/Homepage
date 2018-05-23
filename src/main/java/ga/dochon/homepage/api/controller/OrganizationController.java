package ga.dochon.homepage.api.controller;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ga.dochon.homepage.model.entity.Board;
import ga.dochon.homepage.model.entity.Organization;
import ga.dochon.homepage.service.OrganizationService;

@Controller
public class OrganizationController {
	
	@Autowired
	private OrganizationService organizationService;
	
	@PostMapping("/organization")
	@ResponseBody
	public ResponseEntity<?> createOrganization(@Valid @RequestBody Organization organization){
        try {
        	return new ResponseEntity<>(organizationService.createOrganization(organization),HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}
	
    @GetMapping("/organization/{idOrganization}")
    @ResponseBody
    public ResponseEntity<?> findOrganization(@PathVariable int idOrganization) {
        if (idOrganization <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Organization organization = organizationService.findOrganization(idOrganization);

        if (Objects.nonNull(organization)) {
            return new ResponseEntity<>(organization,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    @GetMapping("/organizations")
    @ResponseBody
    public ResponseEntity<List<Organization>> findOrganizations(@RequestParam(value = "idOrganization", required=false) Integer idOrganization,
                                   @RequestParam(value = "idParent", required=false) Integer idParent,
                                   @RequestParam(value = "name", required=false) String name,
                                   @RequestParam(value = "description", required=false) String description) {
        return new ResponseEntity<>(organizationService.findOrganizations(idOrganization, idParent, name, description), HttpStatus.OK);
    }
    
    @DeleteMapping("/organization/{idOrganization}")
    @ResponseBody
    public ResponseEntity<Boolean> deleteOrganization(@PathVariable int idOrganization) {
        if (idOrganization <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(organizationService.deleteOrganization(idOrganization), HttpStatus.OK);
    }
    
    @PatchMapping("/organization")
    @ResponseBody
    public ResponseEntity<Boolean> updateOrganization(@Valid @RequestBody Organization organization) {
        try {
            return new ResponseEntity<>(organizationService.updateOrganization(organization), HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}