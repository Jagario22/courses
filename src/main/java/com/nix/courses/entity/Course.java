package com.nix.courses.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "courses")
public class Course extends AbstractEntityWithGeneratedId {

    @Column(nullable = false, unique = true)
    private String name;

    public Course() {
    }
}
