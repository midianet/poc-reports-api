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
public class CargaNovo {
    private Integer nrCarga;
    private Integer cdOrigem;
    private String  descrOrigem;
    private Integer cdTipoCarga;
    private String  descrTipoCarga;
    private Integer cdConservacao;
    private String  descrConservacao;
    private Double  tempMinimo;
    private Double  tempMaximo;
    private Integer cdFrota;
    private String  placaCarreta;
    private String  nomeTransportador;
    private Double  capacPallet;
    private Double  capacPeso;
    private String  embarqueRetira;
    private String  tipoCarregamento;
    private Integer seqColeta;
    private Integer totalColetas;
    private Integer container;
    private Double  payloadContainer;
    private String  nomeCliente;
    private String  paisDestino;
    private Integer planoEmbarque;
    private List<OrdensNovo> ordensNovo;

}
