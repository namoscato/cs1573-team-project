import java.util.Scanner;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class Test {
	public static void main(String[] args) throws Exception {
		// declare attributes
		Attribute rating = new Attribute("rating");
		Attribute runtime = new Attribute("runtime");
		
		// Declare the feature vector
		FastVector attributes = new FastVector(2);
		attributes.addElement(rating);
		attributes.addElement(runtime);
		
		// Create an empty training set
		Instances trainingSet = new Instances("movie-runtimes", attributes, 10);
		trainingSet.setClassIndex(0);
		
		// open file
		Scanner scanner = Parse.openFile("../data-collection/runtime-data-test.txt");
		
		// add examples
		while (scanner.hasNextLine()) {
			double[] values = new double[attributes.size()];
			String[] temp = scanner.nextLine().split(",");
			for (int i = 0; i < attributes.size(); i++) {
				values[i] = Double.parseDouble(temp[i]);
			}
			trainingSet.add(new Instance(1, values));
		}

		// add the instance
		LinearRegression model = new LinearRegression();
		model.buildClassifier(trainingSet);

		// test the model
		Evaluation eval = new Evaluation(trainingSet);
		eval.evaluateModel(model, trainingSet);

		// print results
		double[] coef = model.coefficients();
		System.out.println("[" + coef[1] + ", " + coef[2] + "]");
		System.out.println(eval.toSummaryString());
	}
}
