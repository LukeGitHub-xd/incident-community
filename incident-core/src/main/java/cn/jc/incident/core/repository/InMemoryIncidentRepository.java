package cn.jc.incident.core.repository;

import cn.jc.incident.core.model.Incident;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class InMemoryIncidentRepository implements IncidentRepository {

    private final Map<String, Incident> store = new ConcurrentHashMap<>();

    @Override
    public Incident save(Incident incident) {
        store.put(incident.getId(), incident);
        return incident;
    }

    @Override
    public Optional<Incident> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Incident> findAll() {
        return new ArrayList<>(store.values());
    }
}
