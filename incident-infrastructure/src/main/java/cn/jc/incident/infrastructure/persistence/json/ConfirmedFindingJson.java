package cn.jc.incident.infrastructure.persistence.json;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmedFindingJson {

    private String description;
    private List<String> evidence;
}