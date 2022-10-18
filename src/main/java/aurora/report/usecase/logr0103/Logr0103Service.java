package aurora.report.usecase.logr0103;

import aurora.report.helper.JasperHelper;
import aurora.report.model.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Logr0103Service {
    private final Logr0103Repository repository;



    private Logr0103Model buildData() {
        //atribiu a carga os dados da carga
        final var carga = repository.findCarga()
                .orElseThrow(() -> new RuntimeException("Carga inexistente"));

        //atribui a lista de ordens os itens da ordem
        carga.setOrdens(repository.listOrdem());

        //monta o json


        carga.getOrdens().forEach(ordem -> {

            ordem.setItens(repository.listItemOrdem(ordem.getCdOrdempick()));

            ordem.getItens()
                    .forEach(item ->
                            item.setReservas(repository.listReserva(item.getItmordpickId())) );

            ordem.setPedidos(repository.listPedFatur(ordem.getCdOrdempick()));
        } );

        return Logr0103Model.builder()
                .carga(carga).build();

    }

    public Report buildReport(@NonNull Report.Type type){
        final var params = new HashMap<String,Object>();
        final var data = buildData();
        final var body =  Report.Type.JSON.equals(type)
                ? data
                : JasperHelper.makeReport(type, "logr0103", params, List.of(data));

        return Report.builder()
                .body(body)
                .type(type.getMediaType())
                .filename("logr0103")
                .build();
    }
}