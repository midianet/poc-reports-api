package br.com.aurora.report.model;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ReportPicking {
    private Integer nr_carga;
    private Integer cd_origem;
    private String  descr_origem;
    private String  cd_picking;
    private String  data_criacao;
    private String  data_fecha_pik;
    private String  cd_ordem;
    private Integer seq_carreg;
    private Integer cd_destino;
    private String  descr_destino;
    private String cd_pallet;
    private Integer cd_caixa;
    private Integer cd_item;
    private String  desc_item;
    private String cd_lote;
    private String  dt_producao;
    private String  dt_vencto;
    private Integer volume;
    private Integer peso_liquido;
    private Integer peso_bruto;


}

