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

import ga.dochon.homepage.model.entity.User;
import ga.dochon.homepage.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/user")
	@ResponseBody
	public ResponseEntity<?> createUser(@Valid @RequestBody User user){
        try {
        	return new ResponseEntity<>(userService.createOrganization(user),HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}
	
    @GetMapping("/user/{idUser}")
    @ResponseBody
    public ResponseEntity<?> findUser(@PathVariable int idUser) {
        if (idUser <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.findUser(idUser);

        if (Objects.nonNull(user)) {
            return new ResponseEntity<>(user,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    @GetMapping("/users")
    @ResponseBody
    public ResponseEntity<List<User>> findUsers(@RequestParam(value = "idUser", required=false) Integer idUser,
    							   @RequestParam(value = "idKakao", required=false) Integer idKakao,
                                   @RequestParam(value = "name", required=false) String name,
                                   @RequestParam(value = "nickname", required=false) String nickname) {
        return new ResponseEntity<>(userService.findUsers(idUser, idKakao, name, nickname), HttpStatus.OK);
    }
    
    @DeleteMapping("/user/{idUser}")
    @ResponseBody
    public ResponseEntity<Boolean> deleteUser(@PathVariable int idUser) {
        if (idUser <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.deleteUser(idUser), HttpStatus.OK);
    }
    
    @PatchMapping("/user")
    @ResponseBody
    public ResponseEntity<Boolean> updateUser(@Valid @RequestBody User user) {
        try {
            return new ResponseEntity<>(userService.updateUser(user), HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}