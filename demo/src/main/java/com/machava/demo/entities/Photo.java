package com.machava.demo.entities;

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
    private String id;
    private String picture;
    private String link;

    public PhotoDto toDto() {
        return PhotoDto.builder()
                .id(this.getId())
                .picture(this.getPicture())
                .link(this.getLink())
                .build();
    }
}
