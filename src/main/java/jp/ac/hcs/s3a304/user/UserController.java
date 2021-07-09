package jp.ac.hcs.s3a304.user;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
		
		if(bindingResult.hasErrors()) {
			return getUserInsert(form, model);
		}
		UserData data = userService.refillToData(form);
		boolean isSuccess = userService.insertOne(data);
		log.info("["+principal.getName()+"]"+"ユーザ追加："+ form.getUser_name());
		if(isSuccess) {
			model.addAttribute("okMSG", "正常に作成されました");
		}else {
			model.addAttribute("errorMSG", "入力値に誤りがあります");
		}
		return getUserList(model);
	}
	
	@GetMapping("/user/detail/{id}")
	public String getUserDetail(@PathVariable("id") String user_id,
			Principal principal,
			Model model) {
		if(user_id.equals("")) {
			return getUserList(model);
		} else if(!user_id.matches("^([a-zA-Z0-9])+([a-zA-Z0-9\\._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9\\._-]+)+$")) {
			return getUserList(model);
		}
		UserData data = userService.selectOne(user_id);
		if(data == null) {
			return getUserList(model);
		}
		model.addAttribute("userData", data);
		log.info("["+principal.getName()+"]" + "詳細画面：" + user_id);
		return "user/detail";
		
	}
	
	@PostMapping("/user/delete")
	public String deleteUser(@RequestParam("user_id") String user_id,
			Principal principal,
			Model model) {
		System.out.println(user_id);
		boolean isSuccess = userService.deleteOne(user_id);
		
		if(isSuccess) {
			model.addAttribute("okMSG", "正常に削除されました");
		}else {
			model.addAttribute("errorMSG", "削除できませんでした");
		}
		log.info("["+principal.getName()+"]" + "ユーザ削除：" + user_id);
		return getUserList(model);
	}
	
	@PostMapping("/user/update")
	public String updateUser(@ModelAttribute @Validated UserFormForUpdata form,
			BindingResult bindingResult,
			Principal principal,
			Model model) {
		
		if(bindingResult.hasErrors()) {
			return getUserDetail(form.getUser_id(), principal, model);
		}
		UserData data = userService.refillToData2(form);
		boolean isSuccess = userService.updateOne(data);
		log.info("["+principal.getName()+"]"+"ユーザ更新："+ form.getUser_id());
		if(isSuccess) {
			model.addAttribute("okMSG", "正常に更新されました");
		}else {
			model.addAttribute("errorMSG", "更新できませんでした");
		}
		return getUserList(model);
	}
}
