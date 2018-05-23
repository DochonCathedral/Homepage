package ga.dochon.homepage.service;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.hibernate.query.criteria.internal.ValueHandlerFactory.IntegerValueHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.org.apache.xpath.internal.operations.Or;

import ga.dochon.homepage.model.entity.Article;
import ga.dochon.homepage.model.entity.Board;
import ga.dochon.homepage.model.entity.Organization;
import ga.dochon.homepage.model.entity.Reply;
import ga.dochon.homepage.model.repository.OrganizationRepository;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository organizationRepository;

    public Organization findOrganization(int idOrganization) {
    	Example<Organization> idExample = Example.of(new Organization().setIdOrganization(idOrganization));
        return organizationRepository.findOne(idExample).orElse(null);
    }

    public List<Organization> findOrganizations(Integer idOrganization, Integer idParent, String name, String description) {
        Organization organization = new Organization();
        if (Objects.nonNull(idOrganization))
        	organization.setIdOrganization(idOrganization);
        if (Objects.nonNull(idParent))
        	organization.setIdParent(idParent);
        if (Objects.nonNull(name))
        	organization.setName(name);
        if (Objects.nonNull(description))
        	organization.setDescription(description);
        ExampleMatcher matcher = ExampleMatcher.matching()
        		.withMatcher("name", contains());
        Example<Organization> example = Example.of(organization, matcher);
    	
    	return organizationRepository.findAll(example);
    }

	public Integer createOrganization(Organization organization) {
		return organizationRepository.save(organization).getIdOrganization();
	}

	@Transactional
	public boolean deleteOrganization(int idOrganization) {
		if ( organizationRepository.existsById(idOrganization) ) {
			Example<Organization> organizationExample = Example.of(new Organization().setIdOrganization(idOrganization));
			for ( Organization organization : organizationRepository.findAll(organizationExample) ) {
				organizationRepository.deleteById(idOrganization);
			}
			return true;
		}else {
			return false;
		}
	}

	@Transactional
	public boolean updateOrganization(@Valid Organization organization) {
        if (organizationRepository.existsById(organization.getIdOrganization())) {
        	organizationRepository.save(organization);
            return true;
        } else {
            return false;
        }
	}
}