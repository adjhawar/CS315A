import java.io.*;
import java.sql.*;
import java.util.*;
  
class Reservation_System{ 
	public static int userid;
	public static String username;

	int login() throws IOException{
		String password;
		BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
		System.out.println("Welcome to the login page");
		System.out.println("Enter your userid and password");
		System.out.print("UserID:");
		userid=Integer.parseInt(br.readLine());
		System.out.print("Password:");
		password=br.readLine();
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation_system","root","root");  
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("select name from users where id = "+userid+" and password = "+password);
			rs.next();			
			username=rs.getString(1);
			con.close();  
			return 1;
		}catch(Exception e){ System.out.println("Unknown user. Please register before you login.");
			return 0;}  
	}

	int register() throws IOException{
		String password;
		BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
		System.out.println("Welcome to the registration page");
		System.out.println("Enter your userid, name and password in that order");
		System.out.print("UserID:");
		userid=Integer.parseInt(br.readLine());
		System.out.print("Name:");
		username=br.readLine();
		System.out.print("Password:");
		password=br.readLine();
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con1=DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation_system","root","root");  
			String query = " insert into users values (?, ?, ?)";
			PreparedStatement preparedStmt = con1.prepareStatement(query);
			preparedStmt.setInt (1,userid );
			preparedStmt.setString (2, username);
			preparedStmt.setString (3, password);
			preparedStmt.execute();
			con1.close();  
			return 1;
		}catch(Exception e1){
			System.out.println("UserID already exists.Please choose another userID");
			return 0;}  
	}

	int get_seats(int train_id)
	{
		int s;		
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation_system","root","root");  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select seats from train where id ="+ train_id);  
			rs.next();			
			s=rs.getInt(1);
			con.close(); 
			return s; 
		}catch(Exception e){ System.out.println("Enter a valid train number");
			return -1;} 
	}

	int get_fare(int train_id)
	{
		int f;		
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation_system","root","root");  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select fare from train where id = "+train_id);  
			rs.next();						
			f=rs.getInt(1);
			con.close();
			return f;
		}catch(Exception e){ System.out.println("Enter a valid train id");
			return 0;} 
	}  

	int get_directFare(String src, String dest)
	{
		int f;
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation_system","root","root");  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select fare from train where src = ( select code from stations where name = \""+src+"\") and dest = (select code from stations where name = \"" + dest + "\")");  
			rs.next();
			f=rs.getInt(1);
			con.close(); 
			return f; 
		}catch(Exception e){ System.out.println("Sorry, there are no stations with the given input name");
			return 0;} 
	}

	void get_train(String src, String dest)
	{
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation_system","root","root");  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select id,name from train where src = ( select code from stations where name = \""+src+"\") and dest =  (select code from stations where name = \"" + dest + "\")"); 
			System.out.println("Train ID, Train Name");
			while(rs.next()){
				System.out.println(rs.getInt(1)+","+rs.getString(2));}  
			con.close();  
		}catch(Exception e){ System.out.println("Sorry, there are no trains between these two stations");} 
	}
 
	void cancel(int pnr)
	{
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation_system","root","root");  
			Statement stmt=con.createStatement();  
			String query = "delete from ticket where pnr=?";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			preparedStmt.setInt (1,pnr); 
			preparedStmt.execute();
			con.close();
			System.out.println("Ticket successfully cancelled");
		}catch(Exception e){ System.out.println("Invalid PNR");} 
	}
	
	int book(int tid,String src,String dest,int seats)
	{
		int avl_seats,ticket=0;
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation_system","root","root");  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select seats from train where id ="+tid+" and src=\""+src+"\" and dest=\""+dest+"\"");
			rs.next();
			avl_seats=rs.getInt(1);
			con.close();  
		}catch(Exception e){ 
			System.out.println("Sorry, there are no trains with given id or the train does not run between given stations. Please check the trains between stations to select your train");
			return 0;}
		if(avl_seats<seats){
			System.out.println("Available seats are less than the required number of seats. Please check seat availability before booking");
			return 0;
		}
		else
			avl_seats=avl_seats-seats;
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con1=DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation_system","root","root");  
			String query = " update train set seats=? where id=?";
			PreparedStatement preparedStmt = con1.prepareStatement(query);
			preparedStmt.setInt (1,avl_seats );
			preparedStmt.setInt (2, tid);
			preparedStmt.execute();
			String query2="insert into ticket values(?,?,?,?,?,?,?)";
			PreparedStatement preparedStmt1 = con1.prepareStatement(query2);
			preparedStmt1.setInt (1, 0);
			preparedStmt1.setInt (2, tid );
			preparedStmt1.setString (3, src);
			preparedStmt1.setString (4, dest);
			preparedStmt1.setString (5, username);
			preparedStmt1.setInt (6, seats);
			preparedStmt1.setInt (7, seats*get_fare(tid));
			preparedStmt1.execute();
			ResultSet rs=preparedStmt1.getGeneratedKeys();
			rs.next();
			ticket=rs.getInt(1);
			con1.close();  
			return ticket;
		}catch(Exception e1){
			System.out.println("Ticket creation failed");
			return 0;} 
	}

	void display(int pnr)
	{
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation_system","root","root");  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select * from ticket where pnr =" + pnr);
			rs.next();
			System.out.println(rs.getInt(1)+","+rs.getInt(2)+","+rs.getString(3)+","+rs.getString(4)+","+rs.getString(5)+","+rs.getInt(6)+","+rs.getInt(7));  
			con.close();  
		}catch(Exception e){ System.out.println("Invalid PNR");} 
	}

	public static void main(String args[]) throws IOException{  
		int choice=1,x=0,choice2=0;
		int tid,seats,fare,pnr;
		String src,dest;
		BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
		Reservation_System obj=new Reservation_System();
		System.out.println("Welcome to the Rail Reservation Portal");
		while(choice==1 || choice==2){
			System.out.println("Enter choice");
			System.out.println("1.Login");
			System.out.println("2.Register");	
			System.out.println("Any other number to exit the portal");
			choice=Integer.parseInt(br.readLine());
			if(choice==1)
				x=obj.login();
			else if(choice==2)
				x=obj.register();
			else
				choice=3;
			while(x==1){
				System.out.println("Welcome "+username+"!!!");
				System.out.println("1.Trains between stations");
				System.out.println("2.Seat Availability");
				System.out.println("3.Calculate your journey fare");
				System.out.println("4.Ticket reservation");
				System.out.println("5.Ticket Cancellation");
				System.out.println("6.Ticket Details");
				System.out.println("Press any other number to Logout");
				choice2=Integer.parseInt(br.readLine());
				switch(choice2){
					case 1: System.out.print("Enter the source and estination station name.\nSource:");
						src=br.readLine();
						System.out.print("Destination:");
						dest=br.readLine();
						obj.get_train(src,dest);
						break;
					case 2: System.out.println("Enter the train id");
						tid=Integer.parseInt(br.readLine());
						seats=obj.get_seats(tid);
						if(seats>=0)
							System.out.println("Available seats on train id:"+tid+" is "+seats);
						else
							System.out.println("No available seats on train id:"+tid); 
						break;
					case 3: System.out.print("Enter the source and estination station name.\nSource:");
						src=br.readLine();
						System.out.print("Destination:");
						dest=br.readLine();
						System.out.println("Enter the number of seats");
						seats=Integer.parseInt(br.readLine());
						fare=obj.get_directFare(src,dest);
						if(fare>0)
							System.out.println("Your fare is:"+fare*seats);
						break;
					case 4: System.out.println("Enter the train id");
						tid=Integer.parseInt(br.readLine());
						System.out.println("Enter the number of seats");
						seats=Integer.parseInt(br.readLine());
						System.out.print("Enter the source and estination station name.\nSource:");
						src=br.readLine();
						System.out.print("Destination:");
						dest=br.readLine();
						pnr=obj.book(tid,src,dest,seats);
						if(pnr!=0)
							System.out.println("Ticket successfully booked. PNR is:"+pnr);
						break;
					case 5: System.out.println("Enter the ticket PNR");
						pnr=Integer.parseInt(br.readLine());
						obj.cancel(pnr);
						break;
					case 6: System.out.println("Enter the ticket PNR");
						pnr=Integer.parseInt(br.readLine());
						obj.display(pnr);
						break;
					default: System.out.println("Logging out "+username);
						 username="";
						 choice=1;
						 x=0;
						 break;
				}}
			if(choice==3)
				System.out.println("Good Bye!!!");
		}
	}
 }

