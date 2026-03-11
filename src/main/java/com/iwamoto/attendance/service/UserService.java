package com.iwamoto.attendance.service;

import com.iwamoto.attendance.entity.User;
import com.iwamoto.attendance.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    //コンストラクションインジェクション
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //ログイン
    public User login(String username, String password){

        User user = userRepository.findByUsername(username);

        //
        if(user == null){
            return null;
        }

        if(!user.getPassword().equals(password)){
            return null;
        }

        return user;
    }
}
