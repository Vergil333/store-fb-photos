package com.machava.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.DynamicUpdate;

import com.machava.demo.dtos.UserDto;

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
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String fbId;
    private String name;
    private String gender;
    @Lob
    private byte[] picture;

    public UserDto toDto() {
        return UserDto.builder()
                .fbId(this.getFbId())
                .name(this.getName())
                .gender(this.getGender())
                .picture(this.getPicture())
                .build();
    }

}
