package aurora.report.usecase.logr0103novo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaNovo {
    private Integer pickingId;
    private Integer ordemPickingId;
    private Integer codItem;
    private String  descrItem;
    private String  contraMarca;
    private Double  shelfLife;
    private Double  qtdeEmbalagem;
    private Double  qtdeItem;
    private Integer localFisico;
    private Integer cdPallet;
    private Integer cdLote;
    private Double  shelfLifeReserva;
    private Double  qtdeEmbalagemReserva;
    private Double  qtdeItemReserva;

}
