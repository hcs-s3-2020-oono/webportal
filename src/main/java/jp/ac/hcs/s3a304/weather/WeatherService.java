package jp.ac.hcs.s3a304.weather;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 天気情報を操作する
 * お天気APIを活用する
 * - https://weather.tsukumijima.net
 */
@Transactional
@Service
public class WeatherService {
	
	@Autowired
	RestTemplate restTemplate;
	
	/**お天気API リクエストURL */
	private static final String URL = "https://weather.tsukumijima.net/api/forecast?city={cityCode}";
	
	/**
	 * 気象情報取得業務ロジック
	 * @param cityCode 都市コード
	 * @return WeatherEntity
	 */
	public WeatherEntity getWeather(String cityCode) {
		
		// APIへアクセスして、結果を取得
		String json = restTemplate.getForObject(URL, String.class, cityCode);
		// エンティティクラスを生成
		WeatherEntity weatherEntity = new WeatherEntity();
		
		// jsonクラスへの変換失敗のため、例外処理
		try {
			//変換クラスを生成し、文字列からjsonクラスへ変換する（例外の可能性あり）
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(json);
			
			if(node.get("forecasts") == null) {
				return null;
			}
			String description = node.get("description").get("text").asText();
			weatherEntity.setDescription(description);
			// forecast(配列)をForEachで配列分繰り返す
			for(JsonNode forecast : node.get("forecasts")) {
				WeatherData data = new WeatherData();
				// dataLabelをDataクラスへ設定
				data.setDateLabel(forecast.get("dateLabel").asText());
				//telopをDataクラスへ設定
				data.setTelop(forecast.get("telop").asText());
				// 可変長配列の末尾に追加
				weatherEntity.getForecasts().add(data);
			}
		} catch (IOException e) {
			//例外発生時は、エラーメッセージの詳細を標準エラー出力
			e.printStackTrace();
		}
		return weatherEntity;
		
	}

}
