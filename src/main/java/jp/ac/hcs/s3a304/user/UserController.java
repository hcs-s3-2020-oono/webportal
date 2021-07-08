package jp.ac.hcs.s3a304.user;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/user/list")
	public String getUserList(Model model) {
		
		UserEntity userEntity = userService.getUserList();
		model.addAttribute("UserEntity", userEntity);
		return "user/userList";
		
	}
	
	@GetMapping("/user/insert")
	public String getUserInsert(UserForm form, Model model) {
		return "user/insert";
	}
	
	@PostMapping("/user/insert")
	public String getUserInsert(@ModelAttribute @Validated UserForm form,
			BindingResult bindingResult,
			Principal principal,
			Model model) {
		
				return getUserList(model);
	}
}
