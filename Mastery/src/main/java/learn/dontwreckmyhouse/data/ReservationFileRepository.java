package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationFileRepository implements ReservationRepository {

    private static final String HEADER = "id,start_date,end_date,guest_id,total";
    private final String directory;
    private String getFilePath(String hostId) {
        return Paths.get(directory, hostId + ".csv").toString();
    }

    public ReservationFileRepository(@Value("${ReservationDataDirectory:./data/reservations}") String directory) {
        this.directory = directory;
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        List<Reservation> hostReservations = findByHost(reservation.getHostId());
        int nextId = getNextId(hostReservations);
        reservation.setReservationId(nextId);
        hostReservations.add(reservation);
        writeToFile(hostReservations, reservation.getHostId());
        return reservation;
    }

    @Override
    public Reservation update(Reservation reservation) throws DataException {
        List<Reservation> reservations = findByHost(reservation.getHostId());
        for (int i = 0; i < reservations.size(); i++) {
            if ((reservations.get(i).getReservationId() == reservation.getReservationId())
                && (reservations.get(i).getGuestId() == reservation.getGuestId())) {
                reservations.set(i, reservation);
                writeToFile(reservations, reservation.getHostId());
                return reservation;
            }
        }
        return null;
    }

    @Override
    public boolean cancel(String hostId, String reservationId) throws DataException {
        return false;
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

    @Override
    public Reservation findById(int reservationId, String hostId) throws DataException {
        return findByHost(hostId).stream().filter(
                i -> i.getReservationId() == reservationId)
                .findFirst().orElse(null);
    }

    private int getNextId(List<Reservation> reservations) {
        int maxId = 0;
        for (Reservation reservation : reservations) {
            if (maxId < reservation.getReservationId()) {
                maxId = reservation.getReservationId();
            }
        }
        return maxId + 1;
    }

    private void writeToFile(List<Reservation> reservations, String hostId) throws DataException {
        try (PrintWriter writer = new PrintWriter(getFilePath(hostId))) {
            writer.println(HEADER);
            for (Reservation reservation : reservations) {
                writer.println(serialize(reservation));
            }
        } catch (IOException ex) {
            throw new DataException(ex);
        }
    }

    private String serialize(Reservation reservation) {
        return String.format("%s,%s,%s,%s,%s",
                reservation.getReservationId(),
                reservation.getStart(),
                reservation.getEnd(),
                reservation.getGuestId(),
                reservation.getTotal());
    }

    private Reservation deserialize(String[] fields) {
        Reservation result = new Reservation();
        result.setReservationId(Integer.parseInt(fields[0]));
        result.setStart(LocalDate.parse(fields[1]));
        result.setEnd(LocalDate.parse(fields[2]));
        result.setGuestId(Integer.parseInt(fields[3]));
        result.setTotal(new BigDecimal(fields[4]));
        //result.setTotal(BigDecimal.valueOf(Integer.parseInt(fields[4])));
        return result;
    }
}