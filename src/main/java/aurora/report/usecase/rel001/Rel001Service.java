package aurora.report.usecase.rel001;

import aurora.report.exception.BusinessException;
import aurora.report.helper.JasperHelper;
import aurora.report.model.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Rel001Service {
    private final Rel001Repository repository;

    public Report buildReport(@NonNull Report.Type type,
                              @NonNull Integer clienteId,
                              @NonNull  String cep){
        final var params = new HashMap<String,Object>();
        return Report.builder()
                .body(JasperHelper.makeReport(type, "picking", params, List.of(buildData(clienteId, cep))))
                .type(type.getMediaType())
                .filename("picking")
                .build();
    }


    private Rel001Model buildData(@NonNull final Integer clienteId,
                                        @NonNull final String cep){

        final var pessoa = repository.findPessoa(clienteId)
                .orElseThrow(() -> new BusinessException("Pessoa não econtrada"));

        final var endereco =repository.findEndereco(cep)
                .orElse(Endereco.builder()
                    .logradouro("nao informado")
                    .build());
        return Rel001Model.builder()
                .nome(pessoa.getNome())
                .logradouro(endereco.getLogradouro())
                .compras(repository.listCompras(clienteId))
                .build();
    }

}