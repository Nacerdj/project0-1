# project0-1
bankapp 
# project0
explantion for my project bankapp
my first step was to designe a plan what we need for our application or app to implement in our classes and see what we have as constaints where you can see it in the following diagram
![Blank diagram](https://user-images.githubusercontent.com/89362530/168853550-9dea9939-5bcf-4256-8897-78b450a23e17.svg)

as you see the diagrame we came with

1-class driver:

 this main class holds the main method for our  program which controles the entire applciation and  execute the application and we take input from user 
              ( username, password, role) from the scanner where the role will diplay
              
 1-1  based on the user role we show respective menu items
 
 1-2 based on the menu selected we call respective methods in BankServices class
  
 2-class BankServices:
 
 in this class we write the methods for all requirements needed for the bank services we do have here
 2-1 method validateUser which do verification for username, password and role are correct or not 
 2-1 method createAccount will raise and create account request with rondum number it /generate 8 digits 
 2-3 method viewBalance  -- this method will show balance of given account number
 
 2-4 method  withDrawal  -- this method is used to withdraw requested amount from account by following steps
 
 2-4-1- we validate the account number 
 
 2-4-2- if given account number is doest not exist or wrong we stop the program through throwing exception
 
 2-4-3- if given account number is correct then we proceed in this step we have to check the requested Amount if is correct or not with a simple logique as if                 and                
              else
             if    requestedAmount < 0 or requestedAmount > existingBalance
             else  then also we reject
             else   otherwise we proceed
  2-5  method depoist  -- to add money to an account 
  2-6  method postMoneyTransfer -- will raise money transfer request from one accoun to another account
  2-7  method acceptMoneyTransfer -- will accept money transfer after validating the accounts and then complete transaction
  2-8  method viewAllTransactions -- to view all transactions
  2-9  method viewCusomerAccount  -- to display customer account details
  2-10  method approveOrReject  -- to approve or reject in this step we are using 101 as key for approval and 102 as key for reject
  
  3 class BankDao.java:
  
    in this class we communicate with database to perform operations on our SQL database which goes from the Driver class to bankServices class then to BankDao class
    also we have our methods here to porforme defirent taks 
    3-1 method validateUser -- this method  through sql query we check in database whether we have record or not
    3-2 method getAccountDetails --  this method through sql query to get account information from database
    3-3 method createNewAccount --   this method through sql query to insert and create account record in database
    3-4 method isAccountExisted  --  this method through sql query to check whether account is present in database or not
    3-5 method withDrawOrDeposit --  this method through sql query based on operation we perform whether withdraw or deposit
    3-6 method postTransfer --       this method through sql query to insert record into pending transactions table
    3-7 method acceptTransfer --     this method through sql query will read records from pending transaction table and completes transaction
    3-8 method displayTransactions -- this method through sql query will read transaction table and display all transactions information
    
    4 class Account:
    hold all properties of Account class for storing records as objects.
    where our account has (int accountNumber, int amount, int userId, String status)
    
    5 class AccountDoesNotExist:
    we use this class to through exception on RuntimeException which occurs when we enter invalid accoutn number we print out text  
    
    ("Sorry !! No Account existed with given account number")
    
    6  class Constants:
       we difinded our constants(EMPLOYEE,CUSTOMER,SUCCESS,FAIL,WITHDRAW, DEPOSIT,PENDINGTOAPPROVE,APPROVED,REJECTED) 
       we holded all our constant in this enum class  instead of  repiting coding in all places
       
      7 class Transaction:
         creates transactions and send it to pending 
      
      8 class PendingTransaction.java:
        is waiting to give approve or reject from emplyee  to change status for this  transaction created 
       
       
       9 class UserNotExistException 
       in a loogin process if you give invalida user name and password then this occur
       
       10 class Utility:
       from this program we get connection to database java.sql.Connection
       
       
       
       
       
 #SQL Tables  we created 2 script to let us keep records on our database
 
 1 script 
       we create this tow tables 
       create table user(
   userId integer primary key,
   userName varchar(50),
   password varchar(50),
   role varchar(50)   
);


we insert on it this values to test our database and if is connected to our application one as customer and other one as employee 


insert into user values(3, 'ibrahim', 'java', 'customer');
insert into user values(4, 'tomtom', 'sql', 'employee');
nsert into user values (1, 'nacer', 'java', 'customer');
insert into user values(2, 'john', 'sql', 'employee');



then 

create table account(
   accountNumber integer primary key,
   amount integer,
   userId integer,
   status varchar(60),
   foreign key account(userId) references user(userId)
);


we tested it with this values 


insert into account values (1111, 10, 1);
insert into account values(2222, 20, 2);

 2 script 
 
 
    create table transaction(   
    accountNumber integer,
    amount integer,
    operation varchar(40),
     status varchar(40)
);



    create table pendingTransactions(    
    senderAccountNumber integer,
    receiverAccountNumber integer,
    amount integer
);
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
