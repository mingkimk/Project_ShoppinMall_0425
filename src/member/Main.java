package member;

import java.net.Socket;

public class Main {

	public static void main(String[] args) throws Exception {
		Socket withServer = null;
		withServer = new Socket("1.247.118.30", 9999);
		new Login();
	}

}
