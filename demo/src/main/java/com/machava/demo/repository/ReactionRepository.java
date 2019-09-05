package com.machava.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.machava.demo.entities.Photo;
import com.machava.demo.entities.Reaction;
import com.machava.demo.enums.EReactionType;

@Repository
public interface ReactionRepository extends CrudRepository<Reaction, Long> {

    Reaction findFirstByTypeAndPhoto(EReactionType type, Photo photo);

}
