package com.machava.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

import com.machava.demo.dtos.PhotoDto;

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
    @GeneratedValue
    private Long id;
    private String fbId;
    private String picture;
    private String link;

    public PhotoDto toDto() {
        return PhotoDto.builder()
                .fbId(this.getFbId())
                .picture(this.getPicture())
                .link(this.getLink())
                .build();
    }
}
