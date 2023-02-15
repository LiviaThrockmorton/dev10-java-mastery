package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.HostRepository;
import learn.dontwreckmyhouse.models.Host;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HostService {

    private final HostRepository hostRepository;

    public HostService(HostRepository hostRepository) {
        this.hostRepository = hostRepository;
    }

    public List<Host> findByState(String initials) {
        return hostRepository.findAll().stream()
                .filter(i -> i.getHostState().equalsIgnoreCase(initials))
                .collect(Collectors.toList());
    }
}