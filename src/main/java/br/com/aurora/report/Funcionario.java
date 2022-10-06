package br.com.aurora.report;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Funcionario {
    private String nome;
    private String cargo;
    private Integer quantidade;
}