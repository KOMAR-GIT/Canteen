package main.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "REG_EVENTS")
@Data
public class RegisterEvents implements Serializable {

    public RegisterEvents() {
    }

    @Id
    @Column(name="ID_REG")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReg;

    @Column(name = "STAFF_ID", columnDefinition = "INT")
    private Integer staffId;

    @Column(name = "LAST_TIMESTAMP", columnDefinition = "TIMESTAMP")
    private String lastTimestamp;


}