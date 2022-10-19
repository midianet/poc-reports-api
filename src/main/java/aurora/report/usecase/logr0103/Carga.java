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
public class Carga {
    private Integer cargaId;
    private Integer cdCarrocveic;
    private Integer cdLocallogOrig;
    private String  descrLocallogOrig;
    private String  placaCarrocveic;
    private Integer cdTpcarsai;
    private String  descTpcarsai;
    private String  nomeTransporta;
    private Integer cdTpconscarg;
    private String  descrTpconscarg;
    private Integer capacPalete;
    private Integer temperMin;
    private Integer temperMax;
    private Integer capacCarga;
    private Integer cdTpmovprotv;
    private String  descrTpmovprotv;
    private String  embarqRetirada;
    private Integer seqCarga;
    private Integer totOrigem;
    private String  nroContainer;
    private Integer payload;
    private String  nroPlanoEmbarq;
    private Integer pesoBrutoCarroc;
    private String  descrPaises;
    private String  nomeClieContrato;
    private List<Ordem> ordens;

}