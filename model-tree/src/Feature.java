import java.util.List;

public class Feature {
	private int id;
	private int oldID;
	private String name;
	private List<String> values;
	
	/*
	 * Discrete feature constructor
	 * @param id feature id
	 * @param name feature name
	 * @param values feature values
	 */
	public Feature(int id, int oldID, String name, List<String> values) {
		this.id = id;
		this.oldID = oldID;
		this.name = name;
		this.values = values;
	}
	
	/*
	 * Continuous feature constructor
	 * @param id feature id
	 * @param name feature name
	 */
	public Feature(int id, int oldID, String name) {
		this.id = id;
		this.oldID = oldID;
		this.name = name;
		this.values = null;
	}
	
	public int getID() {
		return id;
	}
	
	public int getOldID() {
		return oldID;
	}
	
	public String getName() {
		return name;
	}
	
	public List<String> getValues() {
		return values;
	}
	
	public String getValue(int index) {
		if (isDiscrete() && index >= 0 && index < values.size()) {
			return values.get(index);
		} else {
			return null;
		}
	}
	
	public int getValueCount() {
		if (isDiscrete()) {
			return values.size();
		} else {
			return -1;
		}
	}
	
	public boolean isDiscrete() {
		if (values == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean isContinuous() {
		if (isDiscrete()) {
			return false;
		} else {
			return true;
		}
	}
	
	public String toString() {
		String result = "(" + id + ")" + name;
		if (isDiscrete()) {
			result += " => " + values.toString();
		}
		return result;
	}
}
