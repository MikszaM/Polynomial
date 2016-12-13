package pl.edu.agh.polynomial.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.IntAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import pl.edu.agh.polynomial.Polynomial;

import static pl.edu.agh.polynomial.Polynomial.skin;

/**
 * Created by Piotr Muras on 03.12.2016.
 */

public class StartScreen extends State {

    private BitmapFont sofiaProSoftMedium46px = new BitmapFont(Gdx.files.internal("SofiaProSoftMedium46px.fnt"));
    private Label podajStopien = new Label("Podaj stopień wielomianu 1-24" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));
    private GlyphLayout layout = new GlyphLayout(); // do mierzenia długości tekstu w px
    private TextField stopien;
    //private SelectBox <Integer> stopien;
    private Image dalej;
    private Image bg;


    private Label blad1 = new  Label("Błąd! \n Podaj lliczbę z podanego zakresu" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));

    public StartScreen(GameStateManager gsm) {
        super(gsm);
        bg = new Image(skin.getDrawable("bg"));
        addActor(bg);
        layout.setText(sofiaProSoftMedium46px , podajStopien.getText());
        podajStopien.setPosition(Polynomial.WIDTH/2-layout.width/2 , upY(100));
        addActor(podajStopien);
        TextField.TextFieldStyle tStyle = new TextField.TextFieldStyle();
        tStyle.fontColor = Color.RED;
        tStyle.font = sofiaProSoftMedium46px;
        tStyle.background = Polynomial.skin.getDrawable("ramka1");
        stopien = new TextField("" , tStyle);
        stopien .setMessageText("0");
        stopien.setPosition(Polynomial.WIDTH/2-stopien.getWidth()/2,upY(200));
        stopien.setAlignment(Align.center);
        stopien.setMaxLength(2);
        stopien.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        addActor(stopien);
       /* SelectBox.SelectBoxStyle boxStyle = new SelectBox.SelectBoxStyle();
        boxStyle.fontColor = Color.BLACK;
        boxStyle.font = sofiaProSoftMedium46px;
        boxStyle.background = Polynomial.skin.getDrawable("ramka");
        boxStyle.scrollStyle = new ScrollPane.ScrollPaneStyle();
        boxStyle.listStyle = new List.ListStyle();
        boxStyle.listStyle.font = sofiaProSoftMedium46px;
        boxStyle.listStyle.fontColorSelected = Color.GOLD;
        boxStyle.listStyle.fontColorUnselected = Color.WHITE;
        boxStyle.listStyle.selection = Polynomial.skin.getDrawable("ramka");
        boxStyle.listStyle.background = Polynomial.skin.getDrawable("ramka");*/
        /*stopien = new SelectBox<Integer>(skin);
        stopien.setPosition(Polynomial.WIDTH/2-stopien.getWidth()/2,upY(200));


        Integer stopnie[] = new Integer[20];
        for(int i=1 ; i<21 ; i++){
            stopnie[i-1]=i;
        }
        stopien.setSelected(14);
        stopien.setItems(stopnie);
        addActor(stopien);*/
        dalej=new Image(Polynomial.skin.getDrawable("dalej"));
        dalej.setPosition(Polynomial.WIDTH/2-dalej.getWidth()/2,upY(400));
        addActor(dalej);
        Gdx.input.setInputProcessor(this);
        startEnterAnimation();
    }



    @Override
    public void handleInput(float x, float y){
        if((x-dalej.getX()-dalej.getWidth()/2)*(x-dalej.getX()-dalej.getWidth()/2) + (y-upY((int)dalej.getY())+dalej.getHeight()/2)*(y-upY((int)dalej.getY())+dalej.getHeight()/2) < dalej.getWidth()/2*dalej.getWidth()/2){
            if(!stopien.getText().isEmpty() && Integer.parseInt(stopien.getText())<=24&&Integer.parseInt(stopien.getText())>0){
                startEndAnimationAndPushNewState(new MainScreen(gsm , Integer.parseInt(stopien.getText())));
            }
            else{
                layout.setText(sofiaProSoftMedium46px , blad1.getText());
                blad1.setPosition(-layout.width/4+30, -layout.height/4-10);
                blad1.setFontScale(0.6f);
                blad1.setAlignment(2);
                addActor(blad1);
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

