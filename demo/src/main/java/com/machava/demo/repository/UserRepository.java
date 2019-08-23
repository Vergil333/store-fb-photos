package com.machava.demo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.machava.demo.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}
