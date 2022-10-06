package br.com.aurora.report.usecase.picking;

import br.com.aurora.report.helper.ReportHelper;
import br.com.aurora.report.model.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/picking")
@RequiredArgsConstructor
public class PickingResource {

    private final PickingService service;

    @GetMapping
    public ResponseEntity<byte[]> generate(@RequestParam(name = "type", required = false, defaultValue = ReportHelper.DEFAULT_TYPE) final Report.Type type,
                                           @RequestParam(name = "nrCarga") Integer nrCarga,
                                           @RequestParam(name = "unidOrigem") Integer unidOrigem,
                                           HttpServletResponse response){
        final var report = service.buildReport(type,nrCarga,unidOrigem);
        return ResponseEntity
                .ok()
                .header("Content-Type", report.getType())
                .header("Content-Disposition", String.format("inline; filename=%s",String.format("%s.%s",report.getFilename(), type.toString().toLowerCase())))
                .body(report.getBody());
    }

}