package cn.jc.incident.core.service.impl.notifier;

import cn.jc.incident.core.model.IncidentInsight;
import cn.jc.incident.core.service.notifier.MultiChannelNotifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationDispatcher {

    private final List<MultiChannelNotifier> notifiers;

    public void dispatch(IncidentInsight insight) {
        for (MultiChannelNotifier notifier : notifiers) {
            log.info("Dispatching incident to {}", notifier.getClass().getSimpleName());
            notifier.notify(insight);
        }
    }
}
