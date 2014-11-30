import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Album {
    private String author, album;
    private int year;

    public Album(JSONObject args){
        author = (String) args.get("author");
        album = (String) args.get("album");
        try{
            year = Integer.parseInt((String) args.get("year"));
        } catch (NumberFormatException e){
            year = 1994;
        }
    }

    @Override
    public boolean equals(Object other){
        if(!(other instanceof Album))
            return false;
        return this.album.equals(((Album) other).album)
             &&this.author.equals(((Album) other).author)
             &&this.year==((Album) other).year;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public JSONObject toJSON(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("author", this.author);
        map.put("album", this.album);
        map.put("year", String.valueOf(this.year));
        return new JSONObject(map);
    }
}
