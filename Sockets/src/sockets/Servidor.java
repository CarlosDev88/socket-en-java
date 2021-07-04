package sockets;

import javax.swing.*;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MarcoServidor mimarco = new MarcoServidor();

	}
}

class MarcoServidor extends JFrame implements Runnable {
	private JTextArea areatexto;

	public MarcoServidor() {

		setBounds(600, 300, 280, 350);
		setTitle("SERVIDOR");

		JPanel milamina = new JPanel();
		milamina.setLayout(new BorderLayout());

		areatexto = new JTextArea();

		milamina.add(areatexto, BorderLayout.CENTER);
		add(milamina);

		Thread hilo = new Thread(this);
		hilo.start();

		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	@Override
	public void run() {
		try {
			ServerSocket servidor = new ServerSocket(3000);
			String nick;
			String ip;
			String mensaje;

			PaqueteEnvio paqueteResivido = new PaqueteEnvio();

			while (true) {
				Socket socket = servidor.accept();

				ObjectInputStream paqueteDatos = new ObjectInputStream(socket.getInputStream());
				paqueteResivido = (PaqueteEnvio) paqueteDatos.readObject();

				nick = paqueteResivido.getNick();
				ip = paqueteResivido.getIp();
				mensaje = paqueteResivido.getMesanje();

				areatexto.append("\n" + nick + ": " + mensaje + " para: " + ip);
				
				Socket enviaDestinatario = new Socket(ip,9090);
				ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
				paqueteReenvio.writeObject(paqueteResivido);
				paqueteReenvio.close();
				
				enviaDestinatario.close();
				socket.close();
			}

		} catch (IOException | ClassNotFoundException e) {

			e.printStackTrace();
		}

	}

}
