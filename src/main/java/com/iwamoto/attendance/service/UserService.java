package com.iwamoto.attendance.service;

import com.iwamoto.attendance.entity.User;
import com.iwamoto.attendance.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    //コンストラクションインジェクション
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //ログイン
    public User login(String username, String password){

        User user = userRepository.findByUsername(username);

        //
//        if(user == null){
//            return null;
//        }
        if(user == null){
            throw new IllegalStateException("ユーザーが存在しません");
        }
        //
//        if(!user.getPassword().equals(password)){
//            return null;
//        }
//        if(!passwordEncoder.matches(password, user.getPassword())){
//            return null;
//        }
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new IllegalStateException("パスワードが違います");
        }

        return user;
    }

    public void register(User user){
        // 既に同じusernameが存在するかチェック
        User existingUser = userRepository.findByUsername(user.getUsername());

        if(existingUser != null){
            throw new IllegalStateException("このユーザー名は既に使われています");
        }

        user.setPassword((passwordEncoder.encode(user.getPassword())));
        //保存
        userRepository.save(user);
    }
}
