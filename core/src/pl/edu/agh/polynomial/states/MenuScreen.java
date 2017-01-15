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
import com.badlogic.gdx.utils.Timer;

import java.util.Iterator;

import pl.edu.agh.polynomial.Polynomial;
import sun.applet.Main;

import static com.badlogic.gdx.Gdx.app;
import static pl.edu.agh.polynomial.Polynomial.skin;

/**
 * Created by MichałM on 2016-12-12.
 */

public class MenuScreen extends State {


    private BitmapFont sofiaProSoftMedium46px = new BitmapFont(Gdx.files.internal("SofiaProSoftMedium46px.fnt"));
    private GlyphLayout layout = new GlyphLayout(); // do mierzenia długości tekstu w px

    private Image bg;

    private Image koniec;
    private Image miejsca;
    private Image wykres;
    private Image wstecz;


    private Label[] potegi;

    private Label[] x;

    private Label[] znak;


    private int stopienWielomianu;

    private Label zero = new Label("=0" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));
    private Label[] wspolczynniki;

    private Label TwojWielomian = new Label("Twój wielomian ma postać:" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));

    private int w1;
    private int h1;

    MenuScreen(GameStateManager gsm) {
        super(gsm);
        stopienWielomianu= MainScreen.getDane().length-1;


        bg = new Image(skin.getDrawable("bg"));
        addActor(bg);

        layout.setText(sofiaProSoftMedium46px, TwojWielomian.getText());
        TwojWielomian.setPosition(Polynomial.WIDTH/2-layout.width/2 , upY(75));
        addActor(TwojWielomian);
        x = new Label[stopienWielomianu+1];
        znak = new Label[stopienWielomianu+1];
        potegi = new Label[stopienWielomianu+1];
        wspolczynniki = new Label[stopienWielomianu+1];

        int z=0;

        for(int i=0; i<stopienWielomianu+1-z; i++) {
            wspolczynniki[i] = new Label("", new Label.LabelStyle(sofiaProSoftMedium46px, Color.BLACK));
            int h = i/ 5;
            int w = i % 5;
            h1=h;
            w1=w;

            if(MainScreen.getDane()[i+z]!=0){


                wspolczynniki[i].setText(MainScreen.getDane()[i+z].toString().replaceAll("-"," "));
                //if(MainScreen.getDane()[i+z]==1.0&&i+z!=stopienWielomianu) wspolczynniki[i].setText("");
                layout.setText(sofiaProSoftMedium46px, wspolczynniki[i].getText());

                wspolczynniki[i].setPosition(190 * w+40 , upY(h * 75 + 120));
                if(MainScreen.getDane()[i+z]==1.0&&i+z!=stopienWielomianu) {
                    wspolczynniki[i].setVisible(false);
                }



                String pot;
                if (stopienWielomianu - i-z != 1 && stopienWielomianu - i -z!= 0)
                    pot = Integer.toString(stopienWielomianu - i-z);
                else pot = " ";

                potegi[i+z] = new Label(pot, new Label.LabelStyle(sofiaProSoftMedium46px, Color.BLACK));
                potegi[i+z].setFontScale(0.5f);
                potegi[i+z].setPosition(190 * w + layout.width / 2 + 120, upY(h * 75 + 130));

                String pisz;
                if (stopienWielomianu - i-z > 0) pisz = "x";
                else pisz = " ";
                if(z<=i){
                    x[i-z] = new Label(pisz, new Label.LabelStyle(sofiaProSoftMedium46px, Color.BLACK));

                    x[i-z].setPosition(190 * w + layout.width / 2 + 100, upY(h * 75 + 150));
                    addActor(x[i-z]);
                }


                znak[i+z] = new Label("+", new Label.LabelStyle(sofiaProSoftMedium46px, Color.BLACK));

                if (i == 0) znak[i].setText("");
                znak[i+z].setPosition(190 * w + layout.width/2-40, upY(h * 75 + 150 ));
                if(MainScreen.getDane()[i+z]<0) znak[i+z].setText("-");


                addActor(potegi[i+z]);
                addActor(znak[i+z]);
                addActor(wspolczynniki[i]);

            }
            else {


                i--;
                z++;
            };
        }

        if(MainScreen.getDane()[stopienWielomianu]==0.0){
            w1=w1-1;
            if(w1<0) {
                w1=4;
                h1=h1-1;
                if(h1<0) h1=0;
            }

       }
        if(MainScreen.getDane()[stopienWielomianu]==0.0){
            zero.setPosition(190 * w1 + layout.width / 2 + 117, upY(h1 * 75 + 150));
        }else {
            zero.setPosition(190 * w1 + layout.width / 2 + 95, upY(h1 * 75 + 150));
            }
        addActor(zero);

        int miejsce;
        if(stopienWielomianu>=22) miejsce=-30;
        else miejsce =0;

        koniec=new Image(Polynomial.skin.getDrawable("koniec"));
        koniec.setPosition(Polynomial.WIDTH-koniec.getWidth(),miejsce);
        addActor(koniec);

        wstecz=new Image(Polynomial.skin.getDrawable("wstecz"));
        wstecz.setPosition(0,miejsce);
        addActor(wstecz);

        miejsca=new Image(Polynomial.skin.getDrawable("miejsca"));
        miejsca.setPosition(2*Polynomial.WIDTH/5-miejsca.getWidth()/2,miejsce);
        addActor(miejsca);

        wykres=new Image(Polynomial.skin.getDrawable("wykres"));
        wykres.setPosition(3*Polynomial.WIDTH/5-miejsca.getWidth()/2,miejsce);
        addActor(wykres);


        Gdx.input.setInputProcessor(this);
        startEnterAnimation();




    }





    @Override
    public void handleInput(float x, float y) {

            if((x-koniec.getX()-koniec.getWidth()/2)*(x-koniec.getX()-koniec.getWidth()/2) + (y-upY((int)koniec.getY())+koniec.getHeight()/2)*(y-upY((int)koniec.getY())+koniec.getHeight()/2) < koniec.getWidth()/2*koniec.getWidth()/2) {
                startEndAnimationAndPopState();

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        app.exit();

                    }
                }, 0.75f);
            }

            if((x-wstecz.getX()-wstecz.getWidth()/2)*(x-wstecz.getX()-wstecz.getWidth()/2) + (y-upY((int)wstecz.getY())+wstecz.getHeight()/2)*(y-upY((int)wstecz.getY())+wstecz.getHeight()/2) < wstecz.getWidth()/2*wstecz.getWidth()/2) {

                   startEndAnimationAndPushNewState(new StartScreen(gsm));
            }
            if((x-miejsca.getX()-miejsca.getWidth()/2)*(x-miejsca.getX()-miejsca.getWidth()/2) + (y-upY((int)miejsca.getY())+miejsca.getHeight()/2)*(y-upY((int)miejsca.getY())+miejsca.getHeight()/2) < miejsca.getWidth()/2*miejsca.getWidth()/2) {

                 startEndAnimationAndPushNewState(new MiejscaZerowe(gsm));
            }
            if((x-wykres.getX()-wykres.getWidth()/2)*(x-wykres.getX()-wykres.getWidth()/2) + (y-upY((int)wykres.getY())+wykres.getHeight()/2)*(y-upY((int)wykres.getY())+wykres.getHeight()/2) < wykres.getWidth()/2*wykres.getWidth()/2) {
                startEndAnimationAndPushNewState(new Graph(gsm));
             }



    }

    @Override
    public void render(SpriteBatch sb) {
        act();
        draw();
        drawShadow();
    }
}
