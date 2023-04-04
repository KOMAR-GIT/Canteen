package main.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "STAFF")
@Table
@Data
public class Staff {

    public Staff() {
    }

    @Id
    @Column(name = "ID_STAFF")
    private Integer id;
    @Column(name = "LAST_NAME", columnDefinition = "VARCHAR(255)")
    private String lastName;
    @Column(name = "FIRST_NAME", columnDefinition = "VARCHAR(255)")
    private String firstName;
    @Column(name = "MIDDLE_NAME", columnDefinition = "VARCHAR(255)")
    private String middleName;
    @Column(name = "TABEL_ID", columnDefinition = "VARCHAR(255)", nullable = false)
    private String tabelId;
    @Column(name = "TEMPORARY_ACC", columnDefinition = "BOOLEAN", nullable = false)
    private boolean temporaryAcc;
    @Column(name = "VALID", columnDefinition = "BOOLEAN", nullable = false)
    private boolean valid;
    @Column(name = "ACCESS_BEGIN_DATE", columnDefinition = "DATE", nullable = false)
    private Date accessBeginDate;
    @Column(name = "ACCESS_END_DATE", columnDefinition = "DATE", nullable = false)
    private Date accessEndDate;
    @Column(name = "ACCESS_PROHIBIT", columnDefinition = "INT")
    private Integer accessProhibit;
    @Column(name = "DEL_GUEST_AFTER_PASS", columnDefinition = "INT")
    private Integer delGuestAfterPass;
    @Column(name = "DATE_BEGIN", columnDefinition = "DATE")
    private Date dateBegin;
    @Column(name = "PORTRET", columnDefinition = "VARCHAR(255)")
    private String portret;
    @Column(name = "DATE_DISMISS", columnDefinition = "DATE")
    private Date dateDismiss;
    @Column(name = "DELETED", columnDefinition = "INT")
    private Integer deleted;
    @Column(name = "ID_FROM_1C", columnDefinition = "VARCHAR(255)")
    private String idFrom1C;
    @Column(name = "STAFF_STATE", columnDefinition = "INT")
    private Integer staffState;
    @Column(name = "LAST_TIMESTAMP", columnDefinition = "TIMESTAMP", nullable = false)
    private Date lastTimestamp;
    @Column(name = "DIN_PAY_SCHEMES_ID", columnDefinition = "INT")
    private Integer dinPaySchemesId;
    @Column(name = "DIN_GRAPHS_ID", columnDefinition = "INT")
    private Integer dinGraphsId;
    @Column(name = "DIN_GRANT_MEASURE_SPENDED", columnDefinition = "DECIMAL(19,2)")
    private BigDecimal dinGrantMeasureSpended;
    @Column(name = "DIN_COST_VALUE_SPENDED", columnDefinition = "DECIMAL(19,2)")
    private BigDecimal dinCostValueSpended;
    @Column(name = "PATH_ACTDIR", columnDefinition = "VARCHAR(255)")
    private String pathActDir;
    @Column(name = "PATH_ACTDIR_LOGIN", columnDefinition = "VARCHAR(255)")
    private String pathActDirLogin;
    @Column(name = "PATH_ACTDIR_DOMAIN", columnDefinition = "VARCHAR(255)")
    private String pathActDirDomain;
    @Column(name = "SHORT_FIO", columnDefinition = "VARCHAR(255)")
    private String shortFio;
    @Column(name = "FULL_FIO", columnDefinition = "VARCHAR(255)")
    private String fullFio;


}
