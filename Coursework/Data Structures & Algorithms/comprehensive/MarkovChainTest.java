package comprehensive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MarkovChainTest {
	
	private WordProbabilityModel model;
	
	@BeforeEach 
	void setup() {
		model = new WordProbabilityModel("story.txt");
	}
	
	@Test
	void ultimateTest() {
		System.out.println(model.generateText("a", 3));
	}
	
	
}
