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
public class PedidosNovo {
    private Integer pickingId;
    private Integer ordemPickingId;
    private Integer nrPedFatur;
    private String  nomeCliente;
    private String  nomeCidade;
    private String  nomeEstado;
    private String  observacao;
    private List<ReservaNovo> reservaNovo;

}
