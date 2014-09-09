package com.turtlegames.noliferadio;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.turtlegames.noliferadio.interfaces.Player;

public class NoLifeRadio extends ApplicationAdapter {
    private SpriteBatch spriteBatch;
    private Stage stage;
    private ImageButton buttonPlay;
    private Skin skin;
    private Player player;
    private Label state;
    private Table table;

    private ClickListener cickListener;

    public NoLifeRadio(Player player) {
        if (player != null)
            this.player = player;
    }

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        table.pad(30);
        stage.addActor(table);

        prepareTitle();
        preparePlayButton();
        prepareTree();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0.1f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();

        stage.draw();

        //table.debug();

        spriteBatch.end();
    }

    private void prepareTitle() {
        table.add(new Image(new Texture(Gdx.files.internal("data/title1.png")))).align(Align.left);
        table.add(new Image(new Texture(Gdx.files.internal("data/title2.png")))).align(Align.right);

        table.row().spaceBottom(20).spaceTop(20);
    }

    private void preparePlayButton() {
        Texture playTexture = new Texture(Gdx.files.internal("data/play.png"));
        Texture stopTexture = new Texture(Gdx.files.internal("data/stop.png"));

        TextureRegion playTexReg = new TextureRegion(playTexture);
        TextureRegion stopTexReg = new TextureRegion(stopTexture);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle(skin.get(Button.ButtonStyle.class));
        style.imageUp = new TextureRegionDrawable(playTexReg);
        style.imageChecked = new TextureRegionDrawable(stopTexReg);

        buttonPlay = new ImageButton(style);
        buttonPlay.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO desktop play not working
                String url = "http://radio.nolife-radio.com:9000/stream";
                player.play(url, state, songName);
            }
        });

        Table _table = new Table(skin);
        _table.add(buttonPlay);
        state = new Label("...IDLE...", skin);
        _table.add(new Label("Status: ", skin), state);
        table.add(_table).align(Align.left);
        table.add();
        table.row();
    }

    private void prepareTree() {
        final Tree tree = new Tree(skin);

        //button now playing
        ImageButton.ImageButtonStyle nowPlayingStyle = new ImageButton.ImageButtonStyle(/*skin.get(Button.ButtonStyle.class)*/);
        nowPlayingStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/nowPlayingFolded.png"))));
        nowPlayingStyle.imageChecked = new TextureRegionDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/nowPlayingUnFolded.png")))));
        final Tree.Node nowPlayingNode = new Tree.Node(new ImageButton(nowPlayingStyle));
        //set listener
        nowPlayingNode.getActor().addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (!nowPlayingNode.isExpanded())
                    nowPlayingNode.setExpanded(true);
                else
                    nowPlayingNode.setExpanded(false);
            }
        });

        ImageButton.ImageButtonStyle chatStyle = new ImageButton.ImageButtonStyle(/*skin.get(Button.ButtonStyle.class)*/);
        chatStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/chatFolded.png"))));
        chatStyle.imageChecked = new TextureRegionDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/chatUnFolded.png")))));
        final Tree.Node chatNode = new Tree.Node(new ImageButton(chatStyle));

        chatNode.getActor().addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (!chatNode.isExpanded())
                    chatNode.setExpanded(true);
                else
                    chatNode.setExpanded(false);
            }
        });

        ImageButton.ImageButtonStyle aboutStyle = new ImageButton.ImageButtonStyle(/*skin.get(Button.ButtonStyle.class)*/);
        aboutStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/aboutFolded.png"))));
        aboutStyle.imageChecked = new TextureRegionDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/aboutUnFolded.png")))));
        final Tree.Node aboutNode = new Tree.Node(new ImageButton(aboutStyle));

        aboutNode.getActor().addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (!aboutNode.isExpanded())
                    aboutNode.setExpanded(true);
                else
                    aboutNode.setExpanded(false);
            }
        });

        final Tree.Node moo4 = new Tree.Node(new TextButton("moo4", skin));

        tree.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){

            }
        });

        tree.add(nowPlayingNode);
        tree.add(chatNode);
        tree.add(aboutNode);
        chatNode.add(moo4);

        table.add(tree).fill().expand().colspan(2);
    }
}