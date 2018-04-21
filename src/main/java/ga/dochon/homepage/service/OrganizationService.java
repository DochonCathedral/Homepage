package ga.dochon.homepage.service;

import ga.dochon.homepage.dao.OrganizationDao;
import ga.dochon.homepage.entity.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationDao organizationDao;

    public Organization getOrganization(int idOrganization) {
        Organization organization = organizationDao.getOrganization(idOrganization);
        return organization;
    }

    public List<Organization> getAllOrganization() {
        List<Organization> organization = organizationDao.getAllOrganization();
        return organization;
    }
}
