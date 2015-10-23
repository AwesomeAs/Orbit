package Utils;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/*
 * 
 * Utility class for fetching and loading in fonts used by the Orbit
 * package.
 * 
 * Java 1.7
 * 
 */

public class FontManager {
	
	protected Font font;
	protected int size;
	protected String path;
	
	protected void loadFont() {
		InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/" + path);
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GraphicsEnvironment graph = GraphicsEnvironment.getLocalGraphicsEnvironment();
		graph.registerFont(font);
		Map<TextAttribute, Object> attr = new HashMap<TextAttribute, Object>();
		attr.put(TextAttribute.SIZE, size);
		font = font.deriveFont(attr);
	}
	
	public FontManager(String path, int size) {
		this.path = path;
		this.size = size;
		loadFont();
	}
	
	public Font get() {
		return font;
	}
}
