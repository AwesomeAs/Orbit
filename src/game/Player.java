package game;

public class Player {
    private String name;
    private int number;
    private FieldStatus fieldStatus;
    private Ownable[] fieldsOwned = new Ownable[17]; // There are only 17 ownable fields.

    // Ensures the player have a account.
    private Account acc = new Account();

    /**
     * Player constructor.
     * 
     * @param name
     *            the name of the player.
     * @param number
     *            the number of the player.
     */
    public Player(String name, int number) {
	this.name = name;
	this.number = number;
    }

    /**
     * getName returns the player name.
     * 
     * @return
     */
    public String getName() {
	return name;
    }

    /**
     * setName sets the new player name.
     * 
     * @param name
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * getPlayerNo returns the player number.
     * 
     * @return player number.
     */
    public int getNumber() {
	return number;
    }

    /**
     * getAccount returns the account connected to the player.
     * 
     * @return player's account.
     */
    public Account getAccount() {
	return acc;
    }

    /**
     * 
     * 
     */
    public boolean buyField(Ownable ownable) {
	if(this.getAccount().getBalance() > ownable.getPrice()) {
            for(int i = 0; i < fieldsOwned.length; i++) {
            	if (fieldsOwned[i] != null && fieldsOwned[i].equals(ownable)) {
            		return true;
            	} else if (fieldsOwned[i] == null) {
            		this.getAccount().addBalance(-ownable.getPrice());
            		fieldsOwned[i] = ownable;
            		ownable.setOwner(this);
            		return true;
            	}
            }
		}
		return false;
    }

    public FieldStatus getFieldStatus() {
	return this.fieldStatus;
    }

    public void setFieldStatus(FieldStatus status) {
	this.fieldStatus = status;
    }

    public Ownable[] getFieldsOwned() {
	return fieldsOwned;
    }
    
    @Override
    public boolean equals(Object obj) {
	Player other = null;
	if(obj instanceof Player) {
	    other = (Player) obj;
	    return this.name == other.getName() && this.number == other.getNumber();
	}
	return false;
    }
}
