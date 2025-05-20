package com.robotsim.etc;

import com.robotsim.exceptions.ErroComunicacaoException;
import com.robotsim.exceptions.RoboDesligadoException;

public interface Comunicavel {

    public void enviarMensagens(Comunicavel comunicavel, String mensagem)
            throws RoboDesligadoException, ErroComunicacaoException;

    public void receberMensagens(String mensagem) throws RoboDesligadoException, ErroComunicacaoException;
}
