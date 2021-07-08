package jp.ac.hcs.s3a304.gourment;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ShopEntity {
	
	/** ショップ情報のリスト */
	private List<ShopData> shoplist = new ArrayList<ShopData>();
}
