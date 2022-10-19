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
public class Ordem {
    private Integer cdLocallogDest;
    private String  descrLocallogDest;
    private String cdOrdempick;
    private Integer cdLocallogDesc;
    private String  descrLocallogDesc;
    private String cdBarra;
    private List<ItemOrdem> itens;
    private List<PedFatur> pedidos;

}
