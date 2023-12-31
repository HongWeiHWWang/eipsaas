package com.hotent.bpm.persistence.constants;

public enum Bool {
	TRUE('Y',"是"),FALSE('N',"否");
	private char value;
	private String label;
	Bool(char _value,String _label){
		value=_value;
		label=_label;
	}
	public char value() {
		return value;
	}
	public String valueToString() {
		return String.valueOf(value);
	}
	public String label() {
		return label;
	}
	public static Bool fromValue(char value){
		if(value=='Y'){
			return Bool.TRUE;
		}
		return Bool.FALSE;
	}
	public static Bool fromValue(String value){
		if(value.equals("Y")){
			return Bool.TRUE;
		}
		return Bool.FALSE;
	}
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
