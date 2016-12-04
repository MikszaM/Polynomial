package pl.edu.agh.polynomial;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.BufferUtils;

import java.nio.IntBuffer;

import pl.edu.agh.polynomial.states.GameStateManager;
import pl.edu.agh.polynomial.states.StartScreen;

public class Polynomial extends ApplicationAdapter {
	public static int WIDTH = 960;
	public static int HEIGHT = 540;
	public static final String TITLE = "Polynomial Solver";
	private static GameStateManager gsm;
	private SpriteBatch batch;
	public static Skin skin;

	@Override
	public void create() {
		skin = new Skin(new TextureAtlas("texture.pack"));
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		Gdx.gl.glClearColor(1, 1, 0, 1);
		gsm.push(new StartScreen(gsm));
		IntBuffer intBuffer = BufferUtils.newIntBuffer(16);
		Gdx.gl20.glGetIntegerv(GL20.GL_MAX_TEXTURE_SIZE, intBuffer);
		System.out.println(intBuffer.get());
	}


	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}
}
