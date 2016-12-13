package pl.edu.agh.polynomial.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.edu.agh.polynomial.Polynomial;

import static pl.edu.agh.polynomial.Polynomial.skin;

/**
 * Created by MichałM on 2016-12-12.
 */

public class MenuScreen extends State {


    private BitmapFont sofiaProSoftMedium46px = new BitmapFont(Gdx.files.internal("SofiaProSoftMedium46px.fnt"));
    private GlyphLayout layout = new GlyphLayout(); // do mierzenia długości tekstu w px

    private Image bg;

    private Image dalej;

    MenuScreen(GameStateManager gsm) {
        super(gsm);

        bg = new Image(skin.getDrawable("bg"));
        addActor(bg);

        dalej=new Image(Polynomial.skin.getDrawable("dalej"));
        dalej.setPosition(Polynomial.WIDTH/2-dalej.getWidth()/2,0);
        addActor(dalej);



        Gdx.input.setInputProcessor(this);
        startEnterAnimation();




    }





    @Override
    public void handleInput(float x, float y) {
        if((x-dalej.getX()-dalej.getWidth()/2)*(x-dalej.getX()-dalej.getWidth()/2) + (y-upY((int)dalej.getY())+dalej.getHeight()/2)*(y-upY((int)dalej.getY())+dalej.getHeight()/2) < dalej.getWidth()/2*dalej.getWidth()/2){
            startEndAnimationAndPushNewState(new MiejscaZerowe(gsm));
        }

    }

    @Override
    public void render(SpriteBatch sb) {
        act();
        draw();
        drawShadow();
    }
}
