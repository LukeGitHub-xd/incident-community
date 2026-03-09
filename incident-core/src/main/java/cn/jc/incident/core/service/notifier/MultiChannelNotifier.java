package cn.jc.incident.core.service.notifier;

import cn.jc.incident.core.model.IncidentInsight;

public interface MultiChannelNotifier {

    /**
     * 通知
     *
     * @param insight 洞察力
     */
    void notify(IncidentInsight insight);
    void notify(String msg);
}
