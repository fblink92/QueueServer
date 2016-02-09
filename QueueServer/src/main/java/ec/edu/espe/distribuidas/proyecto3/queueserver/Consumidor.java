/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.distribuidas.proyecto3.queueserver;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

/**
 *
 * @author Freddy
 * asincrono
 */
public class Consumidor {
     @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/Queue")
    private static Queue queue;

    public void recibeMensajeAsincronoCola() throws JMSException {
        Connection connection = null;
        Session session = null;

        MessageConsumer consumer = null;
        TextoListener listener = null;
        boolean esTransaccional = false;

        try {
            connection = connectionFactory.createConnection();
            // Creamos una sesion sin transaccionalidad y con envio de acuse automatico
            session = connection.createSession(esTransaccional, Session.AUTO_ACKNOWLEDGE);
            // Creamos el consumidor a partir de una cola
            consumer = session.createConsumer(queue);
            // Creamos el listener, y lo vinculamos al consumidor -> asincrono
            listener = new TextoListener();
            consumer.setMessageListener(listener);
            // Llamamos a start() para empezar a consumir
            connection.start();
			
            // Sacamos el mensaje por consola
            System.out.println("Fin asincrono");
        } finally {
            // Cerramos los recursos
            consumer.close();
            session.close();
            connection.close();
        }
    }

    public static void main(String[] args) throws Exception {
        Consumidor p = new Consumidor();
        p.recibeMensajeAsincronoCola();
    }
}
