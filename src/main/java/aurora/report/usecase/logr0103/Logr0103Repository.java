package aurora.report.usecase.logr0103;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class Logr0103Repository {

    private final NamedParameterJdbcTemplate jdbc;

    public Optional<Carga> findCarga () {
        return jdbc.query("""
                    select cargsaida_id
                         , cd_carrocveic
                         , cd_locallog_orig
                         , descr_locallog_orig
                         , placa_carrocveic
                         , cd_tpcarsai
                         , descr_tpcarsai
                         , nome_transporta
                         , cd_tpconscarg
                         , descr_tpconscarg
                         , capac_palete
                         , temper_min
                         , temper_max
                         , capac_carga
                         , cd_tpmovprotv
                         , descr_tpmovprotv
                         , embarq_retirada
                         , seq_carga
                         , nro_container
                         , payload
                         , tot_origem
                         , nro_plano_embarq
                         , peso_bruto_carroc
                         , descr_paises
                         , nome_clie_contrato
                         , 1 as id
                    from logr0103_temp
                    where cargsaida_id is not null
                """, Map.of(), rowMapperCarga).stream().findFirst();
    }

    private RowMapper<Carga> rowMapperCarga = (rs, rowNum) ->
            Carga.builder()
                    .cargaId(rs.getInt("cargsaida_id"))
                    .cdCarrocveic(rs.getInt("cd_carrocveic"))
                    .cdLocallogOrig(rs.getInt("cd_locallog_orig"))
                    .descrLocallogOrig(rs.getString("descr_locallog_orig"))
                    .placaCarrocveic(rs.getString("placa_carrocveic"))
                    .cdTpcarsai(rs.getInt("cd_tpcarsai"))
                    .descTpcarsai(rs.getString("descr_tpcarsai"))
                    .nomeTransporta(rs.getString("nome_transporta"))
                    .cdTpconscarg(rs.getInt("cd_tpconscarg"))
                    .descrTpconscarg(rs.getString("descr_tpconscarg"))
                    .capacPalete(rs.getInt("capac_palete"))
                    .temperMin(rs.getInt("temper_min"))
                    .temperMax(rs.getInt("temper_max"))
                    .capacCarga(rs.getInt("capac_carga"))
                    .cdTpmovprotv(rs.getInt("cd_tpmovprotv"))
                    .descrTpmovprotv(rs.getString("descr_tpmovprotv"))
                    .embarqRetirada(rs.getString("embarq_retirada"))
                    .seqCarga(rs.getInt("seq_carga"))
                    .nroContainer(rs.getString("nro_container"))
                    .payload(rs.getInt("payload"))
                    .totOrigem(rs.getInt("tot_origem"))
                    .nroPlanoEmbarq(rs.getString("nro_plano_embarq"))
                    .pesoBrutoCarroc(rs.getInt("peso_bruto_carroc"))
                    .descrPaises(rs.getString("descr_paises"))
                    .nomeClieContrato(rs.getString("nome_clie_contrato"))
                    .id(rs.getInt("id")).build();

    public List<Ordem> listOrdem() {
        return jdbc.query("""
                select tmp.cd_locallog_dest
                     , tmp.descr_locallog_dest
                     , tmp.cd_ordempick
                     , tmp.cd_locallog_desc
                     , tmp.descr_locallog_desc
                     , tmp.cd_barra
                     , 1 as id        
                  from logr0103_temp tmp
                  where tmp.cd_locallog_dest is not null
                   
                """, Map.of(), rowMapperOrdem);
    }
    private RowMapper<Ordem> rowMapperOrdem = (rs, rowNum) ->
            Ordem.builder()
                    .cdLocallogDest(rs.getInt("cd_locallog_dest"))
                    .descrLocallogDest(rs.getString("descr_locallog_dest"))
                    .cdOrdempick(rs.getString("cd_ordempick"))
                    .cdLocallogDesc(rs.getInt("cd_locallog_desc"))
                    .descrLocallogDesc(rs.getString("descr_locallog_desc"))
                    .cdBarra(rs.getString("cd_barra"))
                    .id(rs.getInt("id"))
                    .build();

    public List<ItemOrdem> listItemOrdem(@NonNull final String idOrdem) {
        return jdbc.query("""
                select tmp.cd_ordempick
                     , tmp.itmordpick_id
                     , tmp.cd_item
                     , tmp.descr_item
                     , tmp.shelf_item
                     , tmp.qtde_emb_item
                     , tmp.qtde_item
                     , tmp.contramarca
                     , 1 as id
                  from logr0103_temp tmp
                 where tmp.cd_item is not null
                  and  tmp.cd_ordempick = :idOrdem          
                """, Map.of("idOrdem" , idOrdem), rowMapperItemOrdem);
    }

    private RowMapper<ItemOrdem> rowMapperItemOrdem = (rs, rowNum) ->
            ItemOrdem.builder()
                    .cdOrdempick(rs.getString("cd_ordempick"))
                    .itmordpickId(rs.getInt("itmordpick_id"))
                    .cdItem(rs.getInt("cd_item"))
                    .descrItem(rs.getString("descr_item"))
                    .shefItem(rs.getDouble("shelf_item"))
                    .qtdeItem(rs.getDouble("qtde_item"))
                    .qtdeEmbItem(rs.getDouble("qtde_emb_item"))
                    .contraMarca(rs.getString("contramarca"))
                    .id(rs.getInt("id")).build();



    public List<PedFatur> listPedFatur(@NonNull final String idOrdem) {
        return jdbc.query("""
                select tmp.cd_ordempick
                     , tmp.nro_ped_fatur
                     , tmp.obs_ped_fatur
                     , tmp.nome_clie
                     , tmp.cidade_clie
                     , tmp.uf_clie
                     , 1 as id
                  from logr0103_temp tmp
                 where tmp.nro_ped_fatur is not null
                   and tmp.cd_ordempick = :idOrdem
                """, Map.of("idOrdem",idOrdem), rowMapperPedFatur);
    }

    private RowMapper<PedFatur> rowMapperPedFatur = (rs, rowNum) ->
            PedFatur.builder()
                    .cdOrdempick(rs.getString("cd_ordempick"))
                    .nroPedFatur(rs.getInt("nro_ped_fatur"))
                    .obsPedFatur(rs.getString("obs_ped_fatur"))
                    .nomeClie(rs.getString("nome_clie"))
                    .cidadeClie(rs.getString("cidade_clie"))
                    .ufClie(rs.getString("uf_clie"))
                    .id(rs.getInt("id"))
                    .build();

    public List<Reserva> listReserva(@NonNull final Integer itemOrdPickId) {
        return jdbc.query("""
                select tmp.itmordpick_id 
                        , tmp.cd_pallet
                        , tmp.cd_lote
                        , tmp.shelf_lote
                        , tmp.cd_locfisdepo
                        , tmp.qtde_emb_item_res
                        , tmp.qtde_item_res
                        , tmp.obrig 
                        , tmp.ordem_locfisdepo
                        , tmp.ordem_pallet
                        , tmp.cd_tpsitlestv 
                        , 1 as id
                     from logr0103_temp tmp
                    where tmp.cd_locfisdepo is not null
                      and tmp.itmordpick_id = :itemOrdPickId
                 order by  tmp.ordem_locfisdepo   desc
                            ,  tmp.ordem_pallet   desc
                            ,  tmp.shelf_lote     desc
                            ,  tmp.cd_locfisdepo
                            ,  tmp.cd_pallet      
                """, Map.of("itemOrdPickId", itemOrdPickId), rowMapperReserva);
    }

    private RowMapper<Reserva> rowMapperReserva = (rs, rowNum) ->
            Reserva.builder()
                    .itmordpickId(rs.getInt("itmordpick_id"))
                    .cdPallet(rs.getInt("cd_pallet"))
                    .cdLote(rs.getInt("cd_lote"))
                    .shelfLote(rs.getDouble("shelf_lote"))
                    .cdLocfisdepo(rs.getInt("cd_locfisdepo"))
                    .qtdeEmbItemRs(rs.getDouble("qtde_emb_item_res"))
                    .qtdeItemRes(rs.getDouble("qtde_item_res"))
                    .obrig(rs.getInt("obrig"))
                    .ordemLocfisdepo(rs.getInt("ordem_locfisdepo"))
                    .ordemPallet(rs.getInt("ordem_pallet"))
                    .cdTpsitlestv(rs.getInt("cd_tpsitlestv"))
                    .id(rs.getInt("id"))
                    .build();

}

