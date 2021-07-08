package jp.ac.hcs.s3a304.gourment;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@Service
public class GourmetService {
	
	@Autowired
	RestTemplate restTemplate;
	
	private static final String URL =
			"http://webservice.recruit.co.jp/hotpepper/gourmet/v1/?key={API_KEY}&name={shopname}&large_service_area={large_service_area}&format=json";
	/**
	 * 
	 * @param API_KEY
	 * @param gourmetname
	 * @param large_service_area
	 * @return
	 */
	public ShopEntity getShopList(String API_KEY, String gourmetname, String large_service_area) {
		
		// APIへアクセスして、結果を取得
		String json = restTemplate.getForObject(URL, String.class, API_KEY, gourmetname, large_service_area);
		// エンティティクラスを生成
		ShopEntity shopEntity = new ShopEntity();
		// jsonクラスへの変換失敗のため、例外処理
		try {
		//変換クラスを生成し、文字列からjsonクラスへ変換する（例外の可能性あり）
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(json);
			
			for(JsonNode shop : node.get("results").get("shop")) {
			ShopData data = new ShopData();
			data.setId(shop.get("id").asText());
			data.setName(shop.get("name").asText());
			data.setLogo_image(shop.get("logo_image").asText());
			data.setName_kana(shop.get("name_kana").asText());
			data.setAddress(shop.get("address").asText());
			data.setAccess(shop.get("station_name").asText());
			data.setUrl(shop.get("urls").get("pc").asText());
			data.setImage(shop.get("photo").get("mobile").get("l").asText());
			shopEntity.getShoplist().add(data);
			}
		} catch (IOException e) {
			//例外発生時は、エラーメッセージの詳細を標準エラー出力
			e.printStackTrace();
		}
		return shopEntity;
		
	}
}
