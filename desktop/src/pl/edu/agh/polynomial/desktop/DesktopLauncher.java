package pl.edu.agh.polynomial.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import pl.edu.agh.polynomial.Polynomial;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width=960;
		config.height=540;
		config.title=Polynomial.TITLE;
		new LwjglApplication(new Polynomial(), config);
	}
}
