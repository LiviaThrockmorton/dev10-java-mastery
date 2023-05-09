package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;

import java.util.List;

public interface HostRepository {
    List<Host> findAll();
    Host findById(String id);
}