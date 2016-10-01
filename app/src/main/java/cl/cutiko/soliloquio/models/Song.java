package cl.cutiko.soliloquio.models;

/**
 * Created by cutiko on 31-08-16.
 */
public class Song {

    private String name, file;

    public Song(String name, String file) {
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public String getFile() {
        return file;
    }
}
