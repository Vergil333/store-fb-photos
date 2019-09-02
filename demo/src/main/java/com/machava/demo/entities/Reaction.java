package com.machava.demo.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicUpdate;

import com.machava.demo.dtos.ReactionDto;
import com.machava.demo.enums.EReactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@DynamicUpdate
public class Reaction {

    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private EReactionType type;
    private Long summary;
    @ManyToOne
    private Photo photo;

    public ReactionDto toDto() {
        return ReactionDto.builder()
                .type(getType())
                .summary(getSummary())
                .build();
    }

}
