package io.springbatch.springbatchlecture.entity.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.BitSet;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    private Long id;
    private String firstName;
    private String lastName;
    private Date birthDate;

    public Customer renewCustom() {
        return new Customer(this.getId(),
                "processed" + this.getFirstName(),
                "processed" + this.getLastName(),
                this.getBirthDate()
        );
    }

}
