package assign01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * For testing the GrayscaleImage class.
 * @author Ben Jones and Jake Heairld
 * @version Jan 12, 2024
 */
class GrayscaleImageTest {

    private GrayscaleImage smallSquare;
    private GrayscaleImage smallWide;

    @BeforeEach
    void setUp() {
        smallSquare = new GrayscaleImage(new double[][]{{1,2},{3,4}});
        smallWide = new GrayscaleImage(new double[][]{{1,2,3},{4,5,6}});
    }

    @Test
    void testGetPixel(){
        assertEquals(smallSquare.getPixel(0,0), 1);
        assertEquals(smallSquare.getPixel(1,0), 2);
        assertEquals(smallSquare.getPixel(0,1), 3);
        assertEquals(smallSquare.getPixel(1,1), 4);
    }
    
    @Test
    void testGetPixelXOutOfBounds() {
    	assertThrows(IllegalArgumentException.class, () -> smallSquare.getPixel(2,1));
    }
    
    @Test
    void testGetPixelYOutOfBounds() {
    	assertThrows(IllegalArgumentException.class, () -> smallSquare.getPixel(1,2));
    }
    
    @Test
    void testGetPixelXNegative() {
    	assertThrows(IllegalArgumentException.class, () -> smallSquare.getPixel(-1,1));
    }
    
    @Test
    void testGetPixelYNegative() {
    	assertThrows(IllegalArgumentException.class, () -> smallSquare.getPixel(1,-1));
    }

    @Test
    void testEquals() {
        assertEquals(smallSquare, smallSquare);
        var equivalent = new GrayscaleImage(new double[][]{{1,2},{3,4}});
        assertEquals(smallSquare, equivalent);
    }

    @Test
    void testEqualsNotInstanceOf() {
    	String word = "Word";
    	assertNotEquals(smallSquare, word);
    }
    
    @Test
    void testEqualsDifferentRowLength() {
    	var other = new GrayscaleImage(new double[][]{{1,2},{3,4},{5,6}});
    	assertNotEquals(smallSquare, other);
    }
    
    @Test
    void testEqualsDifferentColLength() {
    	var other = new GrayscaleImage(new double[][]{{1,2,3},{4,5,6}});
    	assertNotEquals(smallSquare, other);
    }
    
    @Test
    void testEqualsDifferentPixel() {
    	var other = new GrayscaleImage(new double[][]{{1,2},{2,4}});
    	assertNotEquals(smallSquare, other);
    }
    
    @Test
    void averageBrightness() {
        assertEquals(smallSquare.averageBrightness(), 2.5, 2.5*.001);
        var bigZero = new GrayscaleImage(new double[1000][1000]);
        assertEquals(bigZero.averageBrightness(), 0);
    }

    @Test
    void averageBrightnessOnePixel() {
    	var singlePixelImage = new GrayscaleImage(new double[][] {{200}});
    	 assertEquals(singlePixelImage.averageBrightness(), 200, 200*.001);
    }
    
    @Test
    void normalized() {
        var smallNorm = smallSquare.normalized();
        assertEquals(smallNorm.averageBrightness(), 127, 127*.001);
        var scale = 127/2.5;
        var expectedNorm = new GrayscaleImage(new double[][]{{scale, 2*scale},{3*scale, 4*scale}});
        for(var row = 0; row < 2; row++){
            for(var col = 0; col < 2; col++){
                assertEquals(smallNorm.getPixel(col, row), expectedNorm.getPixel(col, row),
                        expectedNorm.getPixel(col, row)*.001,
                        "pixel at row: " + row + " col: " + col + " incorrect");
            }
        }
    }
    
    @Test
    void normalizeSinglePixelImage() {
    	var singlePixelImage = new GrayscaleImage(new double[][] {{200}});
    	var singlePixelNorm = singlePixelImage.normalized();
        assertEquals(singlePixelNorm.averageBrightness(), 127, 127*.001);
    }
    
