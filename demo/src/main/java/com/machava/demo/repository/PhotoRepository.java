package com.machava.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.machava.demo.entities.Photo;

@Repository
public interface PhotoRepository extends CrudRepository<Photo, Long> {

    List<Photo> findAllByUserId(Long user_id);

}
