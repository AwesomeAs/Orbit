package game;

public class Account {
    // Default accounts balance to 1000.
    private int balance = 1000;

    /**
     * getBalance returns the account balance.
     * @return account balance.
     */
    public int getBalance(){
        return balance;
    }
    
    /**
     * setBalance sets the new balance in the account but it will always ensure that
     * the balance will never be under zero.
     * @param balance the value the new balance should take.
     */
    public boolean setBalance(int balance){
        this.balance = balance;
        //If account balance becomes negative, it returns a boolean value that is false if the player is bankrupt
        if (this.balance < 0){
            return false;
        }else
        	return true;
    }
}
