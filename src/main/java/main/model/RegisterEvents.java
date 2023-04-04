package main.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity(name = "REG_EVENTS")
@Table
@Data
public class RegisterEvents {

    public RegisterEvents() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "INNER_NUMBER_EV", columnDefinition = "INT", nullable = false)
    private Integer innerNumberEv;

    @Column(name = "DATE_EV", columnDefinition = "DATE", nullable = false)
    private Date dateEv;

    @Column(name = "TIME_EV", columnDefinition = "TIME", nullable = false)
    private Date timeEv;

    @Column(name = "IDENTIFIER", columnDefinition = "BIGINT", nullable = false)
    private BigInteger identifier;

    @Column(name = "CONFIGS_TREE_ID_CONTROLLER", columnDefinition = "INT", nullable = false)
    private Integer configsTreeIdController;

    @Column(name = "CONFIGS_TREE_ID_RESOURCE", columnDefinition = "INT", nullable = false)
    private Integer configsTreeIdResource;

    @Column(name = "TYPE_PASS", columnDefinition = "INT", nullable = false)
    private Integer typePass;

    @Column(name = "CATEGORY_EV", columnDefinition = "INT", nullable = false)
    private Integer categoryEv;

    @Column(name = "SUBCATEGORY_EV", columnDefinition = "INT", nullable = false)
    private Integer subcategoryEv;

    @Column(name = "AREAS_ID", columnDefinition = "INT", nullable = false)
    private Integer areasId;

    @Column(name = "STAFF_ID", columnDefinition = "INT", nullable = false)
    private Integer staffId;

    @Column(name = "USER_ID", columnDefinition = "INT", nullable = false)
    private Integer userId;

    @Column(name = "TYPE_IDENTIFIER", columnDefinition = "INT", nullable = false)
    private Integer typeIdentifier;

    @Column(name = "VIDEO_MARK", columnDefinition = "VARCHAR(255)", nullable = false)
    private String videoMark;

    @Column(name = "LAST_TIMESTAMP", columnDefinition = "TIMESTAMP", nullable = false)
    private Date lastTimestamp;

    @Column(name = "IDENTIFIER_OWNER_TYPE", columnDefinition = "INT", nullable = false)
    private Integer identifierOwnerType;

    @Column(name = "AVT_CAM_DBID", columnDefinition = "INT", nullable = false)
    private Integer avtCamDbid;

    @Column(name = "SUBDIV_ID", columnDefinition = "INT", nullable = false)
    private Integer subdivId;

    @Column(name = "CONTROLLER_EVENT_ID", columnDefinition = "INT", nullable = false)
    private Integer controllerEventId;

    @Column(name = "STATE_NUMBER", columnDefinition = "INT", nullable = false)
    private Integer stateNumber;

    @Column(name = "CTRL_TIME_ZONE_DATE_EV", columnDefinition = "DATE", nullable = false)
    private Date ctrlTimeZoneDateEv;

    @Column(name = "CTRL_TIME_ZONE_TIME_EV", columnDefinition = "TIME", nullable = false)
    private Date ctrlTimeZoneTimeEv;

    @Column(name = "COMISS_USER_ID_1", columnDefinition = "INT", nullable = false)
    private Integer comissUserId1;

    @Column(name = "COMISS_USER_ID_2", columnDefinition = "INT", nullable = false)
    private Integer comissUserId2;



}
