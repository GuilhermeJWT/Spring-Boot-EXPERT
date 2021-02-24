package br.com.systemsgs.springbooot.exception;

public class SenhaInvalidaException extends RuntimeException {

    public SenhaInvalidaException() {
        super("Senha Inválida!");
    }
}
