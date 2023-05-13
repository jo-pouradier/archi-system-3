package com.sp.repository;

import com.sp.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    default User findByName(String username){
        List<User> users = new ArrayList<User>();
        this.findAll().iterator().forEachRemaining(user -> {
            if(user.getName().equals(username)){
                users.add(user);
            }
        });
        users.add(null);
        return users.get(0);
    }
    default User findByEmail(String email){
        List<User> users = new ArrayList<User>();
        this.findAll().iterator().forEachRemaining(user -> {
            if(user.getEmail().equals(email)){
                users.add(user);
            }
        });
        users.add(null);
        return users.get(0);
    }
}
