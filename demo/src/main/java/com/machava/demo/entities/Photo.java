package com.machava.demo.entities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

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
    @Lob
    private byte[] reactions;

    public PhotoDto toDto() {
        return PhotoDto.builder()
                .id(getId())
                .name(getName())
                .link(getLink())
                .picture(getPicture())
                .reactions(getReactions())
                .build();
    }

    public void setReactions(List<ReactionDto> reactions) {
        this.reactions = serializeReactions(reactions);
    }

    public List<ReactionDto> getReactions() {
        return deserializeReactions(reactions);
    }

    private static byte[] serializeReactions(List<ReactionDto> reactionDtoList) {
        byte[] reactionsInString = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(reactionDtoList);
            oos.flush();
            reactionsInString = baos.toByteArray();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        return reactionsInString;
    }

    @SuppressWarnings("unchecked")
    private static List<ReactionDto> deserializeReactions(byte[] reactionsInString) {
        List<ReactionDto> reactionDtoList = null;
        byte[] reactionsInBytes = reactionsInString;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(reactionsInBytes)) {
            ObjectInputStream ois = new ObjectInputStream(bais);
            reactionDtoList = (List<ReactionDto>) ois.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }

        return reactionDtoList;
    }

}
