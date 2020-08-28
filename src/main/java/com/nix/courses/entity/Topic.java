package com.nix.courses.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "topics")
public class Topic extends AbstractEntityWithGeneratedId {

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(nullable = false)
    private String name;

    public Topic() {
    }
}
