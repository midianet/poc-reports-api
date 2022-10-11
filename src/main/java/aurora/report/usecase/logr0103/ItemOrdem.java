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
public class ItemOrdem {
    private Integer cdOrdempick;
    private Integer itmordpickId;
    private Integer cdItem;
    private String  descrItem;
    private Double  shefItem;
    private Double  qtdeEmbItem;
    private Double  qtdeItem;
    private String  contraMarca;
    private List<Reserva> reservas;
}
