package aurora.report.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * Classe que representa o modelo de mensagens padrão da API
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String message;

    @Singular
    private List<Detail> details;

    public List<Detail> getDetails() {
        return ObjectUtils.isEmpty(details) ? null : details;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail {
        private String name;
        private String message;
    }
}
