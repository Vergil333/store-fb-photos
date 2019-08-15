package com.machava.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.machava.demo.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByFbId(String fbId);
    Boolean existsByFbId(String fbId);

}
