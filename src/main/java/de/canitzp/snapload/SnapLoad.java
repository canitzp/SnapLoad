package de.canitzp.snapload;

import de.canitzp.snapload.loader.Loader;
import de.canitzp.snapload.loader.Mod;
import de.canitzp.snapload.loader.SnapMod;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author canitzp
 */
public class SnapLoad implements ITweaker {

    public final List<String> arguments = new ArrayList<>();
    public File gameDir;
    public File assetsDir;
    public File modsDir;

    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        this.gameDir = gameDir;
        this.assetsDir = assetsDir;
        this.modsDir = new File(gameDir, "snap");
        if (!this.modsDir.exists()) {
            this.modsDir.mkdirs();
        }

        this.arguments.clear();
        this.arguments.addAll(args);
        this.arguments.set(this.arguments.indexOf("custom"), "SnapLoad");
        if (profile != null) {
            this.arguments.add("--version");
            this.arguments.add(profile);
        }

        if (assetsDir != null) {
            this.arguments.add("--assetsDir");
            this.arguments.add(assetsDir.getPath());
        }

        File[] files = this.modsDir.listFiles((dir, name) -> name.endsWith(".jar") || name.endsWith(".zip"));
        if (files != null) {
            File[] var6 = files;
            File file;
            for(int i  = 0; i < files.length; i++) {
                file = var6[i];
                try {
                    Launch.classLoader.addURL(file.toURI().toURL());
                } catch (MalformedURLException var15) {
                    var15.printStackTrace();
                }
            }

            var6 = files;

            for(int i = 0; i < files.length; i++) {
                file = var6[i];
                try {
                    ZipFile jar = new ZipFile(file);
                    Enumeration entries = jar.entries();
                    while(entries.hasMoreElements()) {
                        ZipEntry entry = (ZipEntry)entries.nextElement();
                        if (entry.getName().endsWith(".class") && !entry.getName().contains("$")) {
                            Class clazz = Class.forName(entry.getName().substring(0, entry.getName().length() - 6).replace("/", "."), false, Launch.classLoader);
                            if (clazz.isAnnotationPresent(SnapMod.class)) {
                                SnapMod mod = (SnapMod)clazz.getAnnotation(SnapMod.class);
                                Loader.loadedMods.add(new Mod(mod.modid(), mod.name(), mod.version(), mod.author(), mod.sides()));
                                Loader.modFiles.put(mod.modid(), jar);
                                if (ITweaker.class.isAssignableFrom(clazz)) {
                                    Loader.tweaker.put(mod.modid(), (ITweaker)clazz.newInstance());
                                }
                            }
                        }
                    }
                } catch (Exception var16) {
                    var16.printStackTrace();
                }
            }
        }

        System.out.println(Loader.loadedMods);
        System.out.println(Loader.tweaker);
        for (ITweaker tweaker : Loader.tweaker.values()) {
            tweaker.acceptOptions(this.arguments, gameDir, assetsDir, profile);
        }
    }

    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        for (ITweaker tweaker : Loader.tweaker.values()) {
            tweaker.injectIntoClassLoader(classLoader);
        }
    }

    public String getLaunchTarget() {
        return Loader.getSide().client() ? "net.minecraft.client.main.Main" : "net.minecraft.server.MinecraftServer";
    }

    public String[] getLaunchArguments() {
        return this.arguments.toArray(new String[0]);
    }

}
