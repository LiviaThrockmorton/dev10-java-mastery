package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GuestFileRepository implements GuestRepository {

    private final String filePath;

    public GuestFileRepository(@Value("${HostDataFilePath:./data/guests.csv}") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Guest> findAll() {
        ArrayList<Guest> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] fields = line.split(",", -1);
                if (fields.length == 6) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {//don't throw
        }
        return result;
    }

    @Override
    public Guest findById(int id) {
        return findAll().stream().filter(
                i -> i.getGuestId() == id).findFirst().orElse(null);
    }

    private Guest deserialize(String[] fields) {
        Guest result = new Guest();
        result.setGuestId(Integer.parseInt(fields[0]));
        result.setGuestFirstName(fields[1]);
        result.setGuestLastName(fields[2]);
        result.setGuestEmail(fields[3]);
        result.setGuestPhone(fields[4]);
        result.setGuestState(fields[5]);
        return result;
    }
}