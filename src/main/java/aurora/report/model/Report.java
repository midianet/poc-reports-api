package aurora.report.model;

import lombok.*;
import org.springframework.http.MediaType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    private Object body;
    private String type;
    private String filename;

    @AllArgsConstructor
    public enum Type {
        PDF(MediaType.APPLICATION_PDF_VALUE),
        XLS(MediaType.APPLICATION_OCTET_STREAM_VALUE),
        CSV(MediaType.APPLICATION_OCTET_STREAM_VALUE),
        JSON(MediaType.APPLICATION_JSON_VALUE);

        @Getter
        private String mediaType;
    }
}