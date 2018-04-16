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
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation_system","root","root");  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select seats from train where id ="+ train_id);  
			con.close(); 
			rs.next();			
			return rs.getInt(1); 
		}catch(Exception e){ System.out.println("Enter a valid train number");
			return -1;} 
	}

	int get_fare(int train_id)
	{
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation_system","root","root");  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select fare from train where id = "+train_id);  
			con.close();  
			rs.next();						
			return rs.getInt(1);
		}catch(Exception e){ System.out.println("Enter a valid train id");
			return 0;} 
	}  

	void get_train(String src, String dest)
	{
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation_system","root","root");  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select id,name from train where src =" + src + " and dest = " + dest);
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
			String query = " delete from tickets where pnr=?";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			preparedStmt.setInt (1,pnr); 
			con.close();
			System.out.println("Ticket successfully cancelled");
		}catch(Exception e){ System.out.println("Invalid PNR");} 
	}
	
	int book(int tid,String src,String dest,int seats)
	{
		int avl_seats,ticket;
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation_system","root","root");  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select seats from train where id ="+tid+" and src="+src+" dest="+dest);
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
			query="insert into tickets values(?,?,?,?,?,?)";
			preparedStmt = con1.prepareStatement(query);
			preparedStmt.setInt (1, tid );
			preparedStmt.setString (2, src);
			preparedStmt.setString (3, dest);
			preparedStmt.setString (4, username);
			preparedStmt.setInt (5, seats);
			preparedStmt.setInt (6, seats*get_fare(tid));
			preparedStmt.execute();
			ResultSet rs=preparedStmt.getGeneratedKeys();
			rs.next();
			con1.close();  
			return rs.getInt(1);
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
			ResultSet rs=stmt.executeQuery("select * from tickets where pnr =" + pnr);
			rs.next();
			System.out.println(rs.getInt(1)+","+rs.getString(2));  
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
					case 3: System.out.println("Enter the train id");
						tid=Integer.parseInt(br.readLine());
						System.out.println("Enter the number of seats");
						seats=Integer.parseInt(br.readLine());
						fare=obj.get_fare(tid);
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
					default: System.out.println("Loggind out "+username);
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

