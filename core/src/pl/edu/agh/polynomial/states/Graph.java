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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import pl.edu.agh.polynomial.Polynomial;
import sun.applet.Main;


import static java.lang.Math.abs;
import static java.lang.Math.max;
import static pl.edu.agh.polynomial.Polynomial.skin;
import static pl.edu.agh.polynomial.states.MiejscaZerowe.*;

/**
 * Created by MichałM on 2016-12-16.
 */

public class Graph extends State {
    public OrthographicCamera camera;
    private Image bg;
    private BitmapFont sofiaProSoftMedium46px = new BitmapFont(Gdx.files.internal("SofiaProSoftMedium46px.fnt"));
    private Label x = new Label("x" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));
    private Label y =new Label("y" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));
    private ArrayList<Double> root=new ArrayList<Double>();
    private ArrayList<Double> values=new ArrayList<Double>();
    private Image wstecz;

    private double xmin;
    private double xmax;
    private double h1;
    private double h2;
    private double ymin;
    private double ymax;
    private double przedzial;
    double f(double x){
        Double y=0.0;
        for(int i=0;i<MainScreen.getDane().length;i++){
            y= y+MainScreen.getDane()[i]*Math.pow(x,MainScreen.getDane().length-i-1);
        }
        return y;
    }

    public Graph(GameStateManager gsm) {
        super(gsm);
        bg = new Image(skin.getDrawable("bg"));
        addActor(bg);

        x.setPosition(Gdx.graphics.getWidth()-50,Gdx.graphics.getHeight()/2-50);
        y.setPosition(Gdx.graphics.getWidth()/2-50,Gdx.graphics.getHeight()-50);
        addActor(x);
        addActor(y);


        MiejscaZerowe.licz();

        for(int i=0;i<MiejscaZerowe.getRoots().length;i++) {
            if (Math.round(MiejscaZerowe.getRoots()[i].getImaginary()*100)/100==0.0 )
                root.add( (Math.round(MiejscaZerowe.getRoots()[i].getReal()*100.0)/100.0));
        }

        for(int i=0;i<MiejscaZerowe.getDroots().length;i++) {
            if (Math.round(MiejscaZerowe.getDroots()[i].getImaginary()*100)/100 == 0.0)
                root.add(Math.round(MiejscaZerowe.getDroots()[i].getReal()*100.0)/100.0);
        }
       Collections.sort(root);

        for(int j=0;j<root.size();j++){
            System.out.println(root.get(j));
            values.add(f(root.get(j)));
        }
        xmin=root.get(0);
        xmax=root.get(root.size()-1);

        przedzial=max(1, max(abs(4*xmin) , abs(4*xmax)));

        h1=przedzial/200;

        values.add(f(-100*h1));
        values.add(f(100*h1));

        Collections.sort(values);

        for(int j=0;j<values.size();j++){
            System.out.println(values.get(j));
        }

        ymin=values.get(0);
        ymax=values.get(values.size()-1);



        h2=max(Math.abs(ymax),Math.abs(ymin));


        wstecz=new Image(Polynomial.skin.getDrawable("wstecz"));
        wstecz.setPosition(0,0);
        addActor(wstecz);
        Gdx.input.setInputProcessor(this);
    }



    @Override
    public void handleInput(float x, float y) {
        if((x-wstecz.getX()-wstecz.getWidth()/2)*(x-wstecz.getX()-wstecz.getWidth()/2) + (y-upY((int)wstecz.getY())+wstecz.getHeight()/2)*(y-upY((int)wstecz.getY())+wstecz.getHeight()/2) < wstecz.getWidth()/2*wstecz.getWidth()/2) {

            startEndAnimationAndPopState();
        }
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

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        sr.begin(ShapeRenderer.ShapeType.Filled);

        sr.rectLine(w/2,0,w/2,h,1);
        sr.rectLine(0,h/2,w,h/2,1);

        sr.rectLine(30*w/31,20*h/42,w,h/2,1);
        sr.rectLine(30*w/31,22*h/42,w,h/2,1);

        sr.rectLine(30*w/62,20*h/21,w/2,h,1);
        sr.rectLine(32*w/62,20*h/21,w/2,h,1);

        for(double i=-100; i<100;i++){
            sr.setColor(Color.BLUE);
            sr.rectLine((float) (w*i/200+w/2) ,(float) (f(i*h1)*h/2/h2+h/2),(float) (w*(i+1)/200+w/2),(float) (f((i+1)*h1)*h/2/h2+h/2),1);
        }


        sr.end();
    }
}
