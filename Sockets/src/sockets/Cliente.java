package sockets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;

public class Cliente {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MarcoCliente mimarco = new MarcoCliente();
	}
}

class MarcoCliente extends JFrame {

	public MarcoCliente() {

		setBounds(300, 300, 280, 350);
		LaminaMarcoCliente milamina = new LaminaMarcoCliente();
		add(milamina);

		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}

class LaminaMarcoCliente extends JPanel implements Runnable {
	private JTextField campo1;
	private JLabel nick;
	private JTextField ip;
	private JButton miboton;
	private JTextArea campoChat;

	public LaminaMarcoCliente() {
		
		String nickUsuario = JOptionPane.showInputDialog("Login: ");

		JLabel nNick = new JLabel("Nick: ");
		add(nNick);
		nick = new JLabel();
		nick.setText(nickUsuario);
		add(nick);

		JLabel texto = new JLabel("--CLIENTE--");
		add(texto);

		ip = new JTextField(8);
		add(ip);

		campoChat = new JTextArea(12, 20);
		add(campoChat);

		campo1 = new JTextField(20);
		add(campo1);

		miboton = new JButton("Enviar");
		EnviaTexto evento = new EnviaTexto();
		miboton.addActionListener(evento);
		add(miboton);
		
		Thread mihilo = new Thread(this);
		mihilo.start();

	}

	private class EnviaTexto implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(campo1.getText());

			try {
				Socket misocket = new Socket("192.168.12", 3000);

				PaqueteEnvio datos = new PaqueteEnvio();
				datos.setNick(nick.getText());
				datos.setIp(ip.getText());
				datos.setMesanje(campo1.getText());

				ObjectOutputStream paqueteDatos = new ObjectOutputStream(misocket.getOutputStream());
				paqueteDatos.writeObject(datos);
				misocket.close();

			} catch (UnknownHostException e1) {

				e1.printStackTrace();
			} catch (IOException e1) {

				e1.printStackTrace();
				System.out.println(e1.getMessage());
			}

		}

	}

	@Override
	public void run() {
		try {
			ServerSocket servidorCliente = new ServerSocket(9090);
			Socket cliente;
			PaqueteEnvio paqueteResivido;

			while (true) {
				cliente = servidorCliente.accept();
				ObjectInputStream flujoEntrada = new ObjectInputStream(cliente.getInputStream());
				
				paqueteResivido=(PaqueteEnvio) flujoEntrada.readObject();
				
				campoChat.append("\n"+ paqueteResivido.getNick() + " : " + paqueteResivido.getMesanje());
			}
		} catch (IOException | ClassNotFoundException e) {

			e.printStackTrace();
		}

	}

}

class PaqueteEnvio implements Serializable {
	private String nick;
	private String ip;
	private String mesanje;

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMesanje() {
		return mesanje;
	}

	public void setMesanje(String mesanje) {
		this.mesanje = mesanje;
	}

}