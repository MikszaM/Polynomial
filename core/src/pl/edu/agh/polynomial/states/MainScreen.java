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

import static pl.edu.agh.polynomial.Polynomial.skin;

/**
 * Created by Piotr Muras on 04.12.2016.
 */

public class MainScreen extends State {

    private BitmapFont sofiaProSoftMedium46px = new BitmapFont(Gdx.files.internal("SofiaProSoftMedium46px.fnt"));
    private Label podajWspolczynniki = new Label("Podaj współczynniki wielomianu" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));
    private GlyphLayout layout = new GlyphLayout(); // do mierzenia długości tekstu w px

    private Label zero = new Label("=0" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));

    private static Array<TextField> wspolczynniki;

    public static Array<TextField> getWspolczynniki() {
        return wspolczynniki;
    }

    public static Double dane[] ;

    public static Double[] getDane() { return dane; }

    private Label[] potegi;

    private Label[] xi;

    private Label[] znak;

    private int dlugosc;

    private Image dalej;

    private Label blad1 = new  Label("Błąd! \n Podałeś błedne dane" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));
    private Label blad2 = new  Label("Błąd! \n Współczynnik przy najwyższej \n potędze musi być różny od zera" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));
    private Image bg;



    MainScreen(GameStateManager gsm , final int stopienWielomianu){
        super(gsm);
        bg = new Image(skin.getDrawable("bg"));
        addActor(bg);
        wspolczynniki = new Array<TextField>();
        dlugosc = stopienWielomianu;
        layout.setText(sofiaProSoftMedium46px , podajWspolczynniki.getText());
        podajWspolczynniki.setPosition(Polynomial.WIDTH/2-layout.width/2 , upY(75));
        addActor(podajWspolczynniki);


        TextField.TextFieldStyle tStyle = new TextField.TextFieldStyle();
        tStyle.fontColor = Color.RED;
        tStyle.font = sofiaProSoftMedium46px;
        tStyle.background =  Polynomial.skin.getDrawable("ramka");



        xi = new Label[stopienWielomianu+1];
        znak = new Label[stopienWielomianu+1];
        potegi = new Label[stopienWielomianu+1];

        for(int i=0; i<stopienWielomianu+1; i++){
            TextField wspolczynnik = new TextField("",tStyle);
            wspolczynnik.setMessageText("1");
            if(i==stopienWielomianu) wspolczynnik.setMessageText("0");
            int h = i / 5;
            int w = i % 5;
            wspolczynnik.setPosition(190*w+10,upY(h*75+150));
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
            potegi[i].setPosition(190*w+wspolczynnik.getWidth()/2+80,upY(h*75+130));

            String pisz;
            if(stopienWielomianu-i != 0) pisz = "x";
            else pisz = " ";
            xi[i] = new Label(pisz, new Label.LabelStyle(sofiaProSoftMedium46px,Color.BLACK));
            layout.setText(sofiaProSoftMedium46px , xi[i].getText());
            xi[i].setPosition( 190*w+wspolczynnik.getWidth()/2+60,upY(h*75+150) );

            znak[i]=new Label("+", new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));
            layout.setText(sofiaProSoftMedium46px , znak[i].getText());
            znak[i].setPosition( 190*w+wspolczynnik.getWidth()/2-60,upY(h*75+150) );
            if (i==0) znak[i].setText("");
            if(i==stopienWielomianu){
                layout.setText(sofiaProSoftMedium46px , zero.getText());
                zero.setPosition( 190*w+wspolczynnik.getWidth()/2+60,upY(h*75+150));
            }
        }

        Iterator<TextField> iter = wspolczynniki.iterator();
        while(iter.hasNext()){
            TextField wspolczynnik = iter.next();
            addActor(wspolczynnik);
        }


        for(Label i:xi){
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
        int miejsce;
        if(stopienWielomianu>=22) miejsce=-30;
        else miejsce =0;
        dalej.setPosition(Polynomial.WIDTH/2-dalej.getWidth()/2,miejsce);
        addActor(dalej);



        Gdx.input.setInputProcessor(this);
        startEnterAnimation();


    }



    @Override
    public void handleInput(float x, float y) {
        if((x-dalej.getX()-dalej.getWidth()/2)*(x-dalej.getX()-dalej.getWidth()/2) + (y-upY((int)dalej.getY())+dalej.getHeight()/2)*(y-upY((int)dalej.getY())+dalej.getHeight()/2) < dalej.getWidth()/2*dalej.getWidth()/2){

            int flag=0;

            dane = new Double[dlugosc+1];
            for(int i = dlugosc; i>=0; i--){
                try {
                    if(wspolczynniki.get(i).getText().isEmpty()) {
                        if(i==dlugosc) dane[i]=0.0;
                        else dane[i]=1.0;
                    }
                    else dane[i]=Double.parseDouble(wspolczynniki.get(i).getText().replaceAll(",","."));

                    if(znak[i].getText().toString().equals("-")) dane[i]=-dane[i];

                } catch (NumberFormatException e) {
                    flag =1;
                    }

            }
            layout.setText(sofiaProSoftMedium46px , blad1.getText());
            blad1.setPosition(Polynomial.WIDTH -layout.width-10, -10);
            blad1.setFontScale(0.6f);
            blad1.setAlignment(2);
            addActor(blad1);
            blad1.setVisible(false);
            layout.setText(sofiaProSoftMedium46px , blad2.getText());
            blad2.setPosition(-layout.width/4+30, -layout.height/4-10);
            blad2.setAlignment(2);
            blad2.setFontScale(0.6f);
            addActor(blad2);
            blad2.setVisible(false);

            if(flag==1){
                blad1.setVisible(true);
            }
            else blad1.setVisible(false);

            if(dane[0]==0.0){
                blad2.setVisible(true);
                }
            else blad2.setVisible(false);
            if(flag==0&&dane[0]!=0.0) {

                for(int i = dlugosc; i>=0; i--){
                   wspolczynniki.get(i).setText("");
                    wspolczynniki.get(i).setVisible(false);
                    znak[i].setText("");
                    potegi[i].setText("");
                    xi[i].setText("");
                    }
                    addActor(bg);
                startEndAnimationAndPushNewState(new MenuScreen(gsm));
                }



        }


    }

    @Override
    public void render(SpriteBatch sb) {
        act();
        draw();
        drawShadow();
    }
}
