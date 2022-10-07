package br.com.aurora.report.usecase.cafe;

import br.com.aurora.report.helper.JasperHelper;
import br.com.aurora.report.model.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository repository;

    public Report buildReport(@NonNull Report.Type type,
                              @NonNull String titulo){
        final var params = new HashMap<String,Object>();
        params.put("titulo", titulo ); //"Relatório do Café - Aurora"
        params.put("logo", JasperHelper.getImage("/static/cafe.jpg"));
        return Report.builder()
            .body(JasperHelper.makeReport(type, "ranking_cafe", params, repository.list()))
            .type(type.getMediaType())
            .filename("cafe")
            .build();
    }

}
