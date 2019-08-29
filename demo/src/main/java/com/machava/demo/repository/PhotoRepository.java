package com.machava.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.machava.demo.entities.Photo;
import com.machava.demo.entities.User;

@Repository
public interface PhotoRepository extends CrudRepository<Photo, Long> {

    List<Photo> findAllByUser(User user);

}
