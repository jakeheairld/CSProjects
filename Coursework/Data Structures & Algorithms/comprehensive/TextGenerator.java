package comprehensive;

/**
 * This class represents a text generator.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version April 21, 2024 
 */
public class TextGenerator {
	
	/**
	 * Main class for handling the text generator and command line arguments.
	 * 
	 * @param args - Command line arguments.
	 */
	public static void main(String[] args) {
		String file = args[0];
		String seed = args[1];
		int numWords = Integer.parseInt(args[2]);
		String outputType = "default";
		if(args.length == 4) {
			outputType = args[3];
		}
		WordProbabilityModel model = new WordProbabilityModel(file);
		if(outputType.equals("default")) {
			System.out.println(model.generateText(seed, numWords));			
		} else if(outputType.equals("all")) {
			System.out.println(model.generateTextAll(seed, numWords));						
		} else if(outputType.equals("one")) {
			System.out.println(model.generateTextOne(seed, numWords));
		} else {
			System.out.println("Invalid fourth argument.");
		}
	}	
}