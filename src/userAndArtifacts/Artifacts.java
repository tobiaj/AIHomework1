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

        int chooseGenre = (int) (Math.random() * 4);
        genre = Genre.values()[chooseGenre];
    }
}
