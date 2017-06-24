package de.canitzp.snapload.loader;

/**
 * @author canitzp
 */
public class Mod {

    public String modid;
    public String name;
    public String version;
    public String author;
    public Side[] side;

    public Mod(String modid, String name, String version, String author, Side[] side) {
        this.modid = modid;
        this.name = name;
        this.version = version;
        this.author = author;
        this.side = side;
    }

    public String getModid() {
        return this.modid;
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }

    public String getAuthor() {
        return this.author;
    }

    public Side[] getSide() {
        return this.side;
    }

}
