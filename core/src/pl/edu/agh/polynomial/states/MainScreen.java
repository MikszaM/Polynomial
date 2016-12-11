package pl.edu.agh.polynomial.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSorter;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
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

    private Label zero = new Label("=0" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));

    private static Array<TextField> wspolczynniki = new Array<TextField>();

    public static Array<TextField> getWspolczynniki() {
        return wspolczynniki;
    }

    public static Double dane[] ;

    public static Double[] getDane() { return dane; }

    private Label[] potegi;

    private Label[] x;

    private Label[] znak;

    private int dlugosc;

    private Image dalej;

    private Label blad = new  Label("Podałeś błedne dane" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));




    MainScreen(GameStateManager gsm , final int stopienWielomianu){
        super(gsm);
        dlugosc = stopienWielomianu;
        layout.setText(sofiaProSoftMedium46px , podajWspolczynniki.getText());
        podajWspolczynniki.setPosition(Polynomial.WIDTH/2-layout.width/2 , upY(75));
        addActor(podajWspolczynniki);


        TextField.TextFieldStyle tStyle = new TextField.TextFieldStyle();
        tStyle.fontColor = Color.RED;
        tStyle.font = sofiaProSoftMedium46px;
        tStyle.background =  Polynomial.skin.getDrawable("ramka");



        x = new Label[stopienWielomianu+1];
        znak = new Label[stopienWielomianu+1];
        potegi = new Label[stopienWielomianu+1];

        for(int i=0; i<stopienWielomianu+1; i++){
            TextField wspolczynnik = new TextField("",tStyle);
            wspolczynnik.setMessageText("1");
            if(i==stopienWielomianu) wspolczynnik.setMessageText("0");
            int h = i / 5;
            int w = i % 5;
            wspolczynnik.setPosition(190*w+10,upY(h*65+150));
            wspolczynnik.setAlignment(Align.center);
            wspolczynnik.setMaxLength(3);
            wspolczynniki.add(wspolczynnik);

            final int I = i;
            wspolczynnik.setTextFieldListener(new TextField.TextFieldListener() {
                @Override
               public void keyTyped(TextField textField, char c) {

                    if (c=='-') {
                        wspolczynniki.get(I).setText("");
                        znak[I].setText("-");
                    }
                    if(c=='+'){
                        wspolczynniki.get(I).setText("");
                        if(I!=0) znak[I].setText("+");
                        else znak[I].setText("");
                }
                    if(c==','){
                        wspolczynniki.get(I).setMaxLength(4);
                    }
                    if(c=='.'){
                        wspolczynniki.get(I).setMaxLength(4);
                    }

            }});


            String pot;
            if(stopienWielomianu-i != 1&&stopienWielomianu-i != 0) pot  = Integer.toString(stopienWielomianu-i);
            else pot = " ";

            potegi[i] = new Label(pot, new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));
            layout.setText(sofiaProSoftMedium46px , potegi[i].getText());
            potegi[i].setFontScale(0.5f);
            potegi[i].setPosition(190*w+wspolczynnik.getWidth()/2+80,upY(h*65+130));

            String pisz;
            if(stopienWielomianu-i != 0) pisz = "x";
            else pisz = " ";
            x[i] = new Label(pisz, new Label.LabelStyle(sofiaProSoftMedium46px,Color.BLACK));
            layout.setText(sofiaProSoftMedium46px , x[i].getText());
            x[i].setPosition( 190*w+wspolczynnik.getWidth()/2+60,upY(h*65+150) );

            znak[i]=new Label("+", new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));
            layout.setText(sofiaProSoftMedium46px , znak[i].getText());
            znak[i].setPosition( 190*w+wspolczynnik.getWidth()/2-60,upY(h*65+150) );
            if (i==0) znak[i].setText("");
            if(i==stopienWielomianu){
                layout.setText(sofiaProSoftMedium46px , zero.getText());
                zero.setPosition( 190*w+wspolczynnik.getWidth()/2+60,upY(h*65+150));
            }
        }

        Iterator<TextField> iter = wspolczynniki.iterator();
        while(iter.hasNext()){
            TextField wspolczynnik = iter.next();
            addActor(wspolczynnik);
        }


        for(Label i:x){
            addActor(i);
        }
        for(Label i:potegi){
           addActor(i);
        }
       for(Label i:znak){
            addActor(i);
        }

        addActor(zero);

        dalej=new Image(Polynomial.skin.getDrawable("dalej"));
        dalej.setPosition(Polynomial.WIDTH/2-dalej.getWidth()/2,0);
        addActor(dalej);



        Gdx.input.setInputProcessor(this);
        startEnterAnimation();


    }



    @Override
    public void handleInput(float x, float y) {
        if((x-dalej.getX()-dalej.getWidth()/2)*(x-dalej.getX()-dalej.getWidth()/2) + (y-upY((int)dalej.getY())+dalej.getHeight()/2)*(y-upY((int)dalej.getY())+dalej.getHeight()/2) < dalej.getWidth()/2*dalej.getWidth()/2){
<<<<<<< HEAD
            dane = new Double[dlugosc];
            int flag=0;
            for(int i = 0; i<=dlugosc; i++){
                try {
                    dane[i]=Double.parseDouble(wspolczynniki.get(i).getText());

                } catch (NumberFormatException e) {
                    flag =1;
                    layout.setText(sofiaProSoftMedium46px , podajWspolczynniki.getText());
                    blad.setPosition(Polynomial.WIDTH/2-layout.width/2 , 100);
                    addActor(blad);
                }
=======
            dane = new Double[dlugosc+1];
            for(int i = dlugosc; i>=0; i--){

>>>>>>> refs/remotes/piotrekm7/master
            //    if(znak[i].getText().equals("-"))


            }
            if(flag==0) startEndAnimationAndPushNewState(new MiejscaZerowe(gsm));

        }


    }

    @Override
    public void render(SpriteBatch sb) {
        act();
        draw();
        drawShadow();
    }
}
