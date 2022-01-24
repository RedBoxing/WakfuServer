package fr.redboxing.wakfu.server.models;

import fr.redboxing.wakfu.server.WakfuServer;

import java.io.File;
import java.util.ArrayList;

public class Player {

    private String name;
    private String displayName;
    private String password;
    private ArrayList<GameCharacter> characters = new ArrayList<GameCharacter>();

    public Player(String name, String displayName, String password) {
        this.name = name;
        this.displayName = displayName;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPassword() {
        return password;
    }

    public void addCharacter(GameCharacter character) {
        characters.add(character);
    }

    public ArrayList<GameCharacter> getCharacters() {
        return characters;
    }

    public void save() {
        WakfuServer.getInstance().getLogger().info(this.toString());
      /*  File f = new File("data/chars/"+name+".xml");

        XStream xs = new XStream();
        xs.registerConverter(new EnumConverter());

        xs.alias("player", Player.class);
        xs.alias("breed", Breed.class);
        xs.alias("character", GameCharacter.class);
        xs.alias("nation", Nation.class);
        xs.alias("appearance", Appearance.class);

        try {
            xs.toXML(this, new FileOutputStream(f));
            System.out.println(xs.toXML(this));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
    }

    public static Player load(String name) {
        Player player = new Player("RedBoxing", "RedBoxing", "123456");
    //    Appearance appearance = new Appearance(1, 5, 0, 2, 1, 4, 2, 0, -1);
       // GameCharacter character = new GameCharacter("RedBoxing", 1, 1, 0, Breed.ELIOTROPE, 62, Nation.NONE, appearance);
     //   player.addCharacter(character);

        return player;

     /*   File f = new File("data/chars/"+name+".xml");
        if (!f.exists())
            return null;

        XStream xs = new XStream();
        xs.alias("player", Player.class);
        xs.alias("breed", Breed.class);
        xs.alias("character", GameCharacter.class);
        xs.alias("nation", Nation.class);
        xs.alias("appearance", Appearance.class);
        return (Player) xs.fromXML(f);*/
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", password='" + password + '\'' +
                ", characters=" + characters +
                '}';
    }
}