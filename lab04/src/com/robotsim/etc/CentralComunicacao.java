package com.robotsim.etc;

import java.util.ArrayList;

public final class CentralComunicacao {
    private ArrayList<String> mensagens;

    public void registrarMensagem(String remetente, String mensagem) {
        this.mensagens.add(mensagem);
    }

    public void exibirMensagens() {
        for (String mensagem : this.mensagens)
            System.out.println(mensagem);
    }
}
