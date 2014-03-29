
package ponggl;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class Main {
    public static void main(String[] args) {
        try {
            GameLogic game = new GameLogic(640, 480);
            Graphics graphics = new Graphics(640, 480, "PongGL");
            
            graphics.addRect(game.ball);
            
            while (!Display.isCloseRequested()) {
                //game.update();
                //graphics.updateRect(game.ball);
                graphics.draw();
                
                Display.sync(60);
                Display.update();
            }
            
            graphics.close();
        } catch (LWJGLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
