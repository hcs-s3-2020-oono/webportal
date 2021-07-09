package jp.ac.hcs.s3a304.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	
	public UserEntity getUserList() {
		UserEntity userEntity;
		
		userEntity = userRepository.selectAll();
		
		return userEntity;
		
	}

	/**
	 * 入力項目をUserDataへ変換する
	 * （このメソッドは入力チェックを実施したうえで呼び出すこと）
	 * @param 入力データ
	 * @return UserData
	 */
	public boolean insertOne(UserData userData) {
		int rowNumber;
		try {
			rowNumber = userRepository.insertOne(userData);
		} catch (DataAccessException e) {
			e.printStackTrace();
			rowNumber = 0;
		}
		return rowNumber > 0;
	}
	
	UserData refillToData(UserForm form) {
		UserData data = new UserData();
		data.setUser_id(form.getUser_id());
		data.setPassword(form.getPassword());
		data.setUser_name(form.getUser_name());
		data.setDarkmode(form.isDarkmode());
		data.setRole(form.getRole());
		//初期値は有効とする
		data.setEnabled(true);
		return data;
	}
	
	public UserData selectOne(String user_id) {
		UserData data = userRepository.selectOne(user_id);
		return data;
	}
	
	public boolean deleteOne(String user_id) {
		int rowNumber;
		try {
			rowNumber = userRepository.deleteOne(user_id);
		} catch (DataAccessException e) {
			e.printStackTrace();
			rowNumber = 0;
		}
		return rowNumber > 0;
	}
	
	public boolean updateOne(UserData userData) {
		int rowNumber;
		try {
			UserData userData2 = userRepository.selectOne(userData.getUser_id());
			if(userData.getPassword().equals(userData2.getPassword())) {
				rowNumber = userRepository.updateOne(userData);
			} else {
				rowNumber = userRepository.updateOneWithPassword(userData);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			rowNumber = 0;
		}
		return rowNumber > 0;
	}
	
	UserData refillToData2(UserFormForUpdata form) {
		UserData data = new UserData();
		data.setUser_id(form.getUser_id());
		data.setPassword(form.getPassword());
		data.setUser_name(form.getUser_name());
		data.setDarkmode(form.isDarkmode());
		data.setRole(form.getRole());
		return data;
	}
}
