package aurora.report.usecase.picking;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PickingRepository {
    private final NamedParameterJdbcTemplate jdbc;

    private RowMapper<PickingModel> rowMapper = (rs, rowNum) ->
            PickingModel.builder()
                    .nrCarga(rs.getInt("nr_carga"))
                    .cdOrigem(rs.getInt("cd_origem"))
                    .descrOrigem(rs.getString("descr_origem"))
                    .cdPicking(rs.getString("cd_picking"))
                    .dataCriacao(rs.getString("dt_criacao_pik_"))
                    .dataFechaPik(rs.getString("dt_fecha_pik"))
                    .cdOrdem(rs.getString("cd_ordem"))
                    .seqCarreg(rs.getInt("seq_carreg"))
                    .cdDestino(rs.getInt("cd_destino"))
                    .descrDestino(rs.getString("descr_destino"))
                    .cdPallet(rs.getString("cd_pallet"))
                    .cdCaixa(rs.getInt("cd_caixa"))
                    .cdItem(rs.getInt("cd_item"))
                    .descItem(rs.getString("descr_item"))
                    .cdLote(rs.getString("cd_lote"))
                    .dtProducao(rs.getString("dt_producao"))
                    .dtVencto(rs.getString("dt_vencto"))
                    .volume(rs.getInt("volume"))
                    .pesoLiquido(rs.getInt("peso_liquido"))
                    .pesoBruto(rs.getInt("peso_bruto"))
                    .build();

    public List<PickingModel> list(Integer nrCarga, Integer unidOrigem) {
        return jdbc.query("""
                     select csa.nro_carga     as nr_carga
                     , llo.cd            as cd_origem
                     , llo.descr         as descr_origem
                     , pik.cd            as cd_picking
                     , to_char(pik.dt_criacao,'dd/mm/yyyy hh24:mi:ss')
                                         as dt_criacao_pik_
                     , to_char(pik.dt_fechamento,'dd/mm/yyyy hh24:mi:ss')
                                         as dt_fecha_pik
                     , opk.cd            as cd_ordem
                     , opk.seq_carreg    as seq_carreg
                     , lld.cd            as cd_destino
                     , lld.descr         as descr_destino
                     , to_char(mop.volume_secundario)
                                         as cd_pallet
                     , to_char(decode(mop.volume_primario,mop.volume_secundario,null))
                                         as cd_caixa
                     , mop.cd_item       as cd_item
                     , itm.descr         as descr_item
                     , mop.cd_lote
                     , to_char(mop.dt_producao,'dd/mm/yyyy') as dt_producao
                     , to_char(mop.dt_vencto,'dd/mm/yyyy')   as dt_vencto
                     , mop.volume
                     , mop.peso_liquido
                     , mop.peso_bruto
                  from carga_saida csa
                     , origem_carga_saida ocs
                     , local_log llo
                     , local_log lld
                     , picking_origcarga_saida pos
                     , picking pik
                     , ordem_picking opk
                     , movto_ordem_picking mop
                     , item itm
                 where 1 = 1
                   -- carga saida
                   and csa.nro_carga     = :nrCarga  -- numero da carga 10619558
                   -- origem carga
                   and ocs.cargsaida_id  = csa.cargsaida_id
                   and llo.locallog_id   = ocs.locallog_id
                   and llo.cd            = :unidOrigem         -- local de origem 87
                   -- picking
                   and pos.origcarsai_id = ocs.origcarsai_id
                   and pik.picking_id    = pos.picking_id
                   -- ordem picking
                   and opk.picking_id    = pik.picking_id
                   -- destino ordem
                   and lld.locallog_id   = opk.locallog_id
                   -- movimento da ordem
                   and mop.ordempick_id  = opk.ordempick_id
                   -- item
                   and itm.item_id       = mop.item_id
                 order by
                       opk.ordempick_id
                     , mop.volume_secundario
                     , mop.cd_item
                     , mop.cd_lote""", Map.of("nrCarga",nrCarga, "unidOrigem", unidOrigem), rowMapper);
    }
}
