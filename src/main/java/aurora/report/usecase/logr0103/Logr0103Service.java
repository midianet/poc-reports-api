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



    private Logr0103Model buildData(@NonNull final Integer cargaId,
                                    @NonNull final Integer itemOrdPickId) {
        //atribiu a carga os dados da carga
        final var carga = repository.findCarga()
                .orElseThrow(() -> new RuntimeException("Carga inexistente"));

        //atribui a lista de ordens os itens da ordem
        final var ordens = repository.listOrdem(cargaId);

        ordens.forEach(ordem -> {
            ordem.setItens(repository.listItemOrdem(ordem.getCdOrdempick()));
            ordem.getItens().forEach(item -> item.setReservas(repository.listReserva(itemOrdPickId)) );
            ordem.setPedidos(repository.listPedFatur(ordem.getCdOrdempick()));
        } );




        return Logr0103Model.builder()
                .carga(carga)
                .ordens(ordens).build();

    }

    public Report buildReport(@NonNull Report.Type type,
                              @NonNull Integer cargaId,
                              @NonNull Integer itemOrdPickId){
        final var params = new HashMap<String,Object>();
        return Report.builder()
                .body(JasperHelper.makeReport(type, "logr0103", params, List.of(buildData(cargaId, itemOrdPickId))))
                .type(type.getMediaType())
                .filename("logr0103")
                .build();
    }
}