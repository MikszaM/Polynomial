package pl.edu.agh.polynomial.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.edu.agh.polynomial.Polynomial;

import static pl.edu.agh.polynomial.Polynomial.skin;

/**
 * Created by MichałM on 2016-12-16.
 */

public class Graph extends State {
    public OrthographicCamera camera;
    private Image bg;
    private BitmapFont sofiaProSoftMedium46px = new BitmapFont(Gdx.files.internal("SofiaProSoftMedium46px.fnt"));
    private Label x = new Label("x" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));
    private Label y =new Label("y" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));



    public Graph(GameStateManager gsm) {
        super(gsm);
        bg = new Image(skin.getDrawable("bg"));
        addActor(bg);

        x.setPosition(Gdx.graphics.getWidth()-50,Gdx.graphics.getHeight()/2-50);
        y.setPosition(Gdx.graphics.getWidth()/2-50,Gdx.graphics.getHeight()-50);
        addActor(x);
        addActor(y);

    }

    @Override
    public void handleInput(float x, float y) {

    }

    @Override
    public void render(SpriteBatch sb) {
        act();
        draw();
        drawShadow();

        camera=new OrthographicCamera();
        camera.setToOrtho(false);
        camera.update();
        ShapeRenderer sr = new ShapeRenderer();
        sr.setColor(Color.BLACK);

        sr.setProjectionMatrix(camera.combined);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.rectLine(Gdx.graphics.getWidth()/2,0,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight(),4);
        sr.rectLine(0,Gdx.graphics.getHeight()/2,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()/2,4);

        sr.rectLine(30*Gdx.graphics.getWidth()/31,20*Gdx.graphics.getHeight()/42,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()/2,4);
        sr.rectLine(30*Gdx.graphics.getWidth()/31,22*Gdx.graphics.getHeight()/42,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()/2,4);



        sr.rectLine(30*Gdx.graphics.getWidth()/62,20*Gdx.graphics.getHeight()/21,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight(),4);
        sr.rectLine(32*Gdx.graphics.getWidth()/62,20*Gdx.graphics.getHeight()/21,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight(),4);

        sr.end();
    }
}
