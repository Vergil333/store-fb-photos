package com.machava.demo.managers.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.machava.demo.entities.Photo;
import com.machava.demo.entities.Reaction;
import com.machava.demo.enums.EReactionType;
import com.machava.demo.repository.ReactionRepository;

@Service
public class ReactionRepositoryManager {

    @Autowired
    ReactionRepository reactionRepository;

    public Reaction getReactionIfExists(EReactionType type, Photo photo) {

        Reaction reaction = reactionRepository.findFirstByTypeAndPhoto(type, photo);

        if (reaction != null) {
            return reaction;
        } else {
            return new Reaction(null, type, null, photo);
        }
    }

}
