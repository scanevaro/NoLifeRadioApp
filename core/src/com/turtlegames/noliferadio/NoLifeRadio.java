package com.turtlegames.noliferadio;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.turtlegames.noliferadio.interfaces.Player;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class NoLifeRadio extends ApplicationAdapter {
    private SpriteBatch spriteBatch;
    private Stage stage;
    private ImageButton buttonPlay;
    private Skin skin;
    private Player player;
    private Label state;
    private Table table;

    private Tree.Node nowPlayingNode;

    public NoLifeRadio(Player player) {
        if (player != null)
            this.player = player;
    }

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();

        //prepare UI
        {
            stage = new Stage(new StretchViewport(480, 800));
            skin = new Skin(Gdx.files.internal("data/uiskin.json"));

            table = new Table();
            table.setFillParent(true);
            table.pad(5);
            stage.addActor(table);

            prepareTitle();
            preparePlayButton();
            prepareTree();
            prepareDonateButton();
        }

        //get track name
        getNowPlaying();

        //set Input Processor
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        //Clear Screen
        {
            Gdx.gl.glClearColor(0, 0.1f, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }

        //Draw UI
        {
            spriteBatch.begin();

            stage.draw();

            table.debug();

            spriteBatch.end();
        }
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
                player.play(url, state);
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
        nowPlayingStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/nowPlaying.png"))));
        nowPlayingNode = new Tree.Node(new ImageButton(nowPlayingStyle));
        nowPlayingNode.setExpanded(true);
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
        chatStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/chat.png"))));
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
        aboutStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/about.png"))));
        final Tree.Node aboutNode = new Tree.Node(new ImageButton(aboutStyle));

        aboutNode.getActor().addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (!aboutNode.isExpanded())
                    aboutNode.setExpanded(true);
                else
                    aboutNode.setExpanded(false);
            }
        });

        tree.add(nowPlayingNode);
        tree.add(chatNode);
        tree.add(aboutNode);

        final Tree.Node moo4 = new Tree.Node(new Label("In development...", skin));
        chatNode.add(moo4);

        final Tree.Node moo5 = new Tree.Node(new Label("In development...", skin));
        aboutNode.add(moo5);

        Label songName = new Label("", skin);
        songName.setColor(new Color(50, 205, 50, 0.5f));
        songName.setWidth(50);
        songName.setWrap(true);
        Table tableTest = new Table(skin);
        tableTest.add(songName).width(400);
        final Tree.Node songNameNode = new Tree.Node(tableTest);
        nowPlayingNode.add(songNameNode);

        table.add(tree).fill().expand().colspan(2);
    }

    private void prepareDonateButton() {
        table.row().spaceBottom(20).spaceTop(20);

        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO stop service
                Gdx.app.exit();
            }
        });

        TextButton donateButton = new TextButton("Donate!", skin);
        donateButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("http://pitchinbox.com/redirect.php?id=4314496656");
            }
        });

        table.add(exitButton).prefWidth(100);
        table.add(donateButton).prefWidth(100);

        table.row().spaceTop(5);
    }

    private void getNowPlaying() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        URL updateURL = new URL("http://radio.nolife-radio.com:9000/stream");
                        URLConnection conn = updateURL.openConnection();
                        conn.setRequestProperty("Icy-MetaData", "1");
                        int interval = Integer.valueOf(conn.getHeaderField("icy-metaint")); // You can get more headers if you wish. There in other useful data.

                        InputStream inputStream = conn.getInputStream();

                        int skipped = 0;
                        while (skipped < interval) {
                            skipped += inputStream.skip(interval - skipped);
                        }

                        int metadataLength = inputStream.read() * 16;

                        int bytesRead = 0;
                        int offset = 0;
                        byte[] bytes = new byte[metadataLength];

                        while (bytesRead < metadataLength && bytesRead != -1) {
                            bytesRead = inputStream.read(bytes, offset, metadataLength);
                            offset = bytesRead;
                        }

                        String metaData = new String(bytes).trim();
                        //parse song name
                        String song = metaData.substring(metaData.indexOf("=") + 2, metaData.indexOf(";") - 1);

                        //set song name in label
                        {
                            Table nodeTable = (Table) nowPlayingNode.getChildren().get(0).getActor();
                            Label label = (Label) nodeTable.getCells().get(0).getActor();
                            if (label.getText().toString().equals(""))
                                label.setText(song);
                            else if (!label.getText().toString().equals(song)) {
                                if (nowPlayingNode.getChildren().size == 4)
                                    nowPlayingNode.getChildren().removeIndex(3).remove();

                                Label newSong = new Label(song, skin);
                                newSong.setColor(new Color(50, 205, 50, 0.5f));
                                newSong.setWidth(50);
                                newSong.setWrap(true);
                                Table tableNewSong = new Table(skin);
                                tableNewSong.add(newSong).width(400);
                                final Tree.Node newSongNode = new Tree.Node(tableNewSong);
                                nowPlayingNode.insert(0, newSongNode);
                            }
                        }

                        inputStream.close();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}