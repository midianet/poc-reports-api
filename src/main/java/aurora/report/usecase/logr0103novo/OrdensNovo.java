package aurora.report.usecase.logr0103novo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdensNovo {
    private Integer pickingId;
    private Integer ordemPickingId;
    private String  ordemPicking;
    private Integer seqCarregamento;
    private String  cdLocalDescarga;
    private String  descrLocalDescarga;
    private List<PedidosNovo> pedidosNovo;
}
