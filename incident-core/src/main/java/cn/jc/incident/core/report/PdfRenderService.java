package cn.jc.incident.core.report;

public interface PdfRenderService {

    byte[] renderPdf(String html);
}
