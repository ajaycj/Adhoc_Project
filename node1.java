import java.io.*;
import java.net.*;
import java.util.*;
public class node1 {
	
	
	
	public static void sop(Object s1)
	{
		System.out.println(String.valueOf(s1));
	}
	
	public static void sopnl(Object s1)
	{
		System.out.print(String.valueOf(s1));
	}
	
	public static String parent[]=new String[20];
	public static String children[]=new String[20];
	public static String weight[]=new String[20];
	public static String adj_node[]=new String[20];
	public static int adj[][]=new int[20][20];
	public static String parent_ip,ans,rec_query,temp_op2;
	public static int parent_port;
	public static String child_iport[]=new String[20];
	public static String node[]=new String[20];
	public static String iport[]=new String[20];
	public static String temp[]=new String[20];
	public static String x[]=new String[20];
	public static String y[]=new String[20];
	public static String range[]=new String[20];
	public static String sink[]=new String[20];
	public static String cur_parent;
	public static String packet_stream[];
	public static int ip_p;
	public static int port_p,flag=0;
	public static int ip_c[]=new int[20];
	public static int port_c[]=new int[20];
	public static String cur_children[];
	public static int cur_temp,cur_temp2;
	public static int leaf;
	public static BufferedWriter bw_l;
	public static FileWriter fw_l;
	public static void main(String[] args) throws Exception {
			
		parsetop("Topology(1).txt");		//PARSES TOPOLOGY
		Scanner s1=new Scanner(System.in);
		ans=args[0];
		adjlist();
		BufferedWriter bw2=openlog(bw_l,fw_l);
		detneighbour(bw2,ans);
		cur_temp=Integer.parseInt(find_temp(ans)); //FINDS TEMPERATURE OF CURRENT NODE
		cur_temp2=cur_temp;
		int status=connectparent(ans,cur_temp,bw2);	//FINDS AND CONNECTS TO PARENT NODE
		int index=0;
		// TODO Auto-generated method stub

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
	private static void detneighbour(BufferedWriter bw2, String ans) throws Exception {
		// TODO Auto-generated method stub
		//bw2.write("ADJACENT NODES-NODES WITHIN SAME TRANSMISSION RANGE OF "+ans+": ");
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
					//bw2.write(adj_node[t]);
					//bw2.write("\t");
					//sopnl("---->"+adj_node[t]);
					t++;
					
				}
		}
	}
	
	private static BufferedWriter openlog(BufferedWriter bw_l2, FileWriter fw_l2)throws Exception {
		// TODO Auto-generated method stub
		String filename=ans+"_log.txt";
		try
		{
			File f1=new File("log//"+filename);
			if(!f1.exists())
			{
				f1.createNewFile();
			}
			fw_l2=new FileWriter(f1.getAbsolutePath());
			bw_l2=new BufferedWriter(fw_l2);
		}
		catch(Exception e)
		{
			sop(e);
		}
		return bw_l2;
	}

	private static String find_temp(String ans)throws Exception {
		// TODO Auto-generated method stub
		String t=" ";
		for(int j=0;j<20;j++)
		{
			if(node[j].matches(ans))
			{
				t=temp[j];
			}
		}
		return t;
	}

	
	private static int connectparent(String ans,int cur_temp, BufferedWriter bw2) throws Exception {
		// TODO Auto-generated method stub
		String  op="";
		DatagramSocket server1=new DatagramSocket(null);
		int port=0;
		byte[] rcvData=new byte[1024];
		InetAddress ip_addr;
		for(int i=0;i<20;i++)
		{
			if(node[i].matches(ans))
			{
				String ip_temp[]=iport[i].split("/");
				ip_addr=InetAddress.getByName(ip_temp[0]);
				port=Integer.parseInt(ip_temp[1]);
				server1.setReuseAddress(true);
				server1=new DatagramSocket(port);
				sop("I am node"+ans);
				bw2.write("Current Node: "+ans);
				bw2.write("\n");
				sop("Connected to "+port);
				bw2.write("Running Port: "+port);
				bw2.write("\n");
				
				while(true)
				{
					
					sop("WAITING FOR PARENT TO CONNECT....");
					DatagramPacket rcvpacket=new DatagramPacket(rcvData,rcvData.length);
					server1.receive(rcvpacket);
					op=new String(rcvData);
					packet_stream=new String[op.trim().length()];
					temp_op2=String.valueOf(op.charAt(0));
					for(int j=0;j<20;j++)
					{
						if(temp_op2.matches(node[j]))
						{
							String ip_temp_p[]=iport[j].split("/");
							parent_ip=ip_temp_p[0];
							parent_port=Integer.parseInt(ip_temp_p[1]);
							bw2.write("Parent Connected");
							bw2.write("\n");
							sop("PARENT CONNECTED....");
							sop("Parent:"+temp_op2);
							sop("Parent IP"+parent_ip);
							sop("Parent Port"+String.valueOf(parent_port));
						}
					}
					flag=1;
					break;
				}
			}
			else
			{
				sop("NODE NOT FOUND EXCEPTION");
				
			}
		}
		sop("RECEIVED PACKET:");
		packet_stream=op.split(",");
		for(int i=0;i<packet_stream.length;i++)
		{
			sop(packet_stream[i]);
		}
		disp(packet_stream,temp_op2,ans,bw2,parent_ip,parent_port);
		det_child(packet_stream,temp_op2,ans,port,op,server1,cur_temp,bw2);
		
		for(int i=0;i<packet_stream.length;i++)
		{
			packet_stream[i]=null;
		}
		return flag;
		
		
	}

	

	private static void disp(String[] packet_stream2, String temp_op22,
			String ans2, BufferedWriter bw2, String parent_ip2, int parent_port2)throws Exception {
		// TODO Auto-generated method stub
		int index=0,t=0;
		for(int i=0;i<20;i++)
		{
			if(node[i].matches(ans2))
			{
				index=i;
			}
		}
		/*for(int i=0;i<20;i++)
		{
			bw2.write(adj[index][i]+"\t");
			bw2.write("\n");
		}*/
		bw2.write("ADJACENT NODES-NODES WITHIN SAME TRANSMISSION RANGE OF "+ans+": ");
		for(int i=0;i<20;i++)
		{
		
				if(adj[index][i]!=0&&t<20)
				{
					//adj_node[t]=node[i];
					bw2.write(adj_node[t]);
					bw2.write("\t");
					//sopnl("---->"+adj_node[t]);
					t++;
					
				}
		}
		bw2.write(temp_op22);
		bw2.write("\n");
		bw2.write("BROADCASTING DONE TO NEIGHBOURING NODES..."+"\n");
		bw2.write("\n");
		for(int i=0;i<packet_stream2.length;i++)
		{
			if(packet_stream2[i]!=null)
			{
				bw2.write(packet_stream2[i].trim());
				bw2.write(",");
			}
		}
		bw2.write("----->"+temp_op22);
		bw2.write("\n");
		for(t=0;t<adj_node.length;t++)
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
		for(int i=0;i<packet_stream2.length;i++)
		{
			if(packet_stream2[i]!=null)
			{
				bw2.write(packet_stream2[i].trim());
				bw2.write(",");
			}
		}
		bw2.write("<-----"+temp_op22);
		bw2.write("\n");
		for(t=0;t<adj_node.length;t++)
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
		bw2.write("\n");
		bw2.write("ACCEPTED PACKET FROM PARENT: "+temp_op22);
		bw2.write("\n");
		bw2.write("PACKET DISCARDED FROM: ");
		for(t=0;t<adj_node.length;t++)
		{
				if(adj_node[t]!=null)
				{
					bw2.write(adj_node[t]);
					bw2.write("\t");
				}
			
		}
		bw2.write("\n");
		bw2.write("Parent Connected");
		bw2.write("\n");
		sop("PARENT CONNECTED....");
		sop("Parent:"+temp_op2);
		bw2.write("Parent");
		bw2.write("\t");
		bw2.write("Parent IP");
		bw2.write("\t");
		bw2.write("Parent Port");
		bw2.write("\n");
		bw2.write(temp_op2);
		bw2.write("\t");
		bw2.write(parent_ip2);
		bw2.write("\t");
		bw2.write(String.valueOf(parent_port2));
		bw2.write("\n");
		bw2.write("----------------------------------------------------------------------------");
		bw2.write("\n");
		
	}

	private static void det_child(String[] packet_stream2,String temp_op2,String cur_node1,int port,String op,DatagramSocket server1,int cur_temp, BufferedWriter bw2) throws IOException {
		// TODO Auto-generated method stub
		int index=0;
		leaf=0;
		sop("NODE");
		for(int i=0;i<packet_stream2.length;i++)
		{
			if(packet_stream2[i].matches(cur_node1))
			{
				index=i;
			}
		}
		int t=0;
		int lc=0;
		int count_child=0;
		int fin=index+1;
		try
		{
			sop("Executing TRY CATCH");
			count_child=Integer.parseInt(String.valueOf(packet_stream2[index+1].trim()));
		}
		catch(NumberFormatException e)
		{
			lc=1;
		}
		if(lc==0)
		{
		count_child=Integer.parseInt(packet_stream2[index+1]);
		cur_children=new String[count_child];
		int end_index=index+count_child+1;
		sop("Number of children:"+count_child);
		for(int i=index+2;i<=end_index;i++)
		{
			if(t<=count_child)
			{
				cur_children[t]=packet_stream[i];
				t++;
			}
		}
		bw2.write("Children: ");
		bw2.write("\n");
		for(int i=0;i<cur_children.length;i++)
		{
			sop(cur_children[i]);
			bw2.write(cur_children[i]);
			bw2.write("\t");
			
		}
		
		bw2.write("\n");
		connectchild(cur_children,port,cur_node1,op,server1,temp_op2,cur_temp,leaf,bw2);
		}
		else
		{
			bw2.write("Leaf Node------No Children");
			bw2.write("\n");
			leaf=1;
			server1.close();
			query_dis(cur_node1,cur_temp,temp_op2,leaf,bw2);
		}
		
	}

	private static void connectchild(String[] cur_children2,int port,String ans,String op,DatagramSocket server1,String temp_op2,int cur_temp,int leaf, BufferedWriter bw2) throws IOException {
		// TODO Auto-generated method stub
		int t=0;	
		leaf=0;
		byte[] sendData1=new byte[1024];
		String sendData=ans+","+op;
		sendData1=sendData.getBytes();
		InetAddress ip_addr;
		for(int i=0;i<cur_children2.length;i++)
		{
				if(cur_children2[i]!=null)
				{
				for(int j=0;j<node.length;j++)
				{
					if(node[j].matches(cur_children2[i]))
					{
						String ip_temp[]=iport[j].split("/");
						ip_addr=InetAddress.getByName((ip_temp[0]));
						port=Integer.parseInt(ip_temp[1]);
						DatagramPacket packet=new DatagramPacket(sendData1,sendData1.length,ip_addr,port);
						server1.send(packet);
											
					}
				}
			}
		}
		bw2.write("Connected to Children");
		bw2.write("\n");
		server1.close();
		query_dis(ans,cur_temp,temp_op2,leaf,bw2);
		
	}

	private static void query_dis(String ans,int cur_temp2,
			String temp_op2,int leaf, BufferedWriter bw2) throws IOException {
		// TODO Auto-generated method stub
		String ip_port[]=new String[2];
		InetAddress ip_addr,ip;
		int count=0;
		int port1=0,port=0;
		for(int i=0;i<node.length;i++)
		{
			if(node[i].matches(ans))
			{
				ip_port=iport[i].split("/");
				ip=InetAddress.getByName(ip_port[0]);
				port1=Integer.parseInt(ip_port[1]);
			}
		}
		
		/*RECEIVE QUERY*/
		DatagramSocket server1=new DatagramSocket(port1);
		sop("PORT1"+port1);
		bw2.write("Query Rceived");
		bw2.write("\t");
		sop("CONNECTED TO:"+port1);
		byte sendData1[]=new byte[1024];
		byte rcvData_q[]=new byte[1024];
		byte rcvtemp[]=new byte[1024];
		String rec_query="";
		String rec_temp[]=new String[2];
		int time,time1;
		while(true)
		{
			DatagramPacket packet1=new DatagramPacket(rcvData_q,rcvData_q.length);
			server1.receive(packet1);
			rec_query=new String(rcvData_q);
			sop(rec_query.trim());
			bw2.write(rec_query.trim());
			bw2.write("\n");
			if(rec_query!=null)
			{
				break;
			}
		}
		server1.close();
		
		/*DISTRIBUTE QUERY INFO*/
		if(leaf==0)
		{
			DatagramSocket server2=new DatagramSocket(port1);
			sop("PORT1"+port1);
		for(int i=0;i<cur_children.length;i++)
		{
			if(cur_children[i]!=null)
			{
				count++;
			}
		}
		
		if(count>0)
		{
			bw2.write("Distributing Query to Children....");
			bw2.write("\n");
		while(true)
		{
		for(int i=0;i<cur_children.length;i++)
		{
			if(cur_children[i]!=null)
			{
				for(int j=0;j<node.length;j++)
				{
						if(cur_children[i].matches(node[j]))
						{
							
							sendData1=rec_query.getBytes();
							sop("Data sent"+rec_query.trim());
							String ip_temp[]=iport[j].split("/");
							ip_addr=InetAddress.getByName((ip_temp[0]));
							port=Integer.parseInt(ip_temp[1]);
							DatagramPacket packet22=new DatagramPacket(sendData1,sendData1.length,ip_addr,port);
							server2.send(packet22);
							bw2.write("Query "+rec_query.trim()+" Distributed to:");
							bw2.write("\t");
							bw2.write(cur_children[i]);
							bw2.write("\n");
							sop("QUERY SENT TO"+cur_children[i]);
						}
				}
			}
		}
		break;
		}
		//server2.close();
		}
		bw2.write("\n");
		try
		{
			String port_r="";
			String name_r="";
			bw2.write("Local Processing....");
			bw2.write("\n");	
				int t=0;
				rec_temp=new String[count];
				DatagramPacket packet44=new DatagramPacket(rcvtemp,rcvtemp.length);
				String ip_t[]=new String[2];
				
				while(true)
				{
					while(t<count)
					{
						server2.receive(packet44);
						rec_temp[t]=new String(rcvtemp).trim();
						sop(rec_temp[t]);
						port_r=String.valueOf(packet44.getPort());
						for(int i=0;i<node.length;i++)
						{
							ip_t=iport[i].split("/");
							if(ip_t[1].matches(port_r))
							{
								name_r=node[i];
							}
						}
						bw2.write(rec_temp[t]);
						bw2.write("\t");
						bw2.write("<------"+name_r);
						bw2.write("\n");
						t++;
					}
					break;
				}
				server2.close();
				new Thread(new node2(ans,cur_temp2,temp_op2,count,rec_query,rec_temp,bw2,port1)).start();
				
			}
			catch(Exception e)
			{
				sop(e);
			}
			
		}
		/*SEND LEAF NODE TEMPERATURE TO PARENT*/
		
		else if(leaf==1)
		{
		byte sendtemp[]=new byte[1024];
		sendtemp=String.valueOf(cur_temp2).getBytes();
		DatagramSocket server3=new DatagramSocket(port1);
		sop("PORT1"+port1);
		while(true)
		{
		for(int i=0;i<node.length;i++)
		{
			if(node[i].matches(temp_op2))
			{
				String ip_temp[]=iport[i].split("/");
				ip_addr=InetAddress.getByName((ip_temp[0]));
				port=Integer.parseInt(ip_temp[1]);
				DatagramPacket packet33=new DatagramPacket(sendtemp,sendtemp.length,ip_addr,port);
				server3.send(packet33);
				sop("TEMP SENT TO:"+temp_op2);
				bw2.write(cur_temp2+" Temperature Sent to Parent: "+temp_op2);
				bw2.write("\n");
				bw2.close();
			}
		}
		break;
		}
		
		server3.close();
		}
		
	}
	

	public static void findavg(String[] rec_temp, DatagramSocket server5,
			int cur_temp2, String temp_op2,int count, BufferedWriter bw2) throws IOException {
		// TODO Auto-generated method stub
		double sum=0,sum1=0;
		int length=rec_temp.length+1;
		double avg=0.0;
		int port2=0;
		InetAddress ip_addr;
		double sum2=0.0;
		byte sendres[]=new byte[1024];
		if(count>1)
		{
			for(int i=0;i<rec_temp.length;i++)
			{
				if(rec_temp[i]!=null)
				{
					sum2=Double.valueOf(rec_temp[i].trim());
					sum=sum+sum2;
				}
			}
			sum1=sum+cur_temp2;
		}
		else
		{
			sum1=sum+Double.valueOf(rec_temp[0].trim())+cur_temp2;
		}
		avg=sum1/length;
		bw2.write("Current node's temperature"+cur_temp2);
		bw2.write("\n");
		bw2.write("Total Children"+length);
		bw2.write("\n");
		bw2.write("Average Value Result: "+avg);
		bw2.write("\n");
		bw2.write("Temperature Sent to Parent : "+temp_op2);
		bw2.write("\n");
		sendres=String.valueOf(avg).getBytes();
		for(int i=0;i<node.length;i++)
		{
			if(node[i].matches(temp_op2))
			{
				String ip_temp[]=iport[i].split("/");
				ip_addr=InetAddress.getByName((ip_temp[0]));
				port2=Integer.parseInt(ip_temp[1]);
				DatagramPacket packet55=new DatagramPacket(sendres,sendres.length,ip_addr,port2);
				server5.send(packet55);
				sop(avg+"RESULT SENT TO:"+temp_op2);
			}
		}
		bw2.close();
	}

	public static void findmax(String[] rec_temp, DatagramSocket server5,
			int cur_temp2, String temp_op2,int count, BufferedWriter bw2) throws IOException {
		// TODO Auto-generated method stub
		int max=0;
		int port2=0;
		InetAddress ip_addr;
		byte sendres[]=new byte[1024];
		
		bw2.write("Received temperatures");
		bw2.write("\n");
		for(int i=0;i<rec_temp.length;i++)
		{
			bw2.write(rec_temp[i]);
			bw2.write("\n");
			sop(rec_temp[i]);
		}
		bw2.write("\n");
		if(count>1)
		{
			for(int i=0;i<rec_temp.length;i++)
			{
				if(rec_temp[i]!=null&&Integer.parseInt(rec_temp[i].trim())>max)
				{
					max=Integer.parseInt(rec_temp[i].trim());
				}
			}
			
		}
		
		else
		{
			max=Integer.parseInt(rec_temp[0].trim());
		}
		if(cur_temp2>max)
		{
			max=cur_temp2;
		}
		bw2.write("Current node's temperature"+cur_temp2);
		bw2.write("\n");
		bw2.write("Maximum Value Result: "+max);
		bw2.write("\n");
		bw2.write("Temperature Sent to Parent : "+temp_op2);
		bw2.write("\n");
		sendres=String.valueOf(max).getBytes();
		for(int i=0;i<node.length;i++)
		{
			if(node[i].matches(temp_op2))
			{
				String ip_temp[]=iport[i].split("/");
				ip_addr=InetAddress.getByName((ip_temp[0]));
				port2=Integer.parseInt(ip_temp[1]);
				DatagramPacket packet55=new DatagramPacket(sendres,sendres.length,ip_addr,port2);
				server5.send(packet55);
				sop(max+"RESULT SENT TO:"+temp_op2);
			}
		}
		bw2.close();
		
	}

	public static void findmin(String[] rec_temp, DatagramSocket server5,
			int cur_temp2,String temp_op2,int count, BufferedWriter bw2) throws IOException {
		// TODO Auto-generated method stub
		int min=Integer.MAX_VALUE;
		int port2=0;
		InetAddress ip_addr;
		byte sendres[]=new byte[1024];
		sop(Integer.parseInt(rec_temp[0].trim())+2);
		sop("----");
		bw2.write("Received temperatures");
		bw2.write("\n");
		for(int i=0;i<rec_temp.length;i++)
		{
			bw2.write(rec_temp[i]);
			bw2.write("\n");
			sop(rec_temp[i]);
		}
		bw2.write("\n");
		if(count>1)
		{
			for(int i=0;i<rec_temp.length;i++)
			{
				if(Integer.parseInt(rec_temp[i])<min)
				{
					min=Integer.parseInt(rec_temp[i]);
				}	
			}
		}
		else
		{
			min=Integer.parseInt(rec_temp[0]);
			
		}
		
		
		if(cur_temp2<min)
		{
			min=cur_temp2;
		}
		bw2.write("Current node's temperature"+cur_temp2);
		bw2.write("\n");
		bw2.write("Minimum Value Result: "+min);
		bw2.write("\n");
		bw2.write("Temperature Sent to Parent : "+temp_op2);
		bw2.write("\n");
		sop("MIN:"+min);
		sendres=String.valueOf(min).getBytes();
		for(int i=0;i<node.length;i++)
		{
			if(node[i].matches(temp_op2))
			{
				String ip_temp[]=iport[i].split("/");
				ip_addr=InetAddress.getByName((ip_temp[0]));
				port2=Integer.parseInt(ip_temp[1]);
				DatagramPacket packet55=new DatagramPacket(sendres,sendres.length,ip_addr,port2);
				server5.send(packet55);
				sop(min+"RESULT SENT TO:"+temp_op2);
			}
		}
		bw2.close();
		
	}

	private static void parsetop(String filename) {
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
		/*	 for(int j=0;j<20;j++)
			     {
			    	 sop("Node:"+node[j]);
			    	 sop("X:"+x[j]);
			    	 sop("Y:"+y[j]);
			    	 sop("iport:"+iport[j]);
			    	 sop("temp:"+temp[j]);
			    	 sop("range:"+range[j]);
			    	 sop("sink:"+sink[j]);
			     }*/
			
			
			
		}
	

	

	private static void parse(String filename) {
		// TODO Auto-generated method stub
		FileInputStream fi;
		Scanner s1,s2;
		File file = new File(filename);
		
		int i=0;
		        try {
        	
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter("-->");
            
            while (scanner.hasNextLine()&&i<20) {
            parent[i]=scanner.next();
            children[i]=scanner.next();
            weight[i]=scanner.nextLine();
            i++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}

}
class node2 implements Runnable
{

public static String ans1;
public static String rec_temp1[];
public static String temp_op3;
public static int count1;
public static String rec_query1;
public static int port2;
public static int cur_temp3;
public static DatagramSocket server44;
public static DatagramPacket packet44;
public static int t1;
public static String trim1;
public static BufferedWriter bw3;
public static byte[] rcvtemp=new byte[1024];
public static void sop(Object s1)
{
	System.out.println(String.valueOf(s1));
}
public node2(String ans, int cur_temp, String temp_op2, int count,
		String rec_query, String[] rec_temp2, BufferedWriter bw2, int port1) {
	// TODO Auto-generated constructor stub

	ans1=ans;
	//cur_temp1=cur_temp;
	temp_op3=temp_op2;
	count1=count;
	rec_query1=rec_query;
	//port2=port1;
	cur_temp3=cur_temp;
	//time1=time;
	//server44=server4;
	rec_temp1=rec_temp2;
	bw3=bw2;
	port2=port1;

}
public void run()
{
	for(int i=0;i<rec_temp1.length;i++)
	{
		sop(rec_temp1[i]);
	}
	node1 t2=new node1();
	sop("ANS"+ans1);
	
	/*PERFORM LOCAL PROCESSING*/
	
	/*RECEIVE TEMPERATURE*/
	
	try
	{

		
	//Thread.yield();
	DatagramSocket server5=new DatagramSocket(port2);
	
	while(true)
	{
		if(rec_query1.trim().matches("min"))
		{
			bw3.write("Finding Minimum....");
			bw3.write("\n");
			t2.findmin(rec_temp1,server5,cur_temp3,temp_op3,count1,bw3);
		}
		else if(rec_query1.trim().matches("max"))
		{
			bw3.write("Finding Maximum.....");
			bw3.write("\n");
			t2.findmax(rec_temp1,server5,cur_temp3,temp_op3,count1,bw3);
		}
		else if(rec_query1.trim().matches("avg"))
		{
			bw3.write("Finding Average....");
			bw3.write("\n");
			t2.findavg(rec_temp1,server5,cur_temp3,temp_op3,count1,bw3);
		}
		
		break;
	}
	server5.close();
	}
	
	catch(Exception e)
	{
		sop(e);
	}
	

}
}
