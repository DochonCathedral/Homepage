package ga.dochon.homepage.service;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import ga.dochon.homepage.model.entity.User;
import ga.dochon.homepage.model.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

//    public Organization findOrganization(int idOrganization) {
//    	Example<Organization> idExample = Example.of(new Organization().setIdOrganization(idOrganization));
//        return userRepository.findOne(idExample).orElse(null);
//    }
    
    public User findUser(int idUser) {
    	Example<User> idExample = Example.of(new User().setIdUser(idUser));
    	return userRepository.findOne(idExample).orElse(null);
    }
    
    public List<User> findUsers(Integer idUser, Integer idKakao, String name, String nickname) {
        User user = new User();
        if (Objects.nonNull(idUser))
        	user.setIdUser(idUser);
        if (Objects.nonNull(idKakao))
        	user.setIdUser(idKakao);
        if (Objects.nonNull(name))
        	user.setName(name);
        if (Objects.nonNull(nickname))
        	user.setName(nickname);
         ExampleMatcher matcher = ExampleMatcher.matching()
        		 .withMatcher("name", contains());
        Example<User> example = Example.of(user, matcher);
    	
    	return userRepository.findAll(example);
    }

	public Integer createOrganization(User user) {
		return userRepository.save(user).getIdUser();
	}

	@Transactional
	public boolean deleteUser(int idUser) {
		if ( userRepository.existsById(idUser) ) {
			userRepository.deleteById(idUser);
			return true;
		}else {
			return false;
		}
	}

	@Transactional
	public boolean updateUser(@Valid User user) {
        if (userRepository.existsById(user.getIdUser())) {
        	userRepository.save(user);
            return true;
        } else {
            return false;
        }
	}
}