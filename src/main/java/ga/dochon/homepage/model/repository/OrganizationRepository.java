package ga.dochon.homepage.model.repository;

import ga.dochon.homepage.model.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {

}
