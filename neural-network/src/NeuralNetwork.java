import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class NeuralNetwork {
	private MultilayerPerceptron mp;
	private FastVector attributes;
	private List<String[]> data;
	//private Instances data;
	
	public NeuralNetwork(String filename) {
		try {
			Scanner scan = new Scanner(new File(filename));
			
			// create our attributes
			String[] features = scan.nextLine().split(",");
			attributes = new FastVector();
			//attributes.addElement(new Attribute("rating"));
			for (String feature : features) {
				attributes.addElement(new Attribute(feature));
			}
			
			// bring our examples into memory
			data = new ArrayList<String[]>();
			while (scan.hasNext()) { 
				data.add(scan.nextLine().split(","));
			}
			
			// run 10-fold cross-validation
			mp = new MultilayerPerceptron();
			List<List<String[]>> subsets = splitList(data, 10);
			for (int i = 0; i < 9; i++) {
				List<String[]> train = new ArrayList<String[]>();
				for (int j = 0; j < 0; j++) {
					if (j != i) {
						train.addAll(subsets.get(j));
					}
				}
				
				Instances trainSet = createDataset(train);
				mp.buildClassifier(trainSet);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Instances createDataset(List<String[]> examples) {
		Instances dataSet = new Instances("data-set", attributes, examples.size());
		dataSet.setClassIndex(0);
		
		for (String[] example : examples) {
			Instance inst = createInstance(example);
			dataSet.add(inst);
		}
		
		return dataSet;
	}
	
	public Instance createInstance(String[] values) {
		Instance inst = new Instance(attributes.size());
		inst.setClassValue(values[0]);
		for (int i = 1; i < values.length; i++) {
			inst.setValue(i, values[i]);
		}
		return inst;
	}
	
	public static List<List<String[]>> splitList(List<String[]> list, int k) {
		List<List<String[]>> subsets = new ArrayList<List<String[]>>();
		int size = list.size() / k; //5
		for (int i = 0; i < k; i++) {
			int max = (i + 1) * size - 1;
			if (i == k - 1) {
				// our last subset is probably going to bigger
				max = list.size() - 1;
			}
			subsets.add( list.subList(i * size, max) );
		}
		return subsets;
	}
	
	public static void main(String[] args) {
		NeuralNetwork nn = new NeuralNetwork("../data-collection/datasets/usa/USA_data_test.csv");
	}
}
