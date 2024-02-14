package com.moika22.canteen.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "REG_EVENTS")
@Table
@Data
public class RegisterEvent implements Serializable {

    public RegisterEvent() {
    }

    @Id
    @Column(name = "ID_REG")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReg;

    @Column(name = "STAFF_ID", columnDefinition = "INT")
    private Integer staffId;

    @Column(name = "LAST_TIMESTAMP", columnDefinition = "TIMESTAMP")
    private String lastTimestamp;


}