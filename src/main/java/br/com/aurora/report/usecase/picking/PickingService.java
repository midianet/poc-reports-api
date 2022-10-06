package br.com.aurora.report.usecase.picking;

import br.com.aurora.report.helper.ReportHelper;
import br.com.aurora.report.model.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PickingService {
    private final PickingRepository repository;

    public Report buildReport(@NonNull Report.Type type,
                              @NonNull Integer nrCarga,
                              @NonNull  Integer unidOrigem){
        final var params = new HashMap<String,Object>();
        //params.put("titulo", titulo ); //"Relatório do Café - Aurora"
        return Report.builder()
                .body(ReportHelper.makeReport(type, "picking", params, repository.list(nrCarga,unidOrigem)))
                .type(type.getMediaType())
                .filename("picking")
                .build();
    }

}