import java.text.*;
import java.util.Date; 
public class AccountDemo {

	
	class Account
	{
		private int id; 
		private double balance; 
		private double annualInterestRate; 
		private Date dateCreated; 
	
	Account()	
	{
		id = 0 ; 
		balance = 0 ; 
		annualInterestRate = 0 ; 
		dateCreated= new Date(); 
	}
	
	Account(int ID, double BALANCE)
	{
		id = ID;
		balance = BALANCE; 
		dateCreated = new Date(); 
	}
		public int getId() 
		{
			
			return id;
		}
		
		public double getBalance()
		{
			
			return balance; 
		}
		public double getAnnualInterestRate()
		{
			return annualInterestRate; 
		}
		public void setId(int id)
		{
			this.id = id ;
		}
		public void setBalance(double balance)
		{
			this.balance = balance ;
		}
		public void setAnnualInterestRate(double annualInterestRate)
		{
			this.annualInterestRate = annualInterestRate; 
		}
		public Date getDateCreated()
		{
			return dateCreated; 
		}
		public double getMonthlyInterestRate()
		{
			return annualInterestRate/12.0; 
		}
		public void withdraw(double amount)
		{
			balance = balance - amount; 
		}
		public void deposit(double amount)
		{
			balance = balance + amount; 
		
	}
	
	class AccountProg
	{
	public void main(String[] args)
	{
		DecimalFormat df = new DecimalFormat("$$.$$");
		Account account = new Account(1122,2000);
		account.setAnnualInterestRate(4.5);
		account.withdraw(2500);
		account.deposit(3000); 
		System.out.println("Balance is :" + df.format(account.getBalance()));  
		System.out.println("Monthly Interest is: "  + df.format(account.getMonthlyInterestRate()));
		System.out.println("Account created on: " + account.getDateCreated());
	}
	}
	}}
	

	
