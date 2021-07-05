package jp.ac.hcs.s3a304.weather;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class WeatherController {
	
	@Autowired
	private WeatherService WeatherService;

	/**
	 * 郵便番号から住所を検索し、結果画面を表示する
	 * @param zipcode 検索する郵便番号（ハイフン無し）
	 * @param principal ログイン情報
	 * @param model
	 * @return 結果画面 - 郵便番号
	 */
	@PostMapping("/weather")
	public String getWeather(@RequestParam("cityCode") String cityCode,Principal principal, Model model) {
		
		log.info("["+principal.getName()+"]"+"天気予報検索："+cityCode);
		WeatherEntity WeatherEntity = WeatherService.getWeather(cityCode);
			model.addAttribute("WeatherEntity", WeatherEntity);
			return "weather/weather";
	}
}
