package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HostFileRepository implements HostRepository {

    private final String filePath;

    public HostFileRepository(@Value("${HostDataFilePath:./data/hosts.csv}") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Host> findAll() {
        ArrayList<Host> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] fields = line.split(",", -1);
                if (fields.length == 10) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {//don't throw
        }
        return result;
    }

    @Override
    public Host findById(String id) {
        return findAll().stream().filter(
                i -> i.getHostId().equals(id)).findFirst().orElse(null);
    }

    private Host deserialize(String[] fields) {
        Host result = new Host();
        result.setHostId(fields[0]);
        result.setHostLastName(fields[1]);
        result.setHostEmail(fields[2]);
        result.setHostPhone(fields[3]);
        result.setHostAddress(fields[4]);
        result.setHostCity(fields[5]);
        result.setHostState(fields[6]);
        result.setHostPostalCode(Integer.parseInt(fields[7]));
        result.setStandardRate(BigDecimal.valueOf(Double.parseDouble(fields[8])));
        result.setWeekendRate(BigDecimal.valueOf(Double.parseDouble(fields[9])));
        return result;
    }
}