    @Test
    void normalizePixelsPreNormalized() {
    	var normalizedImage = new GrayscaleImage(new double[][] {{0, 127, 254},{254, 127, 0},{127, 127, 127}});
    	var renormalizedImage = normalizedImage.normalized();
        assertEquals(renormalizedImage.averageBrightness(), 127, 127*.001);
    }
    
    @Test
    void mirrored() {
        var expected = new GrayscaleImage(new double[][]{{2,1},{4,3}});
        assertEquals(smallSquare.mirrored(), expected);
    }

    @Test
    void mirroredOddCol() {
    	var original = new GrayscaleImage(new double[][]{{1,2,3},{4,5,6}});
    	var expected = new GrayscaleImage(new double[][]{{3,2,1},{6,5,4}});
        assertEquals(original.mirrored(), expected);
    }
    
    @Test
    void mirroredOneCol() {
    	var original = new GrayscaleImage(new double[][]{{2},{5},{8}});
    	var expected = new GrayscaleImage(new double[][]{{2},{5},{8}});
        assertEquals(original.mirrored(), expected);
    }
    
    @Test
    void cropped() {
        var cropped = smallSquare.cropped(1,1,1,1);
        assertEquals(cropped, new GrayscaleImage(new double[][]{{4}}));
    }

    @Test
    void croppedWidthOutOfBounds() {
    	assertThrows(IllegalArgumentException.class, () -> smallSquare.cropped(1,1,2,1));
    }
    
    @Test
    void croppedHeightOutOfBounds() {
    	assertThrows(IllegalArgumentException.class, () -> smallSquare.cropped(1,1,1,2));
    }
    
    @Test
    void croppedStartRowOutOfBounds() {
    	assertThrows(IllegalArgumentException.class, () -> smallSquare.cropped(2,1,1,1));
    }
    
    @Test
    void croppedStartColOutOfBounds() {
    	assertThrows(IllegalArgumentException.class, () -> smallSquare.cropped(1,2,1,1));
    }
    
    @Test
    void croppedHeightZero() {
    	assertThrows(IllegalArgumentException.class, () -> smallSquare.cropped(1,1,1,0));
    }
    
    @Test
    void croppedWidthZero() {
    	assertThrows(IllegalArgumentException.class, () -> smallSquare.cropped(1,1,0,1));
    }
    
    @Test
    void croppedStartRowNegative() {
    	assertThrows(IllegalArgumentException.class, () -> smallSquare.cropped(-1,1,1,1));
    }
    
    @Test
    void croppedStartColNegative() {
    	assertThrows(IllegalArgumentException.class, () -> smallSquare.cropped(1,-1,1,1));
    }
    
    @Test
    void squarified() {
        var squared = smallWide.squarified();
        var expected = new GrayscaleImage(new double[][]{{1,2},{4,5}});
        assertEquals(squared, expected);
    }
    
    @Test
    void squarifiedSquareImage() {
    	var original = new GrayscaleImage(new double[][]{{2,3},{4,5}});
    	var expected = new GrayscaleImage(new double[][]{{2,3},{4,5}});
        assertEquals(original.squarified(), expected);
    }
    
    @Test
    void squarifiedSinglePixelImage() {
    	var original = new GrayscaleImage(new double[][]{{2}});
    	var expected = new GrayscaleImage(new double[][]{{2}});
        assertEquals(original.squarified(), expected);
    }
    
    @Test
    void squarifiedOneRowManyCol() {
    	var original = new GrayscaleImage(new double[][]{{2,5,8,34,22,64,200,4,7}});
    	var expected = new GrayscaleImage(new double[][]{{2}});
        assertEquals(original.squarified(), expected);
    }
    
    @Test
    void squarifiedOneColManyRow() {
    	var original = new GrayscaleImage(new double[][]{{2},{4},{1},{200},{0}});
    	var expected = new GrayscaleImage(new double[][]{{2}});
        assertEquals(original.squarified(), expected);
    }
}