package br.com.aurora.report.usecase.cafe;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CafeRepository {

    public List<CafeModel> list(){
        return List.of(
                CafeModel.builder().nome("Ricardo")
                        .cargo("Agile Master")
                        .quantidade(20).build(),
                CafeModel.builder().nome("Marcos")
                        .cargo("Arquiteto")
                        .quantidade(17).build(),
                CafeModel.builder().nome("Abido")
                        .cargo("Agile Master")
                        .quantidade(15).build(),
                CafeModel.builder().nome("Zonta")
                        .cargo("Arquiteto")
                        .quantidade(13).build(),
                CafeModel.builder().nome("Pablo")
                        .cargo("Sócio Acionista")
                        .quantidade(10).build(),
                CafeModel.builder().nome("Marcelo Moreti")
                        .cargo("Sócio Acionista")
                        .quantidade(10).build()
        );
    }

}
