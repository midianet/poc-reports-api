package aurora.report.usecase.logr0103novo;

import aurora.report.helper.JasperHelper;
import aurora.report.model.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin("*")
@RequestMapping("/xpt")
@RequiredArgsConstructor
public class Logr0103NovoResource {

    private final Logr0103NovoService service;
    @GetMapping
    public ResponseEntity<Object> generate(@RequestParam(name = "type", required = false, defaultValue = JasperHelper.DEFAULT_TYPE) final Report.Type type,
                                           HttpServletResponse response){

        final var report = service.builReport(type);

        return ResponseEntity
                .ok()
                .header("Content-Type", report.getType())
                .header("Content-Disposition", String.format("inline; filename=%s",String.format("%s.%s",report.getFilename(), type.toString().toLowerCase())))
                .body(report.getBody());
    }

}
