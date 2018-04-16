import java.sql.*;
import java.util.*;
  
class Reservation_System{ 
 	public static final Scanner sc=new Scanner(System.in);
	public static String username;

	int login(){
		String password;
		System.out.println("Enter your username and password");
		System.out.print("Username:");
		username=sc.next();
		System.out.print("Password:");
		password=sc.next();
		
		return 1;
}
	public static void main(String args[]){  
		int choice,x;
		Reservation_System obj=new Reservation_System();
		System.out.println("Enter choice");
		choice=sc.nextInt();		//stores whether user is new or old
		if(choice==1)
			x=obj.login();
		else if(choice==2)
			x=2;			//register();
		else
			System.out.println("Invalid choice");
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation_system","root","root");  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select * from users");  
			while(rs.next())  
				System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));  
			con.close();  
		}catch(Exception e){ System.out.println(e);}  
	}  
}  
