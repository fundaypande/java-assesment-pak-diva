package tools;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 * Created by User on 24/01/2018.
 */
public class ScreenWidth {
    public static class screenWidth {
        public static double allWidth(){
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            Double width = bounds.getWidth();
            return width;
        }
        public static double allHeight(){
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            Double height = bounds.getHeight();
            return height;
        }
    }

}
