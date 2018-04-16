
import java.sql.*;
import java.util.*;
  
class Reservation_System{ 
	public static final Scanner sc=new Scanner(System.in);
	public static int userid;
	public static String username;

	int login(){
		String password;
		System.out.println("Welcome to the login page");
		System.out.println("Enter your userid and password");
		System.out.print("UserID:");
		userid=sc.nextInt();
		System.out.print("Password:");
		password=sc.next();
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation_system","root","root");  
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from users where id = "+userid+" && password = "+password);
			username=rs.getString(2);  
			con.close();  
			return 1;
		}catch(Exception e){ System.out.println("Unknown user. Please register before you login.");
			return 0;}  
	}

	int register(){
		String password;
		System.out.println("Welcome to the registration page");
		System.out.println("Enter your userid, name and password");
		System.out.print("UserID:");
		userid=sc.nextInt();
		System.out.print("Name:");
		username=sc.nextLine();
		System.out.print("Password:");
		password=sc.next();
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
			Connection con=DriverManager.getConnection(  
					"jdbc:mysql://localhost:3306/reservation_system","root","root");  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select seats from train where id ="+ train_id);  
			con.close(); 
			return rs.getInt(1); 
		}catch(Exception e){ System.out.println(e);
			return 0;} 
	}

	int get_fare(int train_id)
	{
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation_system","root","root");  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select fare from train where id = "+train_id);  
			con.close();  
			return rs.getInt(1);
		}catch(Exception e){ System.out.println(e);
			return 0;} 
	}  

	void get_train(String src, String dest)
	{
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation_system","root","root");  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select id from train where src =" + src + " and dest = " + dest);  
			con.close();  
		}catch(Exception e){ System.out.println("Sorry, there are no trains between these two stations");} 
	}
 
	public static void main(String args[]){  
		int choice=1,x=0,choice2;
		Reservation_System obj=new Reservation_System();
		System.out.println("Welcome to the Rail Reservation Portal");
		while(choice==1 || choice==2){
			System.out.println("Enter choice");
			System.out.println("1.Login");
			System.out.println("2.Register");	
			System.out.println("Any other number to exit the portal");
			choice=sc.nextInt();
			if(choice==1)
				x=obj.login();
			else if(choice==2)
				x=obj.register();			//register();
			else
				System.out.println("Good Bye!!!");
			if(x==1){
				System.out.println("Welcome "+username+"!!!");
				System.out.println("1.Trains between stations");
				System.out.println("2.Seat Availability");
				System.out.println("3.Calculate your journey fare");
				System.out.println("4.Ticket reservation");
				System.out.println("5.Ticket Cancellation");
				System.out.println("Press any other number to logout");
				choice2=sc.nextInt();
				switch(choice2){
					case 1: System.out.print("Enter the source and estination station name.\nSource:");
						String src=sc.nextLine();
						System.out.print("Destination:");
						String dest=sc.nextLine();
						obj.get_train(src,dest);
						break;
					case 2:
						break;
					case 3:
						break;
					case 4:
						break;
					case 5:
						break;
					default: choice=3;
						break;
			}}
			else if(x==0)
				continue;
		}
	}
 }

