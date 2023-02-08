package io.springbatch.springbatchlecture.entity.customer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Customer {

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final Date birthDate;
}
