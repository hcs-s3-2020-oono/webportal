package jp.ac.hcs.s3a304.gourment;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class GourmetController {

	@Autowired
	private GourmetService gourmetService;
	
	private static final String API_KEY = "f42051e847492465";
	/**
	 * 
	 * @param gourmetname
	 * @param principal
	 * @param model
	 * @return gourmet.html
	 */
	@PostMapping("/gourmet")
	public String getShop(@RequestParam("gourmetname") String gourmetname, Principal principal, Model model) {
		String large_service_area = "SS40";
		
		ShopEntity shopEntity = gourmetService.getShopList(API_KEY, gourmetname, large_service_area);
		log.info("["+principal.getName()+"]"+"グルメ検索："+gourmetname);
		model.addAttribute("ShopEntity", shopEntity);
		model.addAttribute("gourmetname", gourmetname);
		return "gourmet/gourmet";
		
	}
}
