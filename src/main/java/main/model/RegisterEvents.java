package main.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

@Entity(name = "REG_EVENTS")
@Data
public class RegisterEvents {

    public RegisterEvents() {
    }

    @Id
    @Column(name="ID_REG")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReg;

    @Column(name = "INNER_NUMBER_EV", columnDefinition = "INT")
    private Integer innerNumberEv;

    @Column(name = "DATE_EV", columnDefinition = "DATE")
    private Date dateEv;

    @Column(name = "TIME_EV", columnDefinition = "TIME")
    private Date timeEv;

    @Column(name = "IDENTIFIER", columnDefinition = "BIGINT")
    private BigInteger identifier;

    @Column(name = "CONFIGS_TREE_ID_CONTROLLER", columnDefinition = "INT")
    private Integer configsTreeIdController;

    @Column(name = "CONFIGS_TREE_ID_RESOURCE", columnDefinition = "INT")
    private Integer configsTreeIdResource;

    @Column(name = "TYPE_PASS", columnDefinition = "INT")
    private Integer typePass;

    @Column(name = "CATEGORY_EV", columnDefinition = "INT")
    private Integer categoryEv;

    @Column(name = "SUBCATEGORY_EV", columnDefinition = "INT")
    private Integer subcategoryEv;

    @Column(name = "AREAS_ID", columnDefinition = "INT")
    private Integer areasId;

    @Column(name = "STAFF_ID", columnDefinition = "INT")
    private Integer staffId;

    @Column(name = "USER_ID", columnDefinition = "INT")
    private Integer userId;

    @Column(name = "TYPE_IDENTIFIER", columnDefinition = "INT")
    private Integer typeIdentifier;

    @Column(name = "VIDEO_MARK", columnDefinition = "VARCHAR(255)")
    private String videoMark;

    @Column(name = "LAST_TIMESTAMP", columnDefinition = "TIMESTAMP")
    private String lastTimestamp;

    @Column(name = "IDENTIFIER_OWNER_TYPE", columnDefinition = "INT")
    private Integer identifierOwnerType;

    @Column(name = "AVT_CAM_DBID", columnDefinition = "INT")
    private Integer avtCamDbid;

    @Column(name = "SUBDIV_ID", columnDefinition = "INT")
    private Integer subdivId;

    @Column(name = "CONTROLLER_EVENT_ID", columnDefinition = "BIGINT")
    private BigInteger controllerEventId;

    @Column(name = "STATE_NUMBER", columnDefinition = "VARCHAR(255)")
    private String stateNumber;

    @Column(name = "CTRL_TIME_ZONE_DATE_EV", columnDefinition = "DATE")
    private Date ctrlTimeZoneDateEv;

    @Column(name = "CTRL_TIME_ZONE_TIME_EV", columnDefinition = "TIME")
    private Date ctrlTimeZoneTimeEv;

    @Column(name = "COMISS_USER_ID_1", columnDefinition = "INT")
    private Integer comissUserId1;

    @Column(name = "COMISS_USER_ID_2", columnDefinition = "INT")
    private Integer comissUserId2;

    @Column(name = "ID", columnDefinition = "INT")
    private Integer id;


}
