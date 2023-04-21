package main.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

@Entity(name = "REG_EVENTS")
@Data
public class RegisterEvents implements Serializable {

    public RegisterEvents() {
    }

    @Id
    @Column(name="ID_REG")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReg;

//    @Column(name = "AREAS_ID", columnDefinition = "INT")
//    private Integer areasId;

    @Column(name = "STAFF_ID", columnDefinition = "INT")
    private Integer staffId;

    @Column(name = "LAST_TIMESTAMP", columnDefinition = "TIMESTAMP")
    private String lastTimestamp;


}