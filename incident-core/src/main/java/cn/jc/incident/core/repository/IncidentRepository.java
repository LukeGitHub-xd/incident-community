package cn.jc.incident.core.repository;

import cn.jc.incident.core.model.Incident;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentRepository {

    Incident save(Incident incident);

    Optional<Incident> findById(String id);

    List<Incident> findAll();
}