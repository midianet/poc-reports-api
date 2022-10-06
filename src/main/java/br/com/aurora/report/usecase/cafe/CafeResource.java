package br.com.aurora.report.usecase.cafe;

import br.com.aurora.report.helper.ReportHelper;
import br.com.aurora.report.model.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin("*")
@RequestMapping("/cafe")
@RequiredArgsConstructor
public class CafeResource {

    private final CafeService service;

    @GetMapping
    public ResponseEntity<byte[]> generate(@RequestParam(name = "type", required = false, defaultValue = ReportHelper.DEFAULT_TYPE) final Report.Type type,
                                           @RequestParam(name = "titulo") final String titulo,
                                           HttpServletResponse response){
        final var report = service.buildReport(type,titulo);
        return ResponseEntity
            .ok()
            .header("Content-Type", report.getType())
            .header("Content-Disposition", String.format("inline; filename=%s",String.format("%s.%s",report.getFilename(), type.toString().toLowerCase())))
            .body(report.getBody());
    }

}