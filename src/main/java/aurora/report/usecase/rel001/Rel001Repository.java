package aurora.report.usecase.rel001;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class Rel001Repository {
    private final NamedParameterJdbcTemplate jdbc;

    private RowMapper<Endereco> rowMapperEndereco = (rs, rowNum) ->
            Endereco.builder()
                    .logradouro(rs.getString("desc_logradouro"))
                    .build();

    private RowMapper<Pessoa> rowMapperPessoa = (rs, rowNum) ->
            Pessoa.builder()
                    .id(rs.getInt("cod_pessoa"))
                    .nome(rs.getString("desc_pessoa"))
                    .build();

    private RowMapper<Compra> rowMapperCompra = (rs, rowNum) ->
            Compra.builder()
                    .valor(rs.getDouble("valor"))
                    .build();

    public Optional<Endereco> findEndereco(@NonNull final String cep) {
        return jdbc.query("""
                     select desc_logradouro
                  from endereco 
                 where cep = :cep""",
                  Map.of("cep",cep), rowMapperEndereco).stream().findFirst();
    }

    public Optional<Pessoa> findPessoa(@NonNull final Integer id) {
        return jdbc.query("""
                select nome
                  from pessoa 
                 where id = :id""",
                Map.of("id",id), rowMapperPessoa).stream().findFirst();
    }



    public List<Compra> listCompras(@NonNull final Integer clienteId) {
        return jdbc.query("""
                     select valor
                  from compras 
                 where cliente_codigo = :clientId""",
                Map.of("clienteId",clienteId), rowMapperCompra);
    }

}
