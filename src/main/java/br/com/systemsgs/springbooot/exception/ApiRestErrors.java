package br.com.systemsgs.springbooot.exception;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class ApiRestErrors {

    @Getter
    private List<String> erros;

    public ApiRestErrors(List<String> erros) {
        this.erros = erros;
    }

    public ApiRestErrors(String mensagemErro){
        this.erros = Arrays.asList(mensagemErro);
    }

}
