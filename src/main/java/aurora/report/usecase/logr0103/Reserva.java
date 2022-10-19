package aurora.report.usecase.logr0103;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {
    private Integer id;
    private Integer itmordpickId;
    private Integer cdPallet;
    private Integer cdLote;
    private Double  shelfLote;
    private Integer cdLocfisdepo;
    private Double  qtdeEmbItemRs;
    private Double  qtdeItemRes;
    private Integer obrig;
    private Integer ordemLocfisdepo;
    private Integer ordemPallet;
    private Integer cdTpsitlestv;


}
