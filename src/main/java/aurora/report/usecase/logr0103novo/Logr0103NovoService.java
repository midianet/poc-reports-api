package aurora.report.usecase.logr0103novo;

import aurora.report.helper.JasperHelper;
import aurora.report.model.Report;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


@Service
@RequiredArgsConstructor

public class Logr0103NovoService {

    private final Logr0103NovoRepository repository;

    private Logr0103NovoModel buildData() {

        final var cargaNew = repository.findCargaNew()
                .orElseThrow(() -> new RuntimeException("Problemas com a Carga, verifique!"));

            cargaNew.setOrdensNovo(repository.listOrdensNew());

            cargaNew.getOrdensNovo().forEach(ordemNew -> {

                ordemNew.setPedidosNovo(repository.listPedidosNew());

                ordemNew.getPedidosNovo().forEach(pedidoNew ->
                    pedidoNew.setReservaNovo(repository.listReservaNew()));

            });

        return Logr0103NovoModel.builder()
                .cargaNovo(cargaNew)
                .build();
    }

    public Report builReport(@NonNull Report.Type type) {
        final var params = new HashMap<String,Object>();
        final var data = buildData();
        final var body =  Report.Type.JSON.equals(type)
                ? data
                : JasperHelper.makeReport(type, "logr0103new", params, List.of(data));

        return Report.builder()
                .body(body)
                .type(type.getMediaType())
                .filename("logr0103new")
                .build();

    }

}
