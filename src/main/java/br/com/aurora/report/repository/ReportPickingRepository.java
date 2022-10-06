package br.com.aurora.report.repository;

import br.com.aurora.report.model.ReportPicking;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportPickingRepository {
    private final NamedParameterJdbcTemplate jdbc;

    private RowMapper<ReportPicking> rowMapper = (rs, rowNum) ->
            ReportPicking.builder()
                    .nr_carga(rs.getInt("nr_carga"))
                    .cd_origem(rs.getInt("cd_origem"))
                    .descr_origem(rs.getString("descr_origem"))
                    .cd_picking(rs.getString("cd_picking"))
                    .data_criacao(rs.getString("dt_criacao_pik_"))
                    .data_fecha_pik(rs.getString("dt_fecha_pik"))
                    .cd_ordem(rs.getString("cd_ordem"))
                    .seq_carreg(rs.getInt("seq_carreg"))
                    .cd_destino(rs.getInt("cd_destino"))
                    .descr_destino(rs.getString("descr_destino"))
                    .cd_pallet(rs.getString("cd_pallet"))
                    .cd_caixa(rs.getInt("cd_caixa"))
                    .cd_item(rs.getInt("cd_item"))
                    .desc_item(rs.getString("descr_item"))
                    .cd_lote(rs.getString("cd_lote"))
                    .dt_producao(rs.getString("dt_producao"))
                    .dt_vencto(rs.getString("dt_vencto"))
                    .volume(rs.getInt("volume"))
                    .peso_liquido(rs.getInt("peso_liquido"))
                    .peso_bruto(rs.getInt("peso_bruto"))
                    .build();

    public List<ReportPicking> listReportPicking(Integer nrCarga, Integer unidOrigem) {
        return jdbc.query("select csa.nro_carga     as nr_carga\n" +
                "     , llo.cd            as cd_origem\n" +
                "     , llo.descr         as descr_origem\n" +
                "     , pik.cd            as cd_picking\n" +
                "     , to_char(pik.dt_criacao,'dd/mm/yyyy hh24:mi:ss')\n" +
                "                         as dt_criacao_pik_\n" +
                "     , to_char(pik.dt_fechamento,'dd/mm/yyyy hh24:mi:ss')\n" +
                "                         as dt_fecha_pik\n" +
                "     , opk.cd            as cd_ordem\n" +
                "     , opk.seq_carreg    as seq_carreg\n" +
                "     , lld.cd            as cd_destino\n" +
                "     , lld.descr         as descr_destino\n" +
                "     , to_char(mop.volume_secundario)\n" +
                "                         as cd_pallet\n" +
                "     , to_char(decode(mop.volume_primario,mop.volume_secundario,null))\n" +
                "                         as cd_caixa\n" +
                "     , mop.cd_item       as cd_item\n" +
                "     , itm.descr         as descr_item\n" +
                "     , mop.cd_lote\n" +
                "     , to_char(mop.dt_producao,'dd/mm/yyyy') as dt_producao\n" +
                "     , to_char(mop.dt_vencto,'dd/mm/yyyy')   as dt_vencto\n" +
                "     , mop.volume\n" +
                "     , mop.peso_liquido\n" +
                "     , mop.peso_bruto\n" +
                "  from carga_saida csa\n" +
                "     , origem_carga_saida ocs\n" +
                "     , local_log llo\n" +
                "     , local_log lld\n" +
                "     , picking_origcarga_saida pos\n" +
                "     , picking pik\n" +
                "     , ordem_picking opk\n" +
                "     , movto_ordem_picking mop\n" +
                "     , item itm\n" +
                " where 1 = 1\n" +
                "   -- carga saida\n" +
                "   and csa.nro_carga     = 10619558  -- numero da carga\n" +
                "   -- origem carga\n" +
                "   and ocs.cargsaida_id  = csa.cargsaida_id\n" +
                "   and llo.locallog_id   = ocs.locallog_id\n" +
                "   and llo.cd            = 87         -- local de origem\n" +
                "   -- picking\n" +
                "   and pos.origcarsai_id = ocs.origcarsai_id\n" +
                "   and pik.picking_id    = pos.picking_id\n" +
                "   -- ordem picking\n" +
                "   and opk.picking_id    = pik.picking_id\n" +
                "   -- destino ordem\n" +
                "   and lld.locallog_id   = opk.locallog_id\n" +
                "   -- movimento da ordem\n" +
                "   and mop.ordempick_id  = opk.ordempick_id\n" +
                "   -- item\n" +
                "   and itm.item_id       = mop.item_id\n" +
                " order by\n" +
                "       opk.ordempick_id\n" +
                "     , mop.volume_secundario\n" +
                "     , mop.cd_item\n" +
                "     , mop.cd_lote\n",rowMapper);
    }
}
