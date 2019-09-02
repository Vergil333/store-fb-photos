package com.machava.demo.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;

import com.machava.demo.dtos.PhotoDto;
import com.machava.demo.dtos.ReactionDto;

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
public class Photo {

    @Id
    @NaturalId
    private Long id;
    @ManyToOne
    private User user;
    private String name;
    private String link;
    private String picture;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "photo")
    private List<Reaction> reactions;

    public PhotoDto toDto() {
        return PhotoDto.builder()
                .id(getId())
                .name(getName())
                .link(getLink())
                .picture(getPicture())
                .reactions(reactionsToDto())
                .build();
    }

    private List<ReactionDto> reactionsToDto() {
        List<ReactionDto> reactionDtoList = new ArrayList<>();

        for (Reaction reaction : this.reactions) {
            reactionDtoList.add(reaction.toDto());
        }

        return reactionDtoList;
    }

}
