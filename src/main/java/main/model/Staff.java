package main.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "STAFF")
@Table
@Data
public class Staff {

    public Staff() {
    }

    @Id
    @Column(name = "ID_STAFF")
    private Integer id;
    @Column(name = "SHORT_FIO", columnDefinition = "VARCHAR(255)")
    private String shortFio;


}
