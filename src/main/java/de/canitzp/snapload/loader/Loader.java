package de.canitzp.snapload.loader;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.ITweaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;

/**
 * @author canitzp
 */
public class Loader {
    public static final List<IClassTransformer> classTransformer = new ArrayList<>();
    public static final List<Mod> loadedMods = new ArrayList<>();
    public static final Map<String, ZipFile> modFiles = new HashMap<>();
    public static final Map<String, ITweaker> tweaker = new HashMap<>();
    public static final boolean client = Loader.class.getClassLoader().getResourceAsStream("net/minecraft/client/main/Main.class") != null;

    public static void registerClassTransformer(IClassTransformer transformer) {
        if (!classTransformer.contains(transformer)) {
            classTransformer.add(transformer);
        }
    }

    public static Side getSide() {
        return client ? Side.CLIENT : Side.SERVER;
    }

}
