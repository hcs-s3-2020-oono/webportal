package jp.ac.hcs.s3a304.user;

import org.springframework.beans.factory.annotation.Autowired;
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

}
