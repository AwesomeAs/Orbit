package game;

public class Field {
	private int fieldNo; // 1 to 12
	private int value; // +/- point consequence of field
	private String name;
	private boolean available = true;

	/**
	 * Field constructor.
	 * @param fieldNo the number of the field.
	 * @param value the value the field holds.
	 * @param name gives the field personality.
	 */
	public Field(int fieldNo, int value, String name) {
		this.fieldNo = fieldNo;
		this.value = value;
		this.name = name;
	}

	/**
	 * getFieldNo returns the fields number.
	 * @return field number.
	 */
	public int getFieldNo() {
		return fieldNo;
	}

	/**
	 * setFieldNo sets a new field number.
	 * @param fieldNo the new value for the field number.
	 */
	public void setFieldNo(int fieldNo) {
		this.fieldNo = fieldNo;
	}

	/**
	 * getName returns the field name.
	 * @return field name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * setName sets a new field name.
	 * @param name the new name for the field.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * getValue returns the field value.
	 * @return field value.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * setValue sets a new field value.
	 * @param value the new field value.
	 */
	public void setValue(int value) {
		this.value = value;
	}

	public boolean isAvailable(){
		if (this.available)
			return this.available;
		else 
			return this.available;
	}
}
