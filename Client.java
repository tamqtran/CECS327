import java.io.BufferedReader; 
import java.io.FileReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * 
 * @author Duong Pham
 * @since 09-25-2018
 *
 */
public class Client implements Runnable{
	//Reception socket
    private DatagramSocket socket;
    // UDP packet to receive data into
    private DatagramPacket packet;
    // Flag for initialization
    private boolean failedInit = true;

    /**
     * Client constructor.
     * @param receptionPort - the socket that is being used
     * @param packetMaxLenght - the max length of the packet
     */
    public Client(int receptionPort, int packetMaxLenght) {
        try {
            // Create the socket using the reception port
            this.socket = new DatagramSocket(receptionPort);
            // Initialize the packet
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
        // Loop indefinitely
        while(true){
            try {
            	int port = 4723;
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

                //Close socket and file
                reader.close();
                socket.close();
    	
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }   
        }
    }


	public static void main(String[] args) {
		try {

	        int port = 4723;
	        
	        //Start a client that can listen
	        new Thread(new Client(port,1024)).start();

	
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
