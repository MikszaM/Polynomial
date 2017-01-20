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
 * Created by MichałM on 2017-01-20.
 */

public class DomainScreen extends State {

    private BitmapFont sofiaProSoftMedium46px = new BitmapFont(Gdx.files.internal("SofiaProSoftMedium46px.fnt"));
    private GlyphLayout layout = new GlyphLayout(); // do mierzenia długości tekstu w px

    private Image bg;
    private Image Copyright;
    private Image real;
    private Image complex;
    private Label Dziedzina = new Label("Wybierz dziedzinę równania:" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));

    DomainScreen(GameStateManager gsm) {
        super(gsm);
        bg = new Image(skin.getDrawable("bg"));
        addActor(bg);

        Copyright=new Image(Polynomial.skin.getDrawable("Copyright"));
        Copyright.setScale(0.58f);
        Copyright.setPosition((int) (Polynomial.WIDTH-Copyright.getWidth()*0.58),upY((int) (Copyright.getHeight()*0.85)));
        addActor(Copyright);

        layout.setText(sofiaProSoftMedium46px, Dziedzina.getText());
        Dziedzina.setPosition(Polynomial.WIDTH/2-layout.width/2 , upY(75));
        addActor(Dziedzina);

        real=new Image(Polynomial.skin.getDrawable("real"));
        real.setPosition(Polynomial.WIDTH/4-real.getWidth()/2,Polynomial.HEIGHT/2);
        addActor(real);

        complex=new Image(Polynomial.skin.getDrawable("complex"));
        complex.setPosition(3*Polynomial.WIDTH/4-complex.getWidth()/2,Polynomial.HEIGHT/2);
        addActor(complex);

        Gdx.input.setInputProcessor(this);
        startEnterAnimation();
    }

    @Override
    public void handleInput(float x, float y) {
        if((x-real.getX()-real.getWidth()/2)*(x-real.getX()-real.getWidth()/2) + (y-upY((int)real.getY())+real.getHeight()/2)*(y-upY((int)real.getY())+real.getHeight()/2) < real.getWidth()/2*real.getWidth()/2) {

            startEndAnimationAndPushNewState(new MiejscaZerowe(gsm,0));
        }
        if((x-complex.getX()-complex.getWidth()/2)*(x-complex.getX()-complex.getWidth()/2) + (y-upY((int)complex.getY())+complex.getHeight()/2)*(y-upY((int)complex.getY())+complex.getHeight()/2) < complex.getWidth()/2*complex.getWidth()/2) {
            startEndAnimationAndPushNewState(new MiejscaZerowe(gsm,1));
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        act();
        draw();
        drawShadow();
    }
}
