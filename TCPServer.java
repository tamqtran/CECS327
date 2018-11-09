import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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
            
            // Put matadata and index into master peer
            String metaData = readFile("METADATA.txt");
            put(master, Number160.createHash("METADATA.txt"), metaData);
            
            String artistIndex = readFile("artistIndex.txt");
            put(master, Number160.createHash("artistIndex.txt"), artistIndex);
            
            String albumIndex = readFile("albumIndex.txt");
            put(master, Number160.createHash("albumIndex.txt"), albumIndex);
            
            String songIndex = readFile("songIndex.txt");
            put(master, Number160.createHash("songIndex.txt"), songIndex);

            Object index = get(peers[1], Number160.createHash("songIndex.txt"));
            //System.out.println(index);
            
            // Put song in index to peer randomly
            String [] songs = index.toString().split("\n");
            for(int i = 1; i<songs.length; i++) {
            	String [] songArray = songs[i].split(";");
            	String songName = songArray[0]+"_"+songArray[1]+"_"+songArray[2]+".wav";
            	byte [] songByte = getSong(songName);
            	PeerDHT peer = peers[i%3];
            	put(peer,Number160.createHash(songName),songByte);
            }
            
            //Test playing bytes from peer get
            
            String requestSong = "Hello Goodbye_The Beatles_Magical Mystery Tour.wav";
            byte [] songByte = (byte[]) get(peers[2],Number160.createHash(requestSong));
            
            Clip current = null;
    		ByteArrayInputStream myInputStream = new ByteArrayInputStream(songByte);
        	try {
       
    			AudioInputStream audioIn = AudioSystem.getAudioInputStream(myInputStream);
    			current = AudioSystem.getClip();
    			current.open(audioIn);
    			myInputStream.close();
    			current.start();
    		}catch(Exception e) {
    			e.printStackTrace();
    		}
        	
            
            
            //***** TEST TEST ****//
          /*File file = new File("Hello Goodbye_The Beatles_Magical Mystery Tour.wav");
    		int size = (int) file.length();
    		byte[] bytes = new byte[size];
    		try 
   		{
    			BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
    			buf.read(bytes, 0, bytes.length);
    			buf.close();
    		} 
    		catch (FileNotFoundException e) 
    		{
    			e.printStackTrace();
    		} 
    		catch (IOException e) 
    		{
    			e.printStackTrace();
    		}
    		System.out.println("Actual Song Byte: "+bytes);
            put(peers[0], new Number160(123), new Data( bytes)); // put song into peer 0 master
           Data bsong = get(peers[2], new Number160(123)); // get song using peer 1
            System.out.println("Got Song Byte: "+bsong);
            Clip current = null;
    		ByteArrayInputStream myInputStream = new ByteArrayInputStream(bsong.toBytes());
        	try {
       
    			AudioInputStream audioIn = AudioSystem.getAudioInputStream(myInputStream);
    			current = AudioSystem.getClip();
    			current.open(audioIn);
    			myInputStream.close();
    			current.start();
    		}catch(Exception e) {
    			e.printStackTrace();
    		}
    		*/
        	/*****TEST**/
            
            //System.out.println(RND);
            //System.out.println(nr);
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
							//result = json.getClass().getMethod(method,argTypes).invoke(json, arguments);
							
							if(method.equals("getSong")) {
								// get song in bytes & transfer it over to UDP server
								//String requestSong = arguments[0];
								byte [] songByte = (byte[]) get(peers[1],Number160.createHash(arguments[0]));
								result = songByte;
								//result = getSong(arguments[0]);
								
							} else {
							//search here
							result = 0;
						}
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
	                peers[0] = new PeerBuilderDHT(new PeerBuilder( new Number160( i+1 ) ).ports( port ).start()).start();
	            } else {
	                peers[i] = new PeerBuilderDHT(new PeerBuilder( new Number160( i+1 ) ).masterPeer( peers[0].peer() ).ports(port+i).start()).start();
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
    
    private static void put(final PeerDHT peer, final Number160 guid, Object data) throws IOException {
    	FuturePut futurePut = peer.put(guid).object(data).start();
        futurePut.awaitUninterruptibly();
        System.out.println("peer " + peer.peerID() + " stored [key: " + guid + ", value: "+ new Data (data));
    }
    
    private static Object get(final PeerDHT peer, final Number160 guid) throws ClassNotFoundException, IOException {
           
    	FutureGet futureGet = peer.get(guid).start();
        futureGet.awaitUninterruptibly();
        System.out.println("peer " + peer.peerID() + " got: \"" + futureGet.data() + "\" for the key " + guid);
        return futureGet.data().object();
    }
    
    //***  Method for Each request ***/
    public static byte[] getSong(String song) throws JSONException, UnsupportedEncodingException 
	{
		
		File file = new File(song);
		int size = (int) file.length();
		byte[] bytes = new byte[size];
		try 
		{
			BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
			buf.read(bytes, 0, bytes.length);
			buf.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
			
			return bytes;
		
	}
    public static String readFile(String filename) throws IOException
    {
        String content = null;
        File file = new File(filename); 
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null){
                reader.close();
            }
        }
        return content;
    }
    
    public static String toHex(String arg) {
    	  return String.format("%x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
    	}
	
    
}

