package ga.dochon.homepage.service;

import ga.dochon.homepage.model.entity.Organization;
import ga.dochon.homepage.model.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository getOrganization;

    public Organization getOrganization(int idOrganization) {
        return getOrganization.getOne(idOrganization);
    }

    public List<Organization> getAllOrganization() {
        return getOrganization.findAll();
    }
}
