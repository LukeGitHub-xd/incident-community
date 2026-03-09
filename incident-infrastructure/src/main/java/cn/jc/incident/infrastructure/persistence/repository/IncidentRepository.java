package cn.jc.incident.infrastructure.persistence.repository;

import cn.jc.incident.infrastructure.persistence.entity.IncidentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentRepository extends JpaRepository<IncidentEntity, String> {

}