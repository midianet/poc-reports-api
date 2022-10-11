package aurora.report.exception;

import org.springframework.lang.NonNull;

/**
 * Classe que respresenta uma Excessão de Negócio
 */
public class BusinessException extends RuntimeException {

    public BusinessException(@NonNull String message) {
        super(message);
    }

//    public void sacar(Integer numConta, Double valor){
//        ///repositor.createTransaction()
//                //try{
//                    repository.initTransaction()
//                    Conta conta  = repository.find(numConta);
//                    if(conta == null) thow "conta invalida""
//                    if(conta.saldo < valor) thow "saldo insuficiente"
//                    conta.saldo = conta.sald - valor;
//                    repository.save(conta)
//                    //repositoyr.commiit()
//                //}catch(Exception e){
//                    //repositor.rollaback
//                //}finaly {
//                  //  repository.closeTransaction()
//                //}
//        }

//    @Transactional
//    @RollesAllowed({"ANDMIN","MANAGER"})
//    public void sacar(Integer numConta, Double valor){
//        Conta conta  = repository.find(numConta);
//        if(conta == null) thow "conta invalida""
//        if(conta.saldo < valor) thow "saldo insuficiente"
//        conta.saldo = conta.sald - valor;
//        repository.save(conta)
//    }


//    }

}
