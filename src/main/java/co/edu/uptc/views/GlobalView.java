package co.edu.uptc.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import co.edu.uptc.utilities.PropertiesService;

public class GlobalView {

    private static PropertiesService propertiesService = new PropertiesService();
    
    public static final Color PRIMARY_BTN_BACKGROUND = new Color(0xC9D7F2);
    public static final Color OPTIONS_BACKGROUND = new Color(0x0A1926);
    public static final Color SECONDARY_BTN_BACKGROUND = new Color(0x6D8BA6);
    public static final Color TITLE_TEXT = new Color(0xFFFFFF);
    public static final Color ALL_TEXT = new Color(0x0A1426);

    public static final Font TITLE_FONT_SMALL;
    public static final Font TITLE_FONT;
    public static final Font ALL_TEXT_FONT;
    public static final Font TITLE_FONT_MEDIUM;
    public static final Color DEFAULT_BTN_BACKGROUND = new Color(0xD9D9D9);

    static {
        Font titleFontSmalltemp = null;
        Font titleFontTemp = null;
        Font allTextFontTemp = null;
        Font titleFontMediumTemp = null;
        try {
            titleFontSmalltemp = Font.createFont(Font.TRUETYPE_FONT, new File(propertiesService.getKeyValue("titleFontPath"))).deriveFont(24f);
            titleFontTemp = Font.createFont(Font.TRUETYPE_FONT, new File(propertiesService.getKeyValue("titleFontPath"))).deriveFont(120f);
            titleFontMediumTemp = Font.createFont(Font.TRUETYPE_FONT, new File(propertiesService.getKeyValue("titleFontPath"))).deriveFont(50f);
            allTextFontTemp = Font.createFont(Font.TRUETYPE_FONT, new File(propertiesService.getKeyValue("allTextFontPath"))).deriveFont(24f);
            
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(titleFontSmalltemp);
            ge.registerFont(titleFontTemp);
            ge.registerFont(allTextFontTemp);
            ge.registerFont(titleFontMediumTemp);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        TITLE_FONT_SMALL = titleFontSmalltemp;
        TITLE_FONT = titleFontTemp;
        ALL_TEXT_FONT = allTextFontTemp;
        TITLE_FONT_MEDIUM = titleFontMediumTemp;
    }
}
