package userAndArtifacts;

import java.io.Serializable;

/**
 * Created by tobiaj on 2016-11-10.
 */
public class Artifacts implements Serializable {

    private int id;
    private String name;
    private String creator;
    private int dateOfCreation;
    private String placeOfCreation;
    private Genre genre;

    public enum Genre {
        Painting,
        Pictures,
        Sculpture,
        Photos

    }

    public Artifacts(){
        double randomNumber = Math.random();
        double rand = randomNumber * 100;
        id = (int) rand;
        name = "Thing " + id;
        creator = "creator " + id;
        dateOfCreation = (int) (Math.random() * 2000);
        placeOfCreation = "Place " + id;

        int chooseGenre = (int) (Math.random() * 3);
        genre = Genre.values()[chooseGenre];
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(int dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getPlaceOfCreation() {
        return placeOfCreation;
    }

    public void setPlaceOfCreation(String placeOfCreation) {
        this.placeOfCreation = placeOfCreation;
    }

}
