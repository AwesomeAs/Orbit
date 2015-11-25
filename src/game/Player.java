package game;

public class Player {
	private String name;
	private int playerNo;
	private String playerInfo;
	private Field[] fieldsOwned = new Field[22];

	// Ensures the player have a account.
	private Account acc = new Account();

	/**
	 * Player constructor.
	 * 
	 * @param name
	 *            the name of the player.
	 * @param playerNo
	 *            the number of the player.
	 */
	public Player(String name, int playerNo) {
		this.name = name;
		this.playerNo = playerNo;
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
	public int getPlayerNo() {
		return playerNo;
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
	public String buyField(Field field) {
		if (field.isAvailable()) {
			for (int i = 0; i < fieldsOwned.length; i++) {
				if (fieldsOwned[i] == null) {
					fieldsOwned[i] = field;
					break;
				}
			}
			return "you can buy the field";
		} else {
			return "you can not buy the field";
		}
	}

	public void setPlayerInfo(String playerInfo) {
		this.playerInfo = playerInfo;

	}

	public String getPlayerInfo() {
		return playerInfo;
	}
	
	public Field[] getFieldsOwned(){
		return fieldsOwned;
	}
}
