import java.util.List;

public class Data {
	private int id;
	private String identifier;
	private float output;
	private List<List<String>> discrete;
	private List<Number> continuous;
	
	/*
	 * Create Data object
	 * @param id example id
	 * @param identifier example id
	 * @param output actual continuous output of example
	 * @param discrete list of discrete feature values
	 * @param continuous list of continuous feature values
	 */
	public Data(int id, String identifier, float output, List<List<String>> discrete, List<Number> continuous) {
		this.id = id;
		this.identifier = identifier;
		this.output = output;
		this.discrete = discrete;
		this.continuous = continuous;
	}
	
	public int getID() {
		return id;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public float getOutput() {
		return output;
	}
	
	public List<List<String>> getDiscrete() {
		return discrete;
	}
	
	public List<String> getDiscrete(int index) {
		return discrete.get(index);
	}
	
	public int getDiscreteSize() {
		return discrete.size();
	}
	
	public List<Number> getContinuous() {
		return continuous;
	}
	
	public Number getContinuous(int index) {
		return continuous.get(index);
	}
	
	public int getContinuousSize() {
		return continuous.size();
	}
	
	public String toString() {
		return "{" + id + ", " + output + ", " + discrete.toString() + ", " + continuous.toString() + "}";
	}
}
