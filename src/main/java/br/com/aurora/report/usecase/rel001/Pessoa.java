package br.com.aurora.report.usecase.rel001;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {
    private Integer id;
    private String nome;
}
