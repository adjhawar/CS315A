import java.sql.*;  
import java.lang.*;

class MysqlCon{  
public static void main(String args[]){  
try{  
Class.forName("com.mysql.jdbc.Driver");  
Connection con=DriverManager.getConnection(  
"jdbc:mysql://localhost:3306/reservation_system","root","root");  
//here sonoo is database name, root is username and password  
Statement stmt=con.createStatement();  
ResultSet rs=stmt.executeQuery("select * from users");  
while(rs.next())  
System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));  
con.close();  
}catch(Exception e){ System.out.println(e);}  
}
  
int get_seats(int train_id)
{
try{  
Class.forName("com.mysql.jdbc.Driver");  
Connection con=DriverManager.getConnection(  
"jdbc:mysql://localhost:3306/reservation_system","root","root");  
//here sonoo is database name, root is username and password  
Statement stmt=con.createStatement();  
ResultSet rs=stmt.executeQuery("select seats from train where id = %d",train_id);  
return rs.getInt(1);
con.close();  
}catch(Exception e){ System.out.println(e);} 
}

int get_fare(int train_id)
{
try{  
Class.forName("com.mysql.jdbc.Driver");  
Connection con=DriverManager.getConnection(  
"jdbc:mysql://localhost:3306/reservation_system","root","root");  
//here sonoo is database name, root is username and password  
Statement stmt=con.createStatement();  
ResultSet rs=stmt.executeQuery("select fare from train where id = %d",train_id);  
return rs.getInt(1);
con.close();  
}catch(Exception e){ System.out.println(e);} 
}  

int get_train(String src, String dest)
{
try{  
Class.forName("com.mysql.jdbc.Driver");  
Connection con=DriverManager.getConnection(  
"jdbc:mysql://localhost:3306/reservation_system","root","root");  
//here sonoo is database name, root is username and password  
Statement stmt=con.createStatement();  
ResultSet rs=stmt.executeQuery("select id from train where src = %s && dest = %s",src,dest);  
return rs.getInt(1);
con.close();  
}catch(Exception e){ System.out.println(e);} 
}  
}  
