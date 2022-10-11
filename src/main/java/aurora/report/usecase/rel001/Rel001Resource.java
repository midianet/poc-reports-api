package aurora.report.usecase.rel001;

import aurora.report.helper.JasperHelper;
import aurora.report.model.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin("*")
@RequestMapping("/rel001")
@RequiredArgsConstructor
public class Rel001Resource {

    private final Rel001Service service;

    @GetMapping
    public ResponseEntity<byte[]> generate(@RequestParam(name = "type", required = false, defaultValue = JasperHelper.DEFAULT_TYPE) final Report.Type type,
                                           @RequestParam(name = "clienteId") final Integer clienteId,
                                           @RequestParam(name = "cep") final String cep,
                                           HttpServletResponse response){
        final var report = service.buildReport(type,clienteId,cep);
        return ResponseEntity
                .ok()
                .header("Content-Type", report.getType())
                .header("Content-Disposition", String.format("inline; filename=%s",String.format("%s.%s",report.getFilename(), type.toString().toLowerCase())))
                .body(report.getBody());
    }

}