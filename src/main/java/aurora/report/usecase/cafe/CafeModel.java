package aurora.report.usecase.cafe;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CafeModel {
    private String nome;
    private String cargo;
    private Integer quantidade;
}