package com.example.Messaging.Service.dao;

import com.example.Messaging.Service.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  UserRepo extends MongoRepository<User,String> {
    Optional<User> findByUsername(String username);//Login için
    boolean existsByUsername(String username);//Kayıt olurken kontrol
    boolean existsByMail(String email);// Mailin daha once kullanilip kullanilmadiginin kontrolu


}
