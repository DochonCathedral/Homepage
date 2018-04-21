package ga.dochon.homepage.dao;

import ga.dochon.homepage.entity.Organization;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
@Repository
public class OrganizationDao {
    @PersistenceContext
    private EntityManager entityManager;

    public Organization getOrganization(int idOrganization) {
        return entityManager.find(Organization.class, idOrganization);
    }

    @SuppressWarnings("unchecked")
    public List<Organization> getAllOrganization() {
        String hql = "FROM Organization as org ORDER BY org.idOrganization";
        return (List<Organization>) entityManager.createQuery(hql).getResultList();
    }
}
