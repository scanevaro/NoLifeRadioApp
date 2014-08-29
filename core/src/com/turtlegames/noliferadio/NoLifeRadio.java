package com.turtlegames.noliferadio;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.turtlegames.noliferadio.interfaces.Player;

public class NoLifeRadio extends ApplicationAdapter
{
	private SpriteBatch spriteBatch;
	private Stage stage;
	private ImageButton buttonPlay;
	private Skin skin;
	private Player player;

	public NoLifeRadio(Player player)
	{
		if (player != null)
			this.player = player;
	}

	@Override
	public void create()
	{
		spriteBatch = new SpriteBatch();
		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		Texture playTexture = new Texture(Gdx.files.internal("data/play.png"));
		Texture stopTexture = new Texture(Gdx.files.internal("data/stop.png"));

		TextureRegion playTexReg = new TextureRegion(playTexture);
		TextureRegion stopTexReg = new TextureRegion(stopTexture);

		ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle(skin.get(Button.ButtonStyle.class));
		style.imageUp = new TextureRegionDrawable(playTexReg);
		style.imageChecked = new TextureRegionDrawable(stopTexReg);

		buttonPlay = new ImageButton(style);
		buttonPlay.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		buttonPlay.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				//TODO desktop play not working
				String url = "http://radio.nolife-radio.com:9000/stream";
				player.play(url);
			}
		});

		stage.addActor(buttonPlay);

		Gdx.input.setInputProcessor(stage);
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