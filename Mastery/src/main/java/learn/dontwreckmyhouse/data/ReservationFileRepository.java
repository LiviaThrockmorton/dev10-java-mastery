package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ReservationFileRepository implements ReservationRepository {

    private static final String HEADER = "id,start_date,end_date,guest_id,total";
    private final String directory;

    public ReservationFileRepository(@Value("${ReservationDataDirectory:./data/reservations}") String directory) {
        this.directory = directory;
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        return null;
    }

    @Override
    public boolean update(Reservation reservation) throws DataException {
        return false;
    }

    @Override
    public boolean cancel(String hostId, String reservationId) throws DataException {
        return false;
    }

    private String getFilePath(String hostId) {
        return Paths.get(directory, hostId + ".csv").toString();
    }

    @Override
    public List<Reservation> findByHost(String hostId) {
        ArrayList<Reservation> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(hostId)))) {
            reader.readLine();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] fields = line.split(",", -1);
                if (fields.length == 5) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {//don't throw
        }
        return result;
    }

    private Reservation deserialize(String[] fields) {
        Reservation result = new Reservation();
        result.setReservationId(fields[0]);
        result.setStart(LocalDate.parse(fields[1]));
        result.setEnd(LocalDate.parse(fields[2]));
        result.setGuestId(fields[3]);//if this doesn't work, the lines below might
        result.setTotal(BigDecimal.valueOf(Integer.parseInt(fields[4])));
//        Guest guest = new Guest();
//        guest.setGuestId(Integer.parseInt(fields[0]));
//        result.setGuestId(guest.getGuestId);
        return result;
    }
}
