package pl.edu.agh.polynomial.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

import pl.edu.agh.polynomial.Polynomial;

/**
 * Created by Piotr Muras on 04.12.2016.
 */

public class MainScreen extends State {

    private BitmapFont sofiaProSoftMedium46px = new BitmapFont(Gdx.files.internal("SofiaProSoftMedium46px.fnt"));
    private Label podajWspolczynniki = new Label("Podaj współczynniki wielomianu" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));
    private GlyphLayout layout = new GlyphLayout(); // do mierzenia długości tekstu w px
    private TextField wspolczynniki;
    private Image dalej;


    MainScreen(GameStateManager gsm , int stopienWielomianu){
        super(gsm);

        layout.setText(sofiaProSoftMedium46px , podajWspolczynniki.getText());
        podajWspolczynniki.setPosition(Polynomial.WIDTH/2-layout.width/2 , upY(100));
        addActor(podajWspolczynniki);
        TextField.TextFieldStyle tStyle = new TextField.TextFieldStyle();
        tStyle.fontColor = Color.RED;
        tStyle.font = sofiaProSoftMedium46px;
        tStyle.background =  Polynomial.skin.getDrawable("ramka");

        Array<TextField> wspolczynniki = new Array<TextField>();

        for(int i=0; i<stopienWielomianu; i++){
            TextField wspolczynnik = new TextField("",tStyle);
            wspolczynnik.setMessageText("0");
            wspolczynnik.setPosition(200*i,upY(200));
            wspolczynnik.setAlignment(Align.center);
            wspolczynnik.setMaxLength(2);
            wspolczynnik.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
            wspolczynniki.add(wspolczynnik);
        }
        Iterator<TextField> iter = wspolczynniki.iterator();
        while(iter.hasNext()){
            TextField wspolczynnik = iter.next();

            addActor(wspolczynnik);
        }

        dalej=new Image(Polynomial.skin.getDrawable("dalej"));
        dalej.setPosition(Polynomial.WIDTH/2-dalej.getWidth()/2,upY(400));
        addActor(dalej);
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
        drawShadow();
    }
}
