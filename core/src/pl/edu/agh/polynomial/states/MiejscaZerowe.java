package pl.edu.agh.polynomial.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Piotr Muras on 10.12.2016.
 */

public class MiejscaZerowe extends State {
    public MiejscaZerowe(GameStateManager gsm){
        super(gsm);
        Gdx.input.setInputProcessor(this);
        startEnterAnimation();
    }

    @Override
    public void handleInput(float x, float y) {

    }

    @Override
    public void render(SpriteBatch sb) {
        act();
        draw();
    }
}
