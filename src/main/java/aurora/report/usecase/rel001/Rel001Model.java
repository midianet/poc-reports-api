package aurora.report.usecase.rel001;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rel001Model {
    private String nome;
    private String logradouro;
    private List<Compra> compras;
}

