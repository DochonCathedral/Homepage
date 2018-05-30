package ga.dochon.homepage.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ga.dochon.homepage.model.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}