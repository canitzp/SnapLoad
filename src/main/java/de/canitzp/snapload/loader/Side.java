package de.canitzp.snapload.loader;

/**
 * @author canitzp
 */
public enum Side {
    CLIENT,
    SERVER;

    public boolean client() {
        return this == CLIENT;
    }

    public boolean server() {
        return this == SERVER;
    }
}
