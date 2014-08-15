package com.turtlegames.noliferadio;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class NoLifeRadio extends ApplicationAdapter
{
	private SpriteBatch spriteBatch;
	private Stage stage;
	private ImageButton buttonPlay;
	private Skin skin;

	@Override
	public void create()
	{
		spriteBatch = new SpriteBatch();
		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		buttonPlay = new ImageButton((Drawable) Gdx.files.internal("data/play.png"));
		//		buttonPlay.setWidth(64f);
		//		buttonPlay.setHeight(64f);
		buttonPlay.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

		stage.addActor(buttonPlay);
	}

	@Override
	public void render()
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spriteBatch.begin();
		stage.draw();
		spriteBatch.end();
	}
}
