import com.moika22.canteen.ApplicationProperties;
import com.moika22.canteen.api.LastEmployee;
import com.moika22.canteen.model.RegisterEvent;
import com.moika22.canteen.model.Staff;
import com.moika22.canteen.repository.RegisterEventsRepository;
import com.moika22.canteen.repository.StaffRepository;
import com.moika22.canteen.service.MainService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
public class MainServiceTest {

    @Mock
    private StaffRepository staffRepository;
    @Mock
    private RegisterEventsRepository registerEventsRepository;
    @Mock
    private ApplicationProperties applicationProperties;
    @InjectMocks
    private MainService mainService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getPrintablePerson() {
        int staffId = 126632;
        Mockito.when(registerEventsRepository.getEvents(any())).thenReturn(getEvents());
        Mockito.when(staffRepository.findById(staffId)).thenReturn(Optional.of(getStaff()));

        LastEmployee lastEmployee = mainService.getPrintablePerson();
        Assertions.assertNotNull(lastEmployee);
        Assertions.assertEquals("Циклаури Д.Ш.", lastEmployee.getName());
    }

    private List<RegisterEvent> getEvents() {
        RegisterEvent registerEvent = new RegisterEvent();
        registerEvent.setStaffId(158216);
        registerEvent.setIdReg(1);
        registerEvent.setLastTimestamp("2024-01-01 11:50:00");

        RegisterEvent registerEvent1 = new RegisterEvent();
        registerEvent1.setIdReg(2);
        registerEvent1.setStaffId(126632);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, -30);
        registerEvent1.setLastTimestamp("2024-01-01 12:01:01");

        return List.of(registerEvent, registerEvent1);
    }

    private Staff getStaff() {
        Staff staff = new Staff();
        staff.setId(126632);
        staff.setShortFio("Циклаури Д.Ш.");
        return staff;
    }
}
