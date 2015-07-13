import java.io.*;
import java.net.*;
import java.util.*;
public class sink_node1 {

	public static void sop(Object s1)
	{
		System.out.println(String.valueOf(s1));
	}
	
	public static void sopnl(Object s1)
	{
		System.out.print(String.valueOf(s1));
	}
	public static int temperature=0;
	public static String ip_temp[]=new String[2];
	public static String cur_node;
	public static String cur_child[]=new String[20];
	public static String child_iport[]=new String[20];
	public static String fin[]=new String[20];
	public static int ip,port;
	public static String adj_node[]=new String[20];
	public static String node[]=new String[20];
	public static String x[]=new String[20];
	public static String y[]=new String[20];
	public static String iport[]=new String[20];
	public static String temp[]=new String[20];
	public static String range[]=new String[20];
	public static String sink[]=new String[20];
	public static int adj[][]=new int[20][20];
	public static int mst[][]=new int[20][20];
	public static String parent[]=new String[20];
	public static String children[]=new String[20];
	public static String weight[]=new String[20];
	public static String packet_stream[]=new String[100];
	public static String temp_packet[]=new String[100];
	public static BufferedWriter bw,bw_l;
	public static FileWriter fw,fw_l;
	public static String sink_n;
	public static DatagramSocket server1;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String name="Topology(1).txt";
		parse(name);
		System.out.println("Enter Node name:");
		Scanner s1=new Scanner(System.in);
		String ans=args[0];
		int index=0;
		for(int i=0;i<20;i++)
		{
			if(node[i].matches(ans))
			{
				index=i;
			}
		}
		temperature=Integer.parseInt(temp[index]);
		cur_node=node[index];
		ip_temp=iport[index].split("/");
		InetAddress ip=InetAddress.getByName((ip_temp[0]));
		port=Integer.parseInt(ip_temp[1]); 
		if(sink[index].contains(", 1"))
		{
		sink_n=node[index];
		sop("SINK NODE RUNNING ON"+port);
		BufferedWriter bw2=openlog(ans,fw_l,bw_l);
		bw2.write(name+"parsed.....");
		bw2.write("\n");
		bw2.write(ans+"is the Sink Node");
		bw2.write("\n");
		bw2.write("IP ADDRESS:"+String.valueOf(ip));
		bw2.write("\n");
		bw2.write("PORT:"+port);
		bw2.write("\n");
		bw2.write("--------------");
		bw2.write("\n");
		adjlist();
		detneighbour(bw2,ans);
		
		//printadj(adj);	
		mst(adj,bw2);
		bw2.write("Minimum Spanning Tree Created....");
		bw2.write("\n");
		bw2.write("Spanning tree details writen into MST.txt");
		bw2.write("\n");
		//parsemst("MST.txt");
		bw2.write("Parsed MST.txt....");
		bw2.write("\n");
		bw2.write("Parent & Children Array Created....");
		bw2.write("\n");
		BufferedWriter bw1=openfile(bw,fw);
		//DatagramSocket server2=openConn(server1,port);
		bw2.write("Packet generated.....");
		bw2.write("\n");
		packetgen(bw1,bw2);
		parsepacket("packet.txt",bw2);
		bw2.write("Recorded Packet in packet_stream");
		bw2.write("\n");
		bw2.write("Distributing Information to children.....");
		bw2.write("\n");
		gen_info(packet_stream,port,bw2);
		
		//server2.close();
		//dist_info(port,ans);
		bw2.write("Starting Query Dissemination....");
		bw2.write("\n");
		//bw2.close();
		query_dis(port,ans,bw2);
		bw2.close();
		//dist_info_sin(port,ans);
		}
		else
		{
			sop("NOT THE SINK NODE");
		}
		
	
		
		
	}
	private static void detneighbour(BufferedWriter bw2, String ans)throws Exception {
		// TODO Auto-generated method stub
		bw2.write("ADJACENT NODES-NODES WITHIN SAME TRANSMISSION RANGE OF "+ans+": ");
		int t=0,index=0;
		sop("ADJACENT NODES");
		for(int i=0;i<20;i++)
		{
			if(node[i].matches(ans))
			{
				index=i;
			}
		}
		
		for(int i=0;i<20;i++)
		{
		
				if(adj[index][i]!=0&&t<20)
				{
					adj_node[t]=node[i];
					bw2.write(adj_node[t]);
					bw2.write("\t");
					//sopnl("---->"+adj_node[t]);
					t++;
					
				}
		}
	}
	

	private static BufferedWriter openlog(String ans,FileWriter fw2, BufferedWriter bw2)throws Exception  {
		// TODO Auto-generated method stub
		String filename=ans+"_log.txt";
		File f1=new File("log//"+filename);
		try
		{
			if(!f1.exists())
			{
				sop("FILE CREATED");
				f1.createNewFile();
			}
			fw2=new FileWriter(f1.getAbsolutePath());
			bw2=new BufferedWriter(fw2);
		}
		catch(IOException e)
		{
			sop(e);
		}
			return bw2;
	
		
	}

	private static DatagramSocket openConn(DatagramSocket server12,int port) throws IOException {
		// TODO Auto-generated method stub
		server12=new DatagramSocket(port);
		
		return server12;
	}

	private static void parsepacket(String filename, BufferedWriter bw2) throws Exception{
		// TODO Auto-generated method stub
		FileInputStream fi;
		Scanner s1,s2;
		File file = new File(filename);
		
		int i=0;
		        try {
        	
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter("-->");
            
            while (scanner.hasNextLine()&&i<packet_stream.length) {
            	
                   	packet_stream[i]=scanner.next().trim();
                   	i++;
            	
           
            }
            for(int j=0;j<packet_stream.length;j++)
            {
            	if(packet_stream[j]!=null)
            	System.out.println(packet_stream[j]+"---->"+j);
            	
            	
            }
           
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		        dist(packet_stream,bw2);
		        bw2.write("DONE");
	}

	private static void dist(String[] packet_stream2, BufferedWriter bw2)throws Exception {
		// TODO Auto-generated method stub
		bw2.write("STARTING....");
		bw2.write("BROADCASTING DONE TO NEIGHBOURING NODES..."+"\n");
		bw2.write("\n");
		for(int t=0;t<adj_node.length;t++)
		{
			if(adj_node[t]!=null)
			{
			for(int i=0;i<packet_stream2.length;i++)
			{
				if(packet_stream2[i]!=null)
				{
					bw2.write(packet_stream2[i].trim());
					bw2.write(",");
				}
			}
			bw2.write("----->"+adj_node[t]);
			bw2.write("\n");
			}
		}
		bw2.write("\n");
		bw2.write("PACKET RECEIVED FROM NEIGHBOURING NODES");
		bw2.write("\n");
		for(int t=0;t<adj_node.length;t++)
		{
			if(adj_node[t]!=null)
			{
			for(int i=0;i<packet_stream2.length;i++)
			{
				if(packet_stream2[i]!=null&&adj_node[t]!=null)
				{
					bw2.write(packet_stream2[i].trim());
					bw2.write(",");
				}
			}
			
			bw2.write("<-----"+adj_node[t]);
			bw2.write("\n");
			}
		}
		for(int i=0;i<adj_node.length;i++)
		{
			if(adj_node[i]!=null)
			{
				
				bw2.write("PACKET DISCARDED FROM :"+adj_node[i]);
				bw2.write("\n");
			}
		}
	}

	private static void gen_info(String[] packet_stream2,int port,BufferedWriter bw2) throws IOException {
		// TODO Auto-generated method stub
		
		int t=0,index=0,index_n=0;
		for(int s=0;s<temp_packet.length;s++)
		{
			temp_packet[s]=null;
			
			
		}
		for(int j=1;j<parent.length;j++)
		{
			if(parent[j].matches(sink_n))
			{
				cur_child[t]=children[j];
				t++;
			}
			
		}
		System.out.println("CUR_CHILD:");
		for(int i=0;i<cur_child.length;i++)
		{
			sop(cur_child[i]);
		}
		int count=0;
		for(int i=0;i<cur_child.length;i++)
		{
			if(cur_child[i]!=null)
			{
				//sop("CUR_CHILD"+cur_child[i]);
			for(int j=0;j<packet_stream2.length;j++)
			{
				
				if(cur_child[i].matches(String.valueOf(packet_stream2[j])))
				{
					index=j;
					sop("Start Index:"+index);
				}
				
			}
			for(int s=index;s<packet_stream2.length;s++)
			{
				if(packet_stream2[s]!=null)
				{
					if(packet_stream2[s].matches("!"))
					{
						index_n=s;
						break;
					}
				}
			}
			sop("FINAL START:"+index);
			sop("FINAL END:"+index_n);
			for(int s=index;s<index_n;s++)
			{
				if(packet_stream2[s]!=null)
				{
					temp_packet[s]=packet_stream2[s];
					//sop(temp_packet[s]);
				}				
			}
			}
			
			//server2.close();
			
		}
		
		dist_info(packet_stream2,port,bw2);
	}

	private static void dist_info(String[] senddata,int port,BufferedWriter bw2) throws IOException {
		// TODO Auto-generated method stub
		
		DatagramSocket server13=new DatagramSocket(port);
		int count=0,t=0;
		sop("I am sink node");
		sop("Connected to "+port);
		sop("Sending Data..");
		String cur_child[]=new String[20];
		for(int j=1;j<parent.length;j++)
		{
			if(parent[j].matches(sink_n))
			{
				cur_child[t]=children[j];
				t++;
			}
			
		}
		sop("SEND DATA:");
		int child_index=0;
		/*for(int i=0;i<senddata.length;i++)
		{
			if(senddata[i]==cur_child1&&senddata[i]!=null)
			{			
				child_index=i;
			}
		}
		for(int i=child_index;i<senddata.length;i++)
		{
			//sop(senddata[i]);
			if(senddata[i]!=null)
			{
				count++;
			}
		}
		
		sop("SEND DATA 1:");
		byte senddata3[]=new byte[1024];
		int t=0;
		String senddata1[]=new String[count];
		
		for(int i=child_index;i<senddata.length;i++)
		{
			if(senddata[i]!=null&&t<count)
			{			
				senddata1[t]=senddata[i];
				t++;
			}
		}*/
		for(int i=0;i<senddata.length;i++)
		{
			sop(senddata[i]);
		}
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < senddata.length; i++) {
			if(senddata[i]!=null)
			{
				result.append(senddata[i]);
				result.append(",");
			}
		}
		String mynewstring = result.toString();
		//sop(Arrays.toString(senddata1));
		String senddata2=cur_node+","+mynewstring;
		/*bw2.write("Packet");
		bw2.write("\t");
		bw2.write("Children");
		bw2.write("\n");
		bw2.write(senddata2);
		bw2.write("\t");
		bw2.write(cur_child1);
		bw2.write("\n");*/
		/*for(int i=0;i<count;i++)
		{
			senddata[i]=null;
			senddata1[i]=null;
		}*/
		byte[] senddata3 = senddata2.getBytes();
		sop("SEND DATA 3:");
		sop(new String(senddata3));
		//System.out.println("cur_child"+cur_child1);
		//server13.setReuseAddress(true);
		InetAddress ip_addr;
		for(int i=0;i<cur_child.length;i++)
		{
			if(cur_child[i]!=null)
			{
				for(int j=0;j<node.length;j++)
				{
					if(cur_child[i].matches(node[j]))
					{
						String ip_temp[]=iport[j].split("/");
						ip_addr=InetAddress.getByName(ip_temp[0]);
						port=Integer.parseInt(ip_temp[1]);
						DatagramPacket packet=new DatagramPacket(senddata3,senddata3.length,ip_addr,port);
						server13.send(packet);
					}
					else
					{
						sop("NODE NOT FOUND EXCEPTION");
					}
				}
			}
		}
		server13.close();
		
		
		
		
	}

	private static BufferedWriter openfile(BufferedWriter bw2, FileWriter fw2) throws IOException {
		// TODO Auto-generated method stub
		File f1=new File("packet.txt");
		if(!f1.exists())
		{
			f1.createNewFile();
		}
		FileWriter fw=new FileWriter("packet.txt");
		BufferedWriter bw=new BufferedWriter(fw);
		return bw;
		
			
	
		}		
	
	private static void packetgen(BufferedWriter bw1,BufferedWriter bw2) throws IOException {
		// TODO Auto-generated method stub
		
		String temp_child[]=new String[20];
		int t=0;
		
		for(int j=0;j<parent.length;j++)
		{
			if(parent[j].matches(node[0]))
			{
				temp_child[t]=children[j];
				sop(children[j]);
				t++;
			}
		}
		for(int i=1;i<temp_child.length;i++)
		{
			if(temp_child[i]!=null)
			{
				bw1.write(temp_child[i]);
				bw1.write("-->");
				bw2.write(temp_child[i]);
				bw2.write("-->");
				countchild(temp_child[i],bw1,bw2);
				bw1.write("!");
				bw1.write("-->");
				bw1.write("\n");
				bw2.write("!");
				bw2.write("-->");
				bw2.write("\n");
			}
		}
		
		bw1.close();
		
		
	}
	private static void countchild(String cur_node2,BufferedWriter bw1,BufferedWriter bw2) throws IOException{
		// TODO Auto-generated method stub
		int count=0;
		if(cur_node2!=null)
		{
			bw1.write(cur_node2);
			bw1.write("-->");
			bw2.write(cur_node2);
			bw2.write("-->");
		for(int i=1;i<parent.length;i++)
		{
			if(parent[i].matches(cur_node2))
			{
				count++;
			}
			
		}
		if(count>0)
		{
			bw1.write(String.valueOf(count));
			bw1.write("-->");
			bw2.write(String.valueOf(count));
			bw2.write("-->");
			String cur_child1[]=new String[count];
			for(int i=0;i<count;i++)
			{
				cur_child1[i]=null;
			}
		
			int t=0;
			for(int i=0;i<parent.length;i++)
			{
				if(parent[i].matches(cur_node2))
				{	
					if(t<count)
					{
						cur_child1[t]=children[i];
						t=t+1;
					}
				}
			
			}
		
		for(int i=0;i<count;i++)
		{
			//System.out.println(cur_child1[i]);	
			bw1.write(cur_child1[i]);
			bw1.write("-->");
			bw2.write(cur_child1[i]);
			bw2.write("-->");
			
		}
		
		for(int i=0;i<cur_child1.length;i++)
		{
			countchild(cur_child1[i],bw1,bw2);
		}
		
		System.out.println("---------------");
		}
		}
		
	}
		private static void query_dis(int port1,String ans,BufferedWriter bw2) throws IOException {
		// TODO Auto-generated method stub
			Scanner s1=new Scanner(System.in);
			int count=0;
			sop("Connected to :"+port1);
			DatagramPacket send_q;
			int q=0;
			byte send1[]=new byte[1024];
			byte rcv1[]=new byte[1024];
			String query="";
			int t=0;
			for(int i=0;i<parent.length;i++)
			{
				if(parent[i].matches(ans))
				{
					for(int j=0;j<node.length;j++)
					{
						if(node[j].matches(children[i]))
						{
							child_iport[t]=iport[j];
							t++;
						}
					}
				}
			}
			DatagramSocket server1=new DatagramSocket(port1);
		while(true)
		{
			
			
		sop("Enter Query(min | max | avg):");
		query=s1.next();
		count=0;
		if(query.matches("min"))
		{
			String send="min";
			bw2.write("Query ");
			bw2.write(send);
			bw2.write("Sent to children....");
			bw2.write("\n");
			send1=send.getBytes();
			for(int i=1;i<child_iport.length;i++)
			{
				if(child_iport[i]!=null)
				{
				String ip_temp[]=child_iport[i].split("/");
				InetAddress ip_addr=InetAddress.getByName(ip_temp[0]);
				port=Integer.parseInt(ip_temp[1]);
				send_q=new DatagramPacket(send1,send1.length,ip_addr,port);
				server1.send(send_q);
				sop("DATA SENT");
				count++;
				}
			}
			//server1.close();
			
		}
		else if(query.matches("max"))
		{
			String send="max";
			bw2.write("Query ");
			bw2.write(send);
			bw2.write("Sent to children....");
			bw2.write("\n");
			send1=send.getBytes();
			for(int i=1;i<child_iport.length;i++)
			{
				if(child_iport[i]!=null)
				{
					String ip_temp[]=child_iport[i].split("/");
					InetAddress ip_addr=InetAddress.getByName(ip_temp[0]);
					port=Integer.parseInt(ip_temp[1]);
					send_q=new DatagramPacket(send1,send1.length,ip_addr,port);
					server1.send(send_q);
					sop("DATA SENT");
					count++;
				}
			}
			//server1.close();
		}
		else if(query.matches("avg"))
		{
			String send="avg";
			bw2.write("Query ");
			bw2.write(send);
			bw2.write("Sent to children....");
			bw2.write("\n");
			send1=send.getBytes();
			for(int i=1;i<child_iport.length;i++)
			{
				if(child_iport[i]!=null)
				{
					String ip_temp[]=child_iport[i].split("/");
					InetAddress ip_addr=InetAddress.getByName(ip_temp[0]);
					port=Integer.parseInt(ip_temp[1]);
					send_q=new DatagramPacket(send1,send1.length,ip_addr,port);
					server1.send(send_q);
					sop("DATA SENT");
					count++;
				}
			}
			//server1.close();
			
		}
		break;
		}
		server1.close();
		sop("Connection Closed");
		DatagramSocket server2=new DatagramSocket(port1);
		String ip_t[]=new String[2];
		String port_t="",name_t="";
		while(true)
		{
			
			while(q<count)
			{
				DatagramPacket rcvpacket=new DatagramPacket(rcv1,rcv1.length);
				server2.receive(rcvpacket);
				String op=new String(rcvpacket.getData());
				fin[q]=op.trim();
				port_t=String.valueOf(rcvpacket.getPort());
				sop(port_t);
				for(int i=0;i<node.length;i++)
				{
					ip_t=iport[i].split("/");
					//sop(ip_t[1]);
					if(ip_t[1].matches(port_t))
					{
						name_t=node[i];
					}
				}
				sop("------");
				bw2.write(fin[q]+"<------"+name_t);
				bw2.write("\n");
				sop(fin[q]+"-----"+name_t);
				q=q+1;
			}
			sop("BREAK");
			server2.close();
			break;
		
		}
		sop("RECEIVED PACKET:");
		bw2.write("Packet Received from Children...");
		bw2.write("\n");
		for(int i=0;i<fin.length-1;i++)
		{
			if(fin[i]!=null)
			{
				sop(fin[i]);
				
			}
		}
		sop("Sink Node Temperature"+temperature);
		bw2.write("Sink Node Temperature"+temperature);
		bw2.write("\n");
		if(query.matches("min"))
		{
			int min=Integer.MAX_VALUE;
			for(int i=0;i<fin.length;i++)
			{
				if(fin[i]!=null)
				{
				if(Integer.parseInt(fin[i].trim())<min)
				{
					min=Integer.parseInt(fin[i].trim());
				}
				}
			}
			if(temperature<min)
			{
				min=temperature;
			}
			sop("MIN VALUE RESULT:"+min);
			bw2.write("Min Query Value Result:");
			bw2.write(String.valueOf(min));
			bw2.write("\n");
		}
		if(query.matches("max"))
		{
			sop("IN MAX");
			int max=0;
			for(int i=0;i<fin.length;i++)
			{
				if(fin[i]!=null)
				{
				if(Integer.parseInt(fin[i].trim())>max)
				{
					max=Integer.parseInt(fin[i].trim());
				}
				}
			}
			if(temperature>max)
			{
				max=temperature;
			}
			sop("MAX VALUE RESULT:"+max);
			bw2.write("Max Query Value Result:");
			bw2.write(String.valueOf(max));
			bw2.write("\n");
		}
		if(query.matches("avg"))
		{
			int count_child=0;
			sop("IN AVG");
			double avg,sum=0.0,sum1=0.0;
			for(int i=0;i<fin.length;i++)
			{
				if(fin[i]!=null)
				{
					sum=sum+Double.valueOf(fin[i].trim());
					count_child++;
				}
			}
			int length=count_child+1;
			sop(length);
			sum1=sum+temperature;
			avg=sum1/length;
			sop("AVG VALUE RESULT:"+avg);
			bw2.write("Average Query Value Result:");
			bw2.write(String.valueOf(avg));
			bw2.write("\n");
		}
		 
		for(int i=0;i<fin.length;i++)
		{
			fin[i]=null;
		}
		
		
	}
		private static void parsemst(int parent1[],int adj1[][], BufferedWriter bw2) throws Exception {
		// TODO Auto-generated method stub
			bw2.write("Minimum Spannning tree Information");
			bw2.write("\n");
			for(int i=0;i<20;i++)
			{
				parent[i]=node[parent1[i]];
				bw2.write(node[parent1[i]]+"---->"+node[i]+"-----"+String.valueOf(adj1[parent1[i]][i]));
				bw2.write("\n");
				children[i]=node[i];
				weight[i]=String.valueOf(adj1[parent1[i]][i]);
				
			}
			
			bw2.write("\n");
			
	}

		private static void dist_info(int port,String ans)throws IOException {
		// TODO Auto-generated method stub
			
		DatagramSocket server1 = new DatagramSocket(port);
		byte[] sendData1 = new byte[1024];
		String sendData = ans;
		sendData1 = sendData.getBytes();
		InetAddress ip_addr;
		int t=0;
		for(int i=0;i<20;i++)
		{
			if(parent[i].matches(cur_node))
			{
				for(int j=0;j<20;j++)
				{
					if(node[j].matches(children[i]))
					{
						cur_child[t]=children[i];
						child_iport[t]=iport[j];
						String ip_temp[]=iport[j].split("/");
						ip_addr=InetAddress.getByName((ip_temp[0]));
						port=Integer.parseInt(ip_temp[1]);
						DatagramPacket packet=new DatagramPacket(sendData1,sendData1.length,ip_addr,port);
						server1.send(packet);
						t++;
					}
				}
			}
		}
		
		server1.close();
	}

		private static void propogate_info(String filename,int[] parent, int[][] adj) {
		// TODO Auto-generated method stub
			File f1=new File(filename);
			FileWriter fw;
			BufferedWriter bw;
			try {
				
				if (!f1.exists()) {
					f1.createNewFile();
				}
				fw = new FileWriter(filename);
				bw=new BufferedWriter(fw);
				
				for(int i=0;i<20;i++)
				{
						
						
							bw.write(node[parent[i]]);
							bw.write("-->");
							bw.write(node[i]);
							bw.write("-->");
							bw.write(String.valueOf(adj[parent[i]][i]));
							bw.write("\n");
						
					
				}
				bw.close();
				
			}
			catch(Exception e)
			{
				sop(e);
			}
		
	}

		private static void mst(int[][] adj2, BufferedWriter bw2)throws Exception {
			// TODO Auto-generated method stub
			int key[]=new int[20];
			int parent[]=new int[20];
			Boolean visited[]=new Boolean[20];
			for(int i=0;i<20;i++)
			{
				key[i]=Integer.MAX_VALUE;
				visited[i]=false;
			}
			key[0]=0;
			parent[0]=0;
			visited[0]=true;
			for(int j=0;j<19;j++)
			{
				int min=minkey(key,visited);
				visited[min]=true;
				for(int i=0;i<20;i++)
				{
					if(adj[min][i]!=0 && visited[i]==false && adj[min][i]<key[i])
					{
						parent[i]=min;
						key[i]=adj[min][i];
					}
				}
			}
			
			printmst(parent,20,adj);
			propogate_info("MST.txt",parent,adj);
			parsemst(parent,adj,bw2);
			
		}

		private static void printmst(int[] parent, int a, int[][] adj2) {
			// TODO Auto-generated method stub
			sop("Edges"+"\t"+"Weights");
			for(int i=0;i<20;i++)
			{
				mst[parent[i]][i]=adj[parent[i]][i];
				sop(node[parent[i]]+"-->"+node[i]+"\t"+adj[parent[i]][i]);
			}
		}

		private static int minkey(int[] key, Boolean[] visited) {
			// TODO Auto-generated method stub
			int min=Integer.MAX_VALUE;
			int index=0;
			for(int i=0;i<20;i++)
			{
				if(visited[i]==false && key[i]<min)
				{
					min=key[i];
					index=i;
				}	
			}
			return index;
		}

		private static void printadj(int[][] adj2) {
			// TODO Auto-generated method stub
			for(int i=0;i<20;i++)
			{
				for(int j=0;j<20;j++)
				{
					sopnl(adj[i][j]+"\t");
					
					
				}
				sop("\n");
			}
		}
		private static void adjlist() {
			// TODO Auto-generated method stub
			for(int i=0;i<20;i++)
			{
				for(int j=i+1;j<20;j++)
				{
					if(eucl(i,j)<=Integer.parseInt(range[0]))
					{
						adj[i][j]=(int) eucl(i,j);
					}
					else
					{
						adj[i][j]=0;
					}
				}
			}
			
			
			
		}
		
		private static double eucl(int i, int j) {
			// TODO Auto-generated method stub
			int a=Integer.parseInt(x[i]);
			int b=Integer.parseInt(y[i]);
			int c=Integer.parseInt(x[j]);
			int d=Integer.parseInt(y[j]);
			
			double xdiff=c-a;
			double fin1= Math.pow(xdiff, 2);
			double ydiff=d-b;
			double fin2=Math.pow(ydiff, 2);
			
			return (Math.sqrt(fin1+fin2));
		}
		private static void parse(String filename) {
			// TODO Auto-generated method stub
			FileInputStream fi;
			Scanner s1,s2;
			File file = new File(filename);
			int i=0;
			        try {
	        	
	            Scanner scanner = new Scanner(file);
	            scanner.useDelimiter(", ");
	            
	            while (scanner.hasNextLine()&&i<20) {
	            node[i]=scanner.next();
	            x[i]=scanner.next();
	            y[i]=scanner.next();
	            iport[i]=scanner.next();
	            temp[i]=scanner.next();
	            range[i]=scanner.next();
	            sink[i]=scanner.nextLine();
	            i++;
	            }
	            scanner.close();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }
			
			
		}

}
