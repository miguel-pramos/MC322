package com.robotsim.environment.entity;

import com.robotsim.exceptions.ErroComunicacaoException;
import com.robotsim.exceptions.RoboDesligadoException;

/**
 * Interface que define o comportamento de entidades capazes de se comunicar no
 * ambiente de simulação.
 * <p>
 * Entidades que implementam esta interface podem enviar e receber mensagens,
 * sujeitas a possíveis
 * erros de comunicação ou ao estado (ligado/desligado) do robô.
 *
 * @see ErroComunicacaoException
 * @see RoboDesligadoException
 */
public interface Comunicavel {

    /**
     * Envia uma mensagem para outra entidade comunicável.
     *
     * @param comunicavel A entidade destinatária da mensagem.
     * @param mensagem    O conteúdo da mensagem a ser enviada.
     * @throws RoboDesligadoException   Se o robô que está tentando enviar a
     *                                  mensagem estiver desligado.
     * @throws ErroComunicacaoException Se ocorrer um erro durante o processo de
     *                                  comunicação.
     */
    public void enviarMensagens(Comunicavel comunicavel, String mensagem)
            throws RoboDesligadoException, ErroComunicacaoException;

    /**
     * Recebe uma mensagem de outra entidade.
     *
     * @param mensagem O conteúdo da mensagem recebida.
     * @throws RoboDesligadoException   Se o robô que está tentando receber a
     *                                  mensagem estiver desligado.
     * @throws ErroComunicacaoException Se ocorrer um erro durante o processamento
     *                                  da mensagem recebida.
     */
    public void receberMensagens(String mensagem) throws RoboDesligadoException, ErroComunicacaoException;
}
