package com.machava.demo.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.machava.demo.entities.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private String fbId;
    private String name;
    private String gender;
    private byte[] picture;

    public User toEntity() {
        return User.builder()
                .fbId(this.getFbId())
                .name(this.getName())
                .gender(this.getGender())
                .picture(this.getPicture())
                .build();
    }
}
