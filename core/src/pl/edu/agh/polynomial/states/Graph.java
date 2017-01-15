package pl.edu.agh.polynomial.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
import static java.lang.Math.pow;
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

    private Label sxp = new Label("1" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));
    private Label sxm = new Label("-1" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));
    private Label syp = new Label("10" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));
    private Label sym = new Label("-10" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));

    private Label sxppow = new Label("1" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));
    private Label sxmpow = new Label("-1" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));
    private Label syppow = new Label("10" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));
    private Label sympow = new Label("-10" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));

    private GlyphLayout layout = new GlyphLayout(); // do mierzenia długości tekstu w px

    private double xmin;
    private double xmax;
    private double h1;
    private double h2;
    private double ymin;
    private double ymax;
    private double przedzial;
    private double n1;
    private double n2;
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
          //  System.out.println(root.get(j));
            values.add(f(root.get(j)));
        }
        xmin=root.get(0);
        xmax=root.get(root.size()-1);

        przedzial=max(abs(4*xmin) , abs(4*xmax));
        if(przedzial==0){
            przedzial=10;
        }

        n1=Math.ceil(Math.log10(przedzial/2));
        System.out.println(przedzial/2);
        System.out.println((Math.pow(10,n1-1)));

        h1=przedzial/200;

        values.add(f(-100*h1));
        values.add(f(100*h1));

        Collections.sort(values);

        for(int j=0;j<values.size();j++){
          //  System.out.println(values.get(j));
        }

        ymin=values.get(0);
        ymax=values.get(values.size()-1);



        h2=max(Math.abs(ymax),Math.abs(ymin));
        n2=Math.ceil(Math.log10(h2));
      //  System.out.println(h2);
       // System.out.println(n2);
       // System.out.println((Math.pow(10,n2-1)));

        wstecz=new Image(Polynomial.skin.getDrawable("wstecz"));
        wstecz.setPosition(0,0);
        addActor(wstecz);

        if(n1-1==0) {
            sxp.setText("1");
            sxm.setText("-1");
        }
        else {
            sxp.setText("10");
            sxm.setText("-10");
        }
        if(n2-1==0) {
            syp.setText("1");
            sym.setText("-1");
        }
        else {
            syp.setText("10");
            sym.setText("-10");
        }
        if(n1-1==1||n1-1==0){
            sxppow.setText(" ");
            sxmpow.setText(" ");
        }
        else{
            sxppow.setText(Integer.toString((int)(n1-1)));
            sxmpow.setText(Integer.toString((int)(n1-1)));
        }
        if(n2-1==1||n2-1==0){
            syppow.setText(" ");
            sympow.setText(" ");
        }
        else{
            syppow.setText(Integer.toString((int)(n2-1)));
            sympow.setText(Integer.toString((int)(n2-1)));
        }
        layout.setText(sofiaProSoftMedium46px, sxp.getText());
        sxp.setPosition((float) (Polynomial.WIDTH/2+(Math.pow(10,n1-1)/(przedzial/2))*Polynomial.WIDTH/2-layout.width/2) , (Polynomial.HEIGHT/2-60));
        addActor(sxp);

        sxppow.setPosition((float) (Polynomial.WIDTH/2+(Math.pow(10,n1-1)/(przedzial/2))*Polynomial.WIDTH/2-layout.width/2+55) , (Polynomial.HEIGHT/2-45));
        sxppow.setFontScale(0.5f);
        addActor(sxppow);


        layout.setText(sofiaProSoftMedium46px, sxm.getText());
        sxm.setPosition((float) (Polynomial.WIDTH/2-(Math.pow(10,n1-1)/(przedzial/2))*Polynomial.WIDTH/2-layout.width/2) , (Polynomial.HEIGHT/2-60));
        addActor(sxm);

        sxmpow.setPosition((float) (Polynomial.WIDTH/2-(Math.pow(10,n1-1)/(przedzial/2))*Polynomial.WIDTH/2-layout.width/2+70) , (Polynomial.HEIGHT/2-45));
        sxmpow.setFontScale(0.5f);
        addActor(sxmpow);
////////////////////////
        layout.setText(sofiaProSoftMedium46px, syp.getText());
        syp.setPosition((Polynomial.WIDTH/2-87) , (float) (Polynomial.HEIGHT/2+(Math.pow(10,n2-1)/(h2))*Polynomial.HEIGHT/2)-layout.height);
        addActor(syp);

        syppow.setPosition((Polynomial.WIDTH/2-30) , (float) (Polynomial.HEIGHT/2+(Math.pow(10,n2-1)/(h2))*Polynomial.HEIGHT/2)-layout.height+10);
        syppow.setFontScale(0.5f);
        addActor(syppow);

        layout.setText(sofiaProSoftMedium46px, sym.getText());
        sym.setPosition((Polynomial.WIDTH/2-100) , (float) (Polynomial.HEIGHT/2-(Math.pow(10,n2-1)/(h2))*Polynomial.HEIGHT/2)-layout.height);
        addActor(sym);

        sympow.setPosition((Polynomial.WIDTH/2-30) , (float) (Polynomial.HEIGHT/2-(Math.pow(10,n2-1)/(h2))*Polynomial.HEIGHT/2)-layout.height+10);
        sympow.setFontScale(0.5f);
        addActor(sympow);






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

        sr.rectLine(w/2,0,w/2,h,5);
        sr.rectLine(0,h/2,w,h/2,5);

        sr.rectLine(30*w/31,20*h/42,w,h/2,5);
        sr.rectLine(30*w/31,22*h/42,w,h/2,5);

        sr.rectLine(30*w/62,20*h/21,w/2,h,5);
        sr.rectLine(32*w/62,20*h/21,w/2,h,5);

        for(double i=-100; i<100;i++){
            sr.setColor(Color.BLUE);
            sr.rectLine((float) (w*i/200+w/2) ,(float) (f(i*h1)*h/2/h2+h/2),(float) (w*(i+1)/200+w/2),(float) (f((i+1)*h1)*h/2/h2+h/2),5);
        }
        sr.setColor(Color.RED);

        sr.rectLine((float) (w/2+(Math.pow(10,n1-1)/(przedzial/2))*w/2) , (h/2+10),(float) (w/2+(Math.pow(10,n1-1)/(przedzial/2))*w/2), h/2-10,5);
        sr.rectLine((float) (w/2-(Math.pow(10,n1-1)/(przedzial/2))*w/2) , (h/2+10),(float) (w/2-(Math.pow(10,n1-1)/(przedzial/2))*w/2), h/2-10,5);

        sr.rectLine((w/2+10) , (float) (h/2+(Math.pow(10,n2-1)/(h2))*h/2),(w/2-10), (float) (h/2+(Math.pow(10,n2-1)/(h2))*h/2),5);
        sr.rectLine((w/2+10) , (float) (h/2-(Math.pow(10,n2-1)/(h2))*h/2),(w/2-10), (float) (h/2-(Math.pow(10,n2-1)/(h2))*h/2),5);


        sr.end();
    }
}
