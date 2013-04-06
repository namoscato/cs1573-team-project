import java.util.List;

public class Configuration {
	private List<Feature> discrete;
	private List<Feature> continuous;
	
	/*
	 * Create Configuration object
	 * @param discrete set of discrete features
	 * @param continuous set of continuous features
	 */
	public Configuration(List<Feature> discrete, List<Feature> continuous) {
		this.discrete = discrete;
		this.continuous = continuous;
	}
	
	public List<Feature> getDiscrete() {
		return discrete;
	}
	
	public List<Feature> getContinuous() {
		return continuous;
	}
	
	public String toString() {
		return "discrete = " + discrete.toString() + "\ncontinuous = " + continuous.toString();
	}
}
