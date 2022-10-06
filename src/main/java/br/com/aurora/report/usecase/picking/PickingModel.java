package br.com.aurora.report.usecase.picking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PickingModel {
    private Integer nrCarga;
    private Integer cdOrigem;
    private String descrOrigem;
    private String cdPicking;
    private String dataCriacao;
    private String dataFechaPik;
    private String cdOrdem;
    private Integer seqCarreg;
    private Integer cdDestino;
    private String descrDestino;
    private String cdPallet;
    private Integer cdCaixa;
    private Integer cdItem;
    private String descItem;
    private String cdLote;
    private String  dtProducao;
    private String  dtVencto;
    private Integer volume;
    private Integer pesoLiquido;
    private Integer pesoBruto;
}

