package br.com.aurora.report;

import br.com.aurora.report.model.ReportPicking;
import br.com.aurora.report.repository.ReportPickingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportPickingRepository repository;

    @GetMapping(produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generate(HttpServletResponse response){
        var params = new HashMap<String,Object>();
        params.put("titulo", "Relatório do Café - Aurora");
        params.put("logo", ReportHelper.getImage("/static/cafe.jpg"));
        final var funcionarios = buildDados();
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/pdf; charset=UTF-8")
                .header("Content-Disposition", "inline; filename=relatorio.pdf")
                .body(ReportHelper.makePDF("ranking_cafe", params, funcionarios));
    }

    @GetMapping("/json")
    public List<Funcionario> makejson() {
        return buildDados();
    }

    @GetMapping("/picking")
    public List<ReportPicking> makePicking() {
        return repository.listReportPicking(null,null);
    }

    private List<Funcionario> buildDados(){
        return List.of(
                Funcionario.builder().nome("Ricardo")
                                     .cargo("Agile Master")
                                     .quantidade(20).build(),
                Funcionario.builder().nome("Marcos")
                                     .cargo("Arquiteto")
                                     .quantidade(17).build(),
                Funcionario.builder().nome("Abido")
                                     .cargo("Agile Master")
                                     .quantidade(15).build(),
                Funcionario.builder().nome("Zonta")
                                     .cargo("Arquiteto")
                                     .quantidade(13).build(),
                Funcionario.builder().nome("Pablo")
                                     .cargo("Sócio Acionista")
                                     .quantidade(10).build(),
                Funcionario.builder().nome("Marcelo Moreti")
                                     .cargo("Sócio Acionista")
                                     .quantidade(10).build()
        );
    }

}