package io.springbatch.springbatchlecture.entity;

import lombok.Data;

@Data
public class StudentBackUp {
    private String id;
    private String name;

    public StudentBackUp(Student student) {
    }
}
