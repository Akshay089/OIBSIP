import java.sql.*;
import java.util.Scanner;
import java.util.Random;

public class Task1
{
	private static final int min=1000;
	private static final int max=9999;//limit values for random pnr number
	
	public static class user{//This class is to take inputs from user
		private String username;
		private String password;

		Scanner sc=new Scanner(System.in);
		
		public user(){//Empty Constructor
		}
		public String getUsername(){
			System.out.println("Enter Username:");
			username=sc.nextLine();
			return username;
		}
		public String getPassword(){
			System.out.println("Enter Password:");
			password=sc.nextLine();
			return password;
		}
	}//End of class user
	public static class pnrRecord{
		private int pnrNumber;
		private String passengerName;
		private String trainNumber;
		private String classType;
		private String journeyDate;
		private String from;
		private String to;

		Scanner sc=new Scanner(System.in);

		public int getPnrNumber(){
			Random random=new Random();
			pnrNumber=random.nextInt(max)+min;
			return pnrNumber;
		}
		public String getPassengerName(){
			System.out.println("Enter the passenger name:");
			passengerName=sc.nextLine();
			return passengerName;
		}
		public String getTrainNumber(){
			System.out.println("Enter the Train Number:");
			trainNumber=sc.nextLine();
			return trainNumber;
		}
		public String getClassType(){
			System.out.println("Enter the ClassType:");
			classType=sc.nextLine();
			return classType;
		}
		public String getJourneyDate(){
			System.out.println("Enter the Journey Date as'YYYY-MM-DD'format:");
			journeyDate=sc.nextLine();
			return journeyDate;
		}
		public String getFrom(){
			System.out.println("from:");
			from=sc.nextLine();
			return from;
		}
		public String getTo(){
			System.out.println("To:");
			to=sc.nextLine();
			return to;
		}
	}
	public static void main(String args[])
	{
		Scanner sc = new Scanner(System.in);

		user u1=new user();	
		String username=u1.getUsername();
		String password=u1.getPassword();

		String url="jdbc:mysql://localhost:3306/akshay";

		try{

			Class.forName("com.mysql.cj.jdbc.Driver");

			try(Connection connection = DriverManager.getConnection(url, username, password)){
				System.out.println("User Connection Granted.\n");
				while(true)
				{
					String InsertQuery="insert into reservations values(?, ?, ?, ?, ?, ?, ?)";
					String DeleteQuery="DELETE FROM reservations WHERE pnrNumber = ?";
					String ShowQuery="Select * from reservations";

					System.out.println("Enter the choice: ");
					System.out.println("1. Insert Record.\n");
					System.out.println("2. Delete Record.\n");
					System.out.println("3. Show All Records.\n");
					System.out.println("4. Exit.\n");
					int choice=sc.nextInt();

					if(choice==1)
					{
						pnrRecord pl=new pnrRecord();
						int pnrNumber=pl.getPnrNumber();
						String passengerName=pl.getPassengerName(); 
						String trainNumber=pl.getTrainNumber();
						String classType=pl.getClassType();
						String journeyDate=pl.getJourneyDate();
						String from=pl.getFrom();
						String to=pl.getTo();

						try(PreparedStatement preparedStatement =connection.prepareStatement(InsertQuery)){

							preparedStatement.setInt(1, pnrNumber);
							preparedStatement.setString(2, passengerName);
							preparedStatement.setString(3,trainNumber);
							preparedStatement.setString(4,classType);
							preparedStatement.setString(5,journeyDate);
							preparedStatement.setString(6,from);
							preparedStatement.setString(7,to);

							int rowsAffected=preparedStatement.executeUpdate();
							if(rowsAffected>0)
							{
								System.out.println("Record added sucessfully.");
							}
							else{
								System.out.println("No Records were added.");
							}

						}

						catch(SQLException e)
						{
							System.out.println("SQLEXCEPTION:"+e.getMessage());
						}
					}

					else if(choice==2)
					{
						System.out.println("Enter the PNR number to delete the record:");
						int pnrNumber=sc.nextInt();
						try(PreparedStatement preparedStatement=connection.prepareStatement(DeleteQuery))
						{
							preparedStatement.setInt(1, pnrNumber);
							int rowsAffected=preparedStatement.executeUpdate();

							if(rowsAffected>0){
								System.out.println("Record deleted successfully.");
							}

							else{
								System.out.println("No records were deleted");
							}
						}
						catch(SQLException e)
						{
							System.out.println("SQLEXCEPTION:"+e.getMessage());
						}
					}

					else if(choice==3)
					{
						try(PreparedStatement preparedStatement=connection.prepareStatement(ShowQuery);
							ResultSet resultSet=preparedStatement.executeQuery()){
								System.out.println("\nAll records printing.\n");
								while(resultSet.next()){
									int pnrNumber=resultSet.getInt("pnrNumber");
									String passengerName=resultSet.getString("passengerName");
									String trainNumber=resultSet.getString("trainNumber");
									String classType=resultSet.getString("classType");
									String journeyDate=resultSet.getString("journeyDate");
									String from=resultSet.getString("from");
									String to=resultSet.getString("to");

									System.out.println("PNR Number:"+pnrNumber);
									System.out.println("Passenger Name:"+passengerName);
									System.out.println("Train Number:"+trainNumber);
									System.out.println("Class Type:"+classType);
									System.out.println("Journey Date:"+journeyDate);
									System.out.println("From Location:"+from);
									System.out.println("To Location:"+to);
									System.out.println("-------------------");
								}
							}
							catch(SQLException e)
							{
								System.out.println("SQLEXCEPTION:"+e.getMessage());
							}

					}
					else if(choice==4){
						System.out.println("Exiting the Program.\n");
						break;
					}
					else{
						System.out.println("Invalid choice Entered!!!\n");
					}
				}
			}

			catch(SQLException e){
				System.out.println("SQLEXCEPTION:"+e.getMessage());
			}
		}
		catch(ClassNotFoundException e){
			System.out.println("Error loading JDBC Driver"+e.getMessage());
		}
		sc.close();
	}

};
