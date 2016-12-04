package pl.edu.agh.polynomial.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Piotr Muras on 04.12.2016.
 */

public class MainScreen extends State {
    MainScreen(GameStateManager gsm , int stopienWielomianu){
        super(gsm);
    }

    @Override
    public void handleInput(float x, float y) {

    }

    @Override
    public void render(SpriteBatch sb) {
        act();
        draw();
        drawShadow();
    }
}
