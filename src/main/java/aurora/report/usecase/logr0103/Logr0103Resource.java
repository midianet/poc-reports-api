package aurora.report.usecase.logr0103;
import aurora.report.helper.JasperHelper;
import aurora.report.model.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
@CrossOrigin("*")
@RequestMapping("/logr0103")
@RequiredArgsConstructor
public class Logr0103Resource {

    private final Logr0103Service service;

    @GetMapping
    public ResponseEntity<Object> generate(@RequestParam(name = "type", required = false, defaultValue = JasperHelper.DEFAULT_TYPE) final Report.Type type,
                                           @RequestParam(name = "cargaId") final Integer cargaId,
                                           @RequestParam(name = "itemOrdPickId") final Integer itemOrdPickId,
                                           HttpServletResponse response){

        final var report = service.buildReport(type,cargaId, itemOrdPickId);

        return ResponseEntity
                .ok()
                .header("Content-Type", report.getType())
                .header("Content-Disposition", String.format("inline; filename=%s",String.format("%s.%s",report.getFilename(), type.toString().toLowerCase())))
                .body(report.getBody());
    }

}
