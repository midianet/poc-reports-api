package aurora.report.usecase.logr0103novo;

import lombok.*;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class Logr0103NovoRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public Optional<CargaNovo> findCargaNew () {
        return jdbc.query("""
                select pos.picking_id
                     , ocs.cargsaida_id    as nrCarga
                     , llo.cd              as cdOrigem
                     , llo.descr           as descrOrigem
                     , tcs.cd              as cdTipoCarga
                     , tcs.descr           as descrTipoCarga
                     , tcc.cd              as cdConservacao
                     , tcc.descr           as descrConservacao
                     , tcc.temperatura_min as tempMinima
                     , tcc.temperatura_max as tempMaxima
                     , crv.cd              as cdFrota
                     , crv.placa           as placaCarreta
                     , pes.nome            as nomeTransportador
                     , pca.capac           as capacPallet
                     , ica.peso_bruto + itr.peso_bruto
                                           as capacPeso
                     , crc.rv_meaning      as embarqueRetira
                     , (select listagg (distinct tmp.descr, ',') within group (order by tmp.descr)
                          from ordem_picking opk
                             , ordem_picking_ped_fatur opf
                             , ped_fatur pfa
                             , tipo_movto_produto_tv tmp
                         where opk.picking_id    = pos.picking_id
                           and opf.ordempick_id  = opk.ordempick_id     
                           and pfa.pedfatur_id   = opf.pedfatur_id
                           and tmp.tpmovprotv_id = pfa.tpmovprotv_id
                           and not exists        ( select 1
                                                     from ped_fatur_canc pfc
                                                        , motiv_canc_docto_tv mcd
                                                    where pfc.pedfatur_id   = pfa.pedfatur_id
                                                      and mcd.mocandoctv_id = pfc.mocandoctv_id
                                                      and mcd.cd           != 30
                                                    fetch first row only
                                                 )           
                       )                   as tipoCarregamento
                     , ocs.seq_carga       as seqColeta
                     , (select count(*)
                          from origem_carga_saida oct
                         where oct.cargsaida_id = csa.cargsaida_id
                       )                   as totalColetas
                     , exp.nr_container    as Container
                     , exp.payload         as payloadContainer
                     , exp.nm_cliente      as nomeCliente
                     , exp.descr_pais      as paisDestino
                     , exp.nr_plano        as PlanoEmbarque
                  from picking_origcarga_saida pos
                     , origem_carga_saida ocs
                     , local_log llo
                     , carga_saida csa
                     , tipo_conserv_carga tcc    
                     , carga_saida_grade csg
                     , tipo_carga_saida tcs
                     , camara_veiculo cmv
                     , carroc_veiculo crv
                     , tracio_veiculo tve
                     , transportador tpd
                     , pessoa pes
                     , ident_carroc ica
                     , ident_tracio itr
                     , porte_carroc pca
                     , cg_ref_codes crc
                     -- dados de exportação
                     , (select op1.picking_id
                             , rce.nro     as nr_container
                             , rce.payload
                             , pes.nome    as nm_cliente
                             , pai.descr   as descr_pais
                             -- uma carga pode ter mais de um plano de embarque
                             , listagg(ple.nro,', ') within group (order by ple.nro)
                                           as nr_plano
                          from ordem_picking op1
                             , ordem_picking_ped_fatur of1
                             , ped_fatur pf1
                             , r_montcarexp_pedfatur rmp
                             , montagem_carga_exp mon
                             , carreg_exp car
                             , plano_embarq_exp ple
                             , plano_embarq_programa_exp pep
                             , programa_exp prg
                             , ordem_venda_exp ove
                             , paises pai
                             , contrato_exp coe
                             , clie cli
                             , pessoa pes
                             , recip_carga_exp rce
                         where of1.ordempick_id  = op1.ordempick_id
                           and pf1.pedfatur_id   = of1.pedfatur_id
                           and rmp.pedfatur_id   = pf1.pedfatur_id
                           and mon.montcarexp_id = rmp.montcarexp_id
                           and car.carregexp_id  = mon.carregexp_id
                           and ple.planembexp_id = car.planembexp_id
                           and ple.canc          = 0
                           and pep.planembexp_id = ple.planembexp_id
                           and pep.canc          = 0
                           and prg.programaex_id = pep.programaex_id
                           and prg.canc          = 0
                           and ove.ordvendexp_id = prg.ordvendexp_id
                           and pai.paises_id     = ove.paises_id
                           and coe.contratexp_id = ove.contratexp_id          
                           and cli.clie_id       = coe.clie_id
                           and pes.pessoa_id     = cli.pessoa_id
                           and rce.nro           = pk_exp_planembexp.fkg_nro_recp (ple.planembexp_id)
                         group by
                               op1.picking_id
                             , rce.nro
                             , rce.payload
                             , pes.nome
                             , pai.descr
                       ) exp
                 where 1 = 1
                   -- picking não cancelado
                   and pos.picking_id       =  1078725 -- PARAMETRO
                   and not exists           ( select 1
                                                from picking_canc pic
                                               where pic.picking_id = pos.picking_id
                                            )
                   -- origem carga saida
                   and ocs.origcarsai_id    = pos.origcarsai_id
                   -- origem
                   and llo.locallog_id      = ocs.locallog_id
                   -- carga
                   and csa.cargsaida_id     = ocs.cargsaida_id
                   -- grade
                   and csg.cargsaida_id     = ocs.cargsaida_id
                   -- tipo conservação
                   and tcc.tpconscarg_id    = csg.tpconscarg_id
                   -- tipo carga
                   and tcs.tpcarsai_id      = csg.tpcarsai_id
                   -- camara
                   and cmv.camaveicul_id(+) = csa.camaveicul_id
                   -- carroceria
                   and crv.carrocveic_id(+) = cmv.carrocveic_id
                   -- tracionador
                   and tve.tracioveic_id(+) = cmv.tracioveic_id
                   -- transportador
                   and tpd.transporta_id(+) = csa.transporta_id
                   and pes.pessoa_id(+)     = tpd.pessoa_id
                   -- capacidade
                   and ica.idencarroc_id(+) = crv.idencarroc_id
                   and pca.portcarroc_id(+) = ica.portcarroc_id
                   and itr.identracio_id(+) = tve.identracio_id
                   -- embarque/retira
                   and crc.rv_domain        = 'CARGA_SAIDA.EMBARQ_RETIRADA'
                   and crc.rv_low_value     = csa.embarq_retirada
                   -- exportação
                   and exp.picking_id(+)    = pos.picking_id
                """, Map.of(), rowMapperCarganew).stream().findFirst();
    }

    private RowMapper<CargaNovo> rowMapperCarganew = (rs, rowNum) ->
        CargaNovo.builder()
                .nrCarga(rs.getInt("nrCarga"))
                .cdOrigem(rs.getInt("cdOrigem"))
                .descrOrigem(rs.getString("descrOrigem"))
                .cdTipoCarga(rs.getInt("cdTipoCarga"))
                .descrTipoCarga(rs.getString("descrTipoCarga"))
                .cdConservacao(rs.getInt("cdConservacao"))
                .descrConservacao("descrConservacao")
                .tempMinimo(rs.getDouble("tempMinima"))
                .tempMaximo(rs.getDouble("tempMaxima"))
                .cdFrota(rs.getInt("cdFrota"))
                .placaCarreta(rs.getString("placaCarreta"))
                .nomeTransportador(rs.getString("nomeTransportador"))
                .capacPallet(rs.getDouble("capacPallet"))
                .capacPeso(rs.getDouble("capacPeso"))
                .embarqueRetira(rs.getString("embarqueRetira"))
                .tipoCarregamento(rs.getString("tipoCarregamento"))
                .seqColeta(rs.getInt("seqColeta"))
                .totalColetas(rs.getInt("totalColetas"))
                .container(rs.getInt("Container"))
                .payloadContainer(rs.getDouble("payloadContainer"))
                .nomeCliente(rs.getString("nomeCliente"))
                .paisDestino(rs.getString("paisDestino"))
                .planoEmbarque(rs.getInt("planoEmbarque"))
                .build();

    public List<OrdensNovo> listOrdensNew() {
        return jdbc.query("""
                select opk.picking_id
                     , opk.ordempick_id
                     , opk.cd         as ordemPicking
                     , opk.seq_carreg as seqCarregamento
                     , listagg(distinct lgd.cd,', ')    within group (order by lgd.cd)
                                      as cdLocalDescarga
                     , listagg(distinct lgd.descr,', ') within group (order by lgd.cd)
                                      as descrLocalDescarga
                  from ordem_picking opk
                     , ordem_picking_ped_fatur opf
                     , ped_fatur pfa
                     , local_log lgd
                 where opk.picking_id   = 1078725 -- PARAMETRO
                   and opf.ordempick_id = opk.ordempick_id
                   and pfa.pedfatur_id  = opf.pedfatur_id
                   and not exists       ( select 1
                                            from ped_fatur_canc pfc
                                               , motiv_canc_docto_tv mcd
                                           where pfc.pedfatur_id = pfa.pedfatur_id
                                             and mcd.mocandoctv_id = pfc.mocandoctv_id
                                             and mcd.cd           != 30
                                        )
                   and lgd.locallog_id  = pfa.locallog_id_descarreg
                 group by
                       opk.picking_id
                     , opk.ordempick_id
                     , opk.cd
                     , opk.seq_carreg
                 order by
                       opk.seq_carreg
                     , opk.ordempick_id
                """, Map.of(), rowMapperOrdensNew);

    }

    private RowMapper<OrdensNovo> rowMapperOrdensNew = (rs, rowNum) ->
            OrdensNovo.builder()
                    .pickingId(rs.getInt("picking_id"))
                    .ordemPickingId(rs.getInt("ordempick_id"))
                    .ordemPicking(rs.getString("ordemPicking"))
                    .seqCarregamento(rs.getInt("seqCarregamento"))
                    .cdLocalDescarga(rs.getString("cdLocalDescarga"))
                    .descrLocalDescarga(rs.getString("descrLocalDescarga"))
                    .build();

    public List<PedidosNovo> listPedidosNew() {
        return jdbc.query("""
                select opk.picking_id
                     , opk.ordempick_id
                     , pfa.nro_ped_fatur  as nrPedFatur
                     , pes.nome           as nomeCliente
                     , cid.descr          as nomeCidade
                     , est.descr          as nomeEstado
                     , pfa.obs            as observacao
                  from ordem_picking opk
                     , ordem_picking_ped_fatur opf
                     , ped_fatur pfa
                     , pessoa pes
                     , ender edr
                     , cidade cid
                     , estado est
                 where opk.picking_id   = 1078725 -- PARAMETRO
                   and opf.ordempick_id = opk.ordempick_id
                   and pfa.pedfatur_id  = opf.pedfatur_id
                   and pes.pessoa_id    = pfa.pessoa_id
                   and edr.ender_id     = pfa.ender_id_dest
                   and cid.cidade_id    = edr.cidade_id
                   and est.estado_id    = cid.estado_id
                 order by
                       opk.ordempick_id
                     , pfa.nro_ped_fatur
                                
                """, Map.of(), rowMapperPedidosNew);
    }

    private RowMapper<PedidosNovo> rowMapperPedidosNew = (rs, rowNum) ->
            PedidosNovo.builder()
                    .pickingId(rs.getInt("picking_id"))
                    .ordemPickingId(rs.getInt("ordempicking_id"))
                    .nrPedFatur(rs.getInt("nrPedFatur"))
                    .nomeCidade(rs.getString("nomeCliente"))
                    .nomeCidade(rs.getString("nomeCidade"))
                    .nomeEstado(rs.getString("nomeEstado"))
                    .observacao(rs.getString("observacao"))
                    .build();

    public List<ReservaNovo> listReservaNew() {
        return jdbc.query("""
                select opk.picking_id
                     , opk.ordempick_id
                     , itm.cd        as codItem
                     , itm.descr     as descrItem
                     -- contramarca, somente para itens de exportação
                     , (select listagg(distinct ice.cd_item_clie,' ,') within group (order by ice.cd_item_clie)
                          from ordem_picking_ped_fatur opf
                             , ped_fatur pfa
                             , item_ped_fatur ipf
                             , embalagem_item eic
                             , r_prgitcarex_itpedfatur rpi
                             , programa_item_carreg_exp  pic
                             , r_itecarrexp_iteprogexp rii
                             , item_programa_exp ipe
                             , item_ordem_venda_exp iov
                             , item_contrato_exp ice
                         where opf.ordempick_id = opk.ordempick_id
                           and pfa.pedfatur_id  = opf.pedfatur_id
                           and ipf.pedfatur_id  = pfa.pedfatur_id
                           and eic.item_id      = itm.item_id
                           and eic.primaria     = 1
                           and ipf.embalitem_id = eic.embalitem_id
                           and rpi.itpedfatur_id= ipf.itpedfatur_id
                           and pic.prgitcarex_id= rpi.prgitcarex_id
                           and rii.itecarrexp_id= pic.itecarrexp_id
                           and ipe.iteprogexp_id= rii.iteprogexp_id
                           and iov.itorvenexp_id= ipe.itorvenexp_id
                           and ice.itcontrexp_id= iov.itcontrexp_id
                       )             as contramarca
                     , iop.shelf     as shelfLife
                     , iop.qtde      as qtdeEmbalagem
                     , trunc(iop.qtde * eit.qtde_unid_medida)
                                     as qtdeItem
                     , lfd.cd        as localFisico
                     , plt.cd        as cdPallet
                     , lot.cd        as cdLote
                     , pk_est_lote.FKG_SHELF_LIFE(ed_dt_referencia => sysdate, en_lote_id => lot.lote_id)
                                     as shelfLifeReserva
                     , trunc(pfr.qtde / eit.qtde_unid_medida)
                                     as qtdeEmbalagemReserva
                     , pfr.qtde      as qtdeItemReserva
                  from ordem_picking opk
                     , item_ordem_picking iop
                     , item itm
                     , embalagem_item eit
                     , reserva_picking rpk
                     , reserva_estoque res
                     , posicao_fisica_reserva_estoque pfr
                     , pallet plt
                     , lote_estoque les
                     , lote lot
                     , saldo_posicao_fisica_depo spd
                     , posicao_local_fisico_depo plf
                     , local_fisico_depo lfd
                 where opk.picking_id       = 1078725 -- PARAMETRO
                   and iop.ordempick_id     = opk.ordempick_id
                   and itm.item_id          = iop.item_id
                   and eit.item_id          = itm.item_id
                   and eit.primaria         = 1
                   and rpk.itmordpick_id(+) = iop.itmordpick_id
                   and res.resestoq_id(+)   = rpk.resestoq_id
                   and pfr.resestoq_id(+)   = res.resestoq_id
                   and plt.pallet_id(+)     = pfr.pallet_id
                   and les.lotestoque_id(+) = pfr.lotestoque_id
                   and lot.lote_id(+)       = les.lote_id
                   and spd.salpofisde_id(+) = pfr.salpofisde_id
                   and plf.polfisdepo_id(+) = spd.polfisdepo_id
                   and lfd.locfisdepo_id(+) = plf.locfisdepo_id
                 order by
                       opk.seq_carreg
                     , opk.ordempick_id
                     , iop.seq
                     , shelfLifeReserva desc
                                
                """, Map.of(), rowMapperReservaNew);
    }

    private RowMapper<ReservaNovo> rowMapperReservaNew = (rs, rowNum) ->
            ReservaNovo.builder()
                    .pickingId(rs.getInt("picking_id"))
                    .ordemPickingId(rs.getInt("ordempick_id"))
                    .codItem(rs.getInt("cod_item"))
                    .descrItem(rs.getString("descrItem"))
                    .contraMarca(rs.getString("contramarca"))
                    .shelfLife(rs.getDouble("shelfLife"))
                    .qtdeEmbalagem(rs.getDouble("qtdeEmbalagem"))
                    .qtdeItem(rs.getDouble("qtdeItem"))
                    .localFisico(rs.getInt("localFisico"))
                    .cdPallet(rs.getInt("cdPallet"))
                    .cdLote(rs.getInt("cdLote"))
                    .shelfLifeReserva(rs.getDouble("shelfLifeReserva"))
                    .qtdeEmbalagemReserva(rs.getDouble("qtdeEmbalagemReserva"))
                    .qtdeItemReserva(rs.getDouble("qtdeItemReserva"))
                    .build();

}
