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
	 * cityCodeで指定した北海道の地域の天気情報の画面を表示する
	 * @param cityCode
	 * @param principal
	 * @param model
	 * @return
	 */
	@PostMapping("/weather")
	public String getWeather(@RequestParam("cityCode") String cityCode,Principal principal, Model model) {
		
		log.info("["+principal.getName()+"]"+"天気予報検索："+cityCode);
		WeatherEntity weatherEntity = WeatherService.getWeather(cityCode);
		if(weatherEntity == null) {
			model.addAttribute("errorMSG2","都市コードが正しくありません");
			return "index";
		}
		
		model.addAttribute("WeatherEntity", weatherEntity);
		return "weather/weather";
	}
}
