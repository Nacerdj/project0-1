package com.revature;

import com.mysql.cj.x.protobuf.MysqlxPrepare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankDao {

    private Connection con;
    public BankDao(){
        con = new Utility().getConnection();
    }

   public int  validateUser(String userName, String password, String role){
        String query = "select userId from user where userName = ? and password = ? and role=?";
        int userId = 0;
        try {
            PreparedStatement pStmtObj = con.prepareStatement(query);
            pStmtObj.setString(1, userName);
            pStmtObj.setString(2, password);
            pStmtObj.setString(3, role);

            ResultSet rsObj = pStmtObj.executeQuery();
            rsObj.next();
            userId = rsObj.getInt(1);
        }catch(SQLException ex){
          userId = 0;
        }
       return userId;
    }

    public Account getAccountDetails(int userId){
          String query = "select * from account where userId = ?";
          try {
              PreparedStatement pStmtObj = con.prepareStatement(query);
              pStmtObj.setInt(1,userId);
              ResultSet rsObj = pStmtObj.executeQuery();
              rsObj.next();
              Account account = new Account();
                account.setAccountNumber(rsObj.getInt(1));
                account.setAmount(rsObj.getInt(2));
                account.setUserId(rsObj.getInt(3));
                return account;
          }catch(SQLException ex){
                return null;
          }
    }

    public int createNewAccount(Account account){
        String query = "insert into account values (?, ?, ?, ?)";
        try {
            PreparedStatement pStmt = con.prepareStatement(query);
            pStmt.setInt(1,account.getAccountNumber());
            pStmt.setInt(2, account.getAmount());
            pStmt.setInt(3,account.getUserId());
            pStmt.setString(4, account.getStatus());
          return  pStmt.executeUpdate();
        }catch(SQLException ex){
            System.out.println("Exception while creating account "+ex.getMessage());
            return 0;
        }
    }
    public Account isAccountExisted(int accountNumber){
        String query = "select * from account where accountNumber = ? and status = '"+Constants.APPROVED.toString()+"'";
        try {
            PreparedStatement pStmtObj = con.prepareStatement(query);
            pStmtObj.setInt(1,accountNumber);
            ResultSet rsObj = pStmtObj.executeQuery();
            rsObj.next();
            Account account = new Account();
            account.setAccountNumber(rsObj.getInt(1));
            account.setAmount(rsObj.getInt(2));
            account.setUserId(rsObj.getInt(3));
            account.setStatus(rsObj.getString(4));
            return account;
        }catch(SQLException ex){
            return null;
        }
    }
  public int withDrawOrDeposit(Account account, int amount, String operation){
        if(operation.equalsIgnoreCase(Constants.WITHDRAW.toString()))
            account.setAmount(account.getAmount() - amount);
        else if(operation.equalsIgnoreCase(Constants.DEPOSIT.toString()))
            account.setAmount(account.getAmount() + amount);
        String query = "update account set amount = ? where accountNumber = ?";
      try {
          PreparedStatement pStmt = con.prepareStatement(query);
          pStmt.setInt(1,account.getAmount());
          pStmt.setInt(2, account.getAccountNumber());
          int result =  pStmt.executeUpdate();
          if(result != 0){
              updateTransaction(new Transaction(account.getAccountNumber(), amount, operation, Constants.SUCCESS.toString() ));
          }
          return result;
      }catch(SQLException ex){
          updateTransaction(new Transaction(account.getAccountNumber(), amount, operation, Constants.FAIL.toString() ));
          return 0;
      }
  }

  public int postTransfer(Account senderAccount, Account receiverAccount, int amountToTransfer){
       String query = "insert into pendingTransactions values(?, ?, ?)";
      try {
          PreparedStatement pStmt = con.prepareStatement(query);
          pStmt.setInt(1,senderAccount.getAccountNumber());
          pStmt.setInt(2, receiverAccount.getAccountNumber());
          pStmt.setInt(3, amountToTransfer);
          return  pStmt.executeUpdate();
      }catch(SQLException ex){
          return 0;
      }
  }
  public int validatePendingTransaction(Account senderAccount, Account receiverAccount, int amountToTransfer){
        String query = "select count(*) from pendingTransactions where senderAccountNumber=? and receiverAccountNumber=? and amount=?";
      System.out.println("Details are "+senderAccount.getAccountNumber()+" - "+receiverAccount.getAccountNumber()+"-"+amountToTransfer);
      try {
          PreparedStatement pStmt = con.prepareStatement(query);
          pStmt.setInt(1,senderAccount.getAccountNumber());
          pStmt.setInt(2, receiverAccount.getAccountNumber());
          pStmt.setInt(3, amountToTransfer);
          ResultSet rs =  pStmt.executeQuery();
          rs.next();
          int count = rs.getInt(1);
          return count;
      }catch(SQLException ex){
          System.out.println("validate Pending transactions query failed "+ex.getMessage());
          return 0;
      }
  }
  public int completePendingTransaction(Account senderAccount, Account receiverAccount, int amountToTransfer){
        int senderResult = withDrawOrDeposit(senderAccount, amountToTransfer, Constants.WITHDRAW.toString());
        int receiverResult = withDrawOrDeposit(receiverAccount, amountToTransfer, Constants.DEPOSIT.toString());
        if(senderResult == 1 && receiverResult ==1){
            String query = "delete from pendingTransactions where senderAccountNumber=? and receiverAccountNumber=? and amount=?";
            try {
                PreparedStatement pStmt = con.prepareStatement(query);
                pStmt.setInt(1,senderAccount.getAccountNumber());
                pStmt.setInt(2, receiverAccount.getAccountNumber());
                pStmt.setInt(3, amountToTransfer);
                return  pStmt.executeUpdate();
            }catch(SQLException ex){
                return 0;
            }
        }else{
           return 0;
        }
  }

  public void displayTransactions(int accountNumber){
        String query = "select * from transaction where accountNumber = ?";
      try {
          PreparedStatement pStmt = con.prepareStatement(query);
          pStmt.setInt(1, accountNumber);
          ResultSet rsObj = pStmt.executeQuery();
          while(rsObj.next()){
              System.out.println("AccountNumber - "+rsObj.getInt(1)+
                      ", Amount - "+rsObj.getInt(2) +
                      ", Operation - "+rsObj.getString(3) +
                      ", Status - "+rsObj.getString(4)
              );
          }
      }catch(SQLException ex){
          System.out.println("exception occurred while viewing transactions "+ex.getMessage());
      }
    }

    private int updateTransaction(Transaction transaction){
         String query = "insert into transaction values (?, ?, ?, ?)";
        try {
            PreparedStatement pStmt = con.prepareStatement(query);
            pStmt.setInt(1, transaction.getAccountNumber());
            pStmt.setInt(2, transaction.getAmount());
            pStmt.setString(3, transaction.getOperation());
            pStmt.setString(4, transaction.getStatus());
            return  pStmt.executeUpdate();
        }catch(SQLException ex){
            System.out.println("Exception while updating transaction - "+ex.getMessage());
            return 0;
        }
    }
    public Account isAccountExistedForApproval(int accountNumber){
        String query = "select * from account where accountNumber = ? and status = '"+Constants.PENDINGTOAPPROVE.toString()+"'";
        try {
            PreparedStatement pStmtObj = con.prepareStatement(query);
            pStmtObj.setInt(1,accountNumber);
            ResultSet rsObj = pStmtObj.executeQuery();
            rsObj.next();
            Account account = new Account();
            account.setAccountNumber(rsObj.getInt(1));
            account.setAmount(rsObj.getInt(2));
            account.setUserId(rsObj.getInt(3));
            return account;
        }catch(SQLException ex){
            System.out.println("Sorry !! we could not find any request with given details "+accountNumber);
            return null;
        }
    }
    public int approveOrRejectAccount(Account account){
        String query = "update account set status = ? where accountNumber = ?";
        try {
            PreparedStatement pStmtObj = con.prepareStatement(query);
            pStmtObj.setString(1,account.getStatus());
            pStmtObj.setInt(2,account.getAccountNumber());
            return pStmtObj.executeUpdate();

        }catch(SQLException ex){
            System.out.println("Exception while approve or reject "+ex.getMessage());
            return 0;
        }
    }

}
