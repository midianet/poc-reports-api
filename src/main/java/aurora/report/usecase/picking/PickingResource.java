package aurora.report.usecase.picking;

import aurora.report.helper.JasperHelper;
import aurora.report.model.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin("*")
@RequestMapping("/picking")
@RequiredArgsConstructor
public class PickingResource {

    private final PickingService service;

    @GetMapping
    public ResponseEntity<Object> generate(@RequestParam(name = "type", required = false, defaultValue = JasperHelper.DEFAULT_TYPE) final Report.Type type,
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