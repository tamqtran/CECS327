import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;


public class TCPServer {
	static Method json;
	private static final Random RND = new Random(42L);
	static PeerDHT[] peers;
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		PeerDHT master = null;
        final int nrPeers = 3;
        final int port = 4001;
        try {
        	peers = createAndAttachPeersDHT(nrPeers, port);
        	printPeers(peers);
            bootstrap(peers);
            master = peers[0];
            Number160 nr = new Number160(RND);
            
            
            System.out.println(RND);
            System.out.println(nr);
        }catch (Exception e) {
        	e.printStackTrace();
        } finally {
            if (master != null) {
                master.shutdown();
            }
        }
        json = new Method();
        
		final ServerSocket serverSocket = new ServerSocket(6777);

		try
		{
			//aSocket = new DatagramSocket(6733);
			System.out.println("Server started on port: "+6777);
			
		
			while(true)
			{
				Socket socket = serverSocket.accept();
				//System.out.println("Waiting for request...");
				ObjectInputStream request = new ObjectInputStream(socket.getInputStream());
				
				System.out.println("Request from Port: "+ socket.getPort());
				String stringRequest = (String) request.readObject();
				System.out.println("Request: "+stringRequest.toString());
			
				new Thread(new Runnable() 
				{
					@Override
					public void run() 
					{
						JSONObject JsonRequest=new JSONObject(stringRequest);
			
						//Request Data
						@SuppressWarnings("unused")
						int ID = JsonRequest.getInt("id");
						String method = JsonRequest.get("method").toString();
						String param = JsonRequest.get("arguments").toString();
						String [] arguments = param.substring(2, param.length() - 2).split("\",\"");
			
			
						//Printing out request
						/*System.out.println("ID: "+ID);
						System.out.println("Method: "+method);
						System.out.println("Arguments: "+param);
						*/ 
			
						Class<?>[] argTypes = new Class<?>[arguments.length];
						for(int i = 0; i<arguments.length; i++) 
						{
							argTypes[i] = String.class;
						}
						Object result = null;
			
						//Method call
						try 
						{						
							result = json.getClass().getMethod(method,argTypes).invoke(json, arguments);
							System.out.println(result.toString());
						} 
						catch (Exception e) 
						{
							e.printStackTrace();
						}
						
						byte[] rep = (byte[]) result;
						//Reply
						try 
						{
							Socket clientSocket  = new Socket("localhost", 6778);
							DataOutputStream reply = new DataOutputStream(clientSocket.getOutputStream());
							reply.writeInt(rep.length);
							reply.write(rep);
							
							request.close();
							reply.close();
							socket.close();
							clientSocket.close();
						} 
						catch (JSONException e1) 
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
			}
		}
		catch (SocketException e)
		{
			System.out.println("Socket: " + e.getMessage());
		}
		catch (IOException e) 
		{
			System.out.println("IO: " + e.getMessage());
		}

		finally 
		{
			if(serverSocket != null) 
				serverSocket.close();
		}
        
	}
	
	/**
	 * Print out a list containing the address of each peer
	 * @param peers array
	 */
	 public static void printPeers(PeerDHT[] peers) {
	    	for(int i = 0; i<peers.length; i++) {
	    		System.out.println("Peer " + peers[i].peerAddress());
	    	}
	    }
	
	
	/**
     * Create peers with a port and attach it to the first peer in the array.
     * 
     * @param nr The number of peers to be created
     * @param port The port that all the peer listens to. The multiplexing is done via the peer Id
     * @return The created peers
     * @throws IOException IOException
     */
	 public static PeerDHT[] createAndAttachPeersDHT( int nr, int port ) throws IOException {
	        PeerDHT[] peers = new PeerDHT[nr];
	        for ( int i = 0; i < nr; i++ ) {
	            if ( i == 0 ) {
	                peers[0] = new PeerBuilderDHT(new PeerBuilder( new Number160( RND ) ).ports( port ).start()).start();
	            } else {
	                peers[i] = new PeerBuilderDHT(new PeerBuilder( new Number160( RND ) ).masterPeer( peers[0].peer() ).start()).start();
	            }
	        }
	        return peers;
	    }
	 public static void bootstrap( PeerDHT[] peers ) {
	    	//make perfect bootstrap, the regular can take a while
	    	for(int i=0;i<peers.length;i++) {
	    		for(int j=0;j<peers.length;j++) {
	    			peers[i].peerBean().peerMap().peerFound(peers[j].peerAddress(), null, null, null);
	    		}
	    	}
	    }
    
    private static void put(final PeerDHT peer, final Number160 guid, Data data) 
            throws IOException, ClassNotFoundException {
        FuturePut futurePut = peer.put(guid).data(data).start();
        futurePut.awaitUninterruptibly();
        System.out.println("peer " + peer.peerID() + " stored [key: " + guid + ", value: "+ data);
    }
    
    private static void get(final PeerDHT peer, final Number160 guid) 
            throws IOException, ClassNotFoundException {
        FutureGet futureGet = peer.get(guid).start();
        futureGet.awaitUninterruptibly();
        System.out.println("peer " + peer.peerID() + " got: \"" + futureGet.data().object() + "\" for the key " + guid);
    }
    
}

