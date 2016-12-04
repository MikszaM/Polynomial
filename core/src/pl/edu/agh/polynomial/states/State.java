package pl.edu.agh.polynomial.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import pl.edu.agh.polynomial.Polynomial;


/**
 * Created by Piotr Muras on 03.12.2016.
 */

abstract class State extends Stage {
    protected GameStateManager gsm;
    private Vector2 touch;
    protected float alpha;
    private ShapeRenderer shapeRenderer;
    protected boolean loaded=false;
    private float shadowAnimationFrameTime = 0.025f;
    private OrthographicCamera cam;

    State(GameStateManager gsm) {
        super(new FillViewport(Polynomial.WIDTH, Polynomial.HEIGHT));
        touch = new Vector2();
        alpha = 1;
        shapeRenderer = new ShapeRenderer();
        this.gsm = gsm;
        cam = new OrthographicCamera();
        cam.setToOrtho(false,Polynomial.WIDTH,Polynomial.HEIGHT);
        getViewport().setCamera(cam);
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        super.touchDown(screenX, screenY, pointer, button);
        getViewport().unproject(touch.set(screenX , screenY));
        handleInput(touch.x , upY((int)touch.y));
        return true;
    }



    protected void startEnterAnimation(){
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                alpha -= 0.025;
            }
        } , 0.1f , shadowAnimationFrameTime , 39);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                loaded=true;
            }
        } , shadowAnimationFrameTime*40);
    }
    protected void startEndAnimationAndPushNewState(final State state){
        alpha=0;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                alpha += 0.025;
            }
        } , 0.f , shadowAnimationFrameTime , 39);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                gsm.push(state);
                state.startEnterAnimation();
            }
        } , shadowAnimationFrameTime*40);
    }
    protected void startEndAnimationAndPopState(){
        alpha=0;

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                alpha += 0.025;
            }
        } , 0.f , shadowAnimationFrameTime , 39);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                gsm.pop();
                gsm.peek().startEnterAnimation();
                Gdx.input.setInputProcessor(gsm.peek());
            }
        } , shadowAnimationFrameTime*40);
    }

    protected void drawShadow(){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0,0,0,alpha));
        shapeRenderer.rect(0,0, 5000 , 5000);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }



    public abstract void handleInput(float x , float y);
    public  void update(float dt){
        getViewport().update(Gdx.graphics.getWidth() , Gdx.graphics.getHeight());
    }
    public abstract void  render(SpriteBatch sb);
    public void dispose(){
        super.dispose();
    }
    protected int upY(int y){
        return Polynomial.HEIGHT - y;
    }
}
