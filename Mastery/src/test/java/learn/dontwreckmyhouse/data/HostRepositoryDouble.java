package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository {
    //creating dummy data
    public final static Host HOST = makeHost();
    private static Host makeHost() {
        Host host = new Host();
        host.setHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        host.setHostLastName("de Clerk");
        host.setHostEmail("kdeclerkdc@sitemeter.com");
        host.setHostPhone("(208) 9496329");
        host.setHostAddress("2 Debra Way");
        host.setHostCity("Boise");
        host.setHostState("ID");
        host.setHostPostalCode(83757);
        host.setStandardRate(BigDecimal.valueOf(200));
        host.setWeekendRate(BigDecimal.valueOf(250));
        return host;
    }
    private final ArrayList<Host> hosts = new ArrayList<>();
    public HostRepositoryDouble() {
        hosts.add(HOST);
    }

    //implementing HostRepository
    @Override
    public List<Host> findAll() {
        return hosts;
    }
}
