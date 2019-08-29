package com.machava.demo.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @Id
    @NaturalId
    private Long id;
    private String name;
    private String gender;
    private String picture;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<Photo> photos;

    public UserDto toDto() {
        return UserDto.builder()
                .id(getId())
                .name(getName())
                .gender(getGender())
                .picture(getPicture())
                .build();
    }

}
