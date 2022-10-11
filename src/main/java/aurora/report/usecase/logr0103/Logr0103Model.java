package aurora.report.usecase.logr0103;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Logr0103Model {
    private Carga carga;
    private List<Ordem> ordens;
    private List<ItemOrdem> itemOrdens;
    private List<Reserva> reservas;
    private List<PedFatur> pedidos;

}