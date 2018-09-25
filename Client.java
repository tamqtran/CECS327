import java.io.BufferedReader; 
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Client implements Runnable{
	//Reception socket
    private DatagramSocket socket;
    // UDP packet to receive data into
    private DatagramPacket packet;
    // Flag for initialisation
    private boolean failedInit = true;

    /**
     * Client constructor.
     * @param receptionPort
     * @param packetMaxLenght
     */
    public Client(int receptionPort, int packetMaxLenght) {
        try {
            // Create the socket using the reception port
            this.socket = new DatagramSocket(receptionPort);
            // Init the packet
            this.packet = new DatagramPacket(new byte[packetMaxLenght],packetMaxLenght);
            this.failedInit = false;
        } catch (SocketException e) {
            //Port already used or other error
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        if(failedInit){
            return;
        }
        // Loop undefinitly
        while(true){
            try {
                System.out.println("Waiting for packet...");

                // Wait for packet
                socket.receive(packet);

                // Assuming you are receiving string
                String msg = new String(packet.getData());
                System.out.println("Received : " + msg);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


	public static void main(String[] args) {
		try {

	        int port = 4723;
	        
	        //Start a client that can listen
	        //new Thread(new Client(port,1024)).start();

	        // Creaete a reader
	        BufferedReader reader = new BufferedReader(new FileReader("File.txt"));

	        //Create a socket
	        DatagramSocket socket = new DatagramSocket();

	        // Create a packet
	        byte[] data = new byte[1024]; // Max length
	        DatagramPacket packet = new DatagramPacket(data, data.length);

	        // Set the destination host and port 
	        packet.setAddress(InetAddress.getByName("localhost"));
	        packet.setPort(port);

	        String line = null;
	        while((line = reader.readLine()) != null){
	            //Set the data
	            packet.setData(line.getBytes());

	            //Send the packet using the socket
	            System.out.println("Sending : " + line);
	            socket.send(packet);
	            Thread.sleep(200);
	        }
	        
	        String method = "playlist(newPlaylist)";
	  
	        packet.setData(method.readLine());
	        
	        while((line = method.readLine()) != null){
	            //Set the data
	            packet.setData(line.getBytes());

	            //Send the packet using the socket
	            System.out.println("Sending : " + line);
	            socket.send(packet);
	            Thread.sleep(200);
	        }
	        
	        //Close socket and file
	        reader.close();
	        socket.close();
	
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
