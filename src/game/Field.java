package game;

public abstract class Field {
    protected String name;
    protected int number;

    /**
     * Field constructor.
     * 
     * @param name
     *            gives the field personality.
     * @param number
     *            the number of the field.
     */
    public Field(String name, int number) {
	this.number = number;
	this.name = name;
    }

    /**
     * getNumber returns the fields number.
     * 
     * @return field number.
     */
    public int getNumber() {
	return this.number;
    }

    /**
     * setNumber sets a new field number.
     * 
     * @param number
     *            the new value for the field number.
     */
    public void setNumber(int number) {
	this.number = number;
    }

    /**
     * getName returns the field name.
     * 
     * @return field name.
     */
    public String getName() {
	return name;
    }

    /**
     * setName sets a new field name.
     * 
     * @param name
     *            the new name for the field.
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * landOnField is called every time a player lands on a field.
     * <br>
     * Subclasses shall implement the functionality.
     * @param player the player who has landed on the field.
     */
    public abstract void landOnField(Player player);
}

