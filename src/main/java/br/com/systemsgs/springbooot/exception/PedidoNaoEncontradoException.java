package br.com.systemsgs.springbooot.exception;

public class PedidoNaoEncontradoException extends RuntimeException {

    public PedidoNaoEncontradoException() {
        super("Pedido não Encontrado");
    }
}
