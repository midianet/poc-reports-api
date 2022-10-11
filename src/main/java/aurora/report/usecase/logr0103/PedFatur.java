package aurora.report.usecase.logr0103;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedFatur {
    private Integer cdOrdempick;
    private Integer nroPedFatur;
    private String  obsPedFatur;
    private String  nomeClie;
    private String  cidadeClie;
    private String  ufClie;
}
