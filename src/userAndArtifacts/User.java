package userAndArtifacts;

import java.io.Serializable;

/**
 * Created by tobiaj on 2016-11-10.
 */
public class User implements Serializable{
    private int age;
    private Name name;
    private Gender gender;
    private Occupasion occupasion;
    private Artifacts.Genre interest;
    private int yearInterest;

    private enum Gender {
        male,
        female
    }

    private enum Name {
        olle,
        pelle,
        kalle,
        lisa,
        kajsa

    }

    private enum Occupasion {
        economic,
        engineer,
        journalist,
        athlete,
        criminal
    }


    public User(){
        int chooseGender = (int) (Math.random() * 1);
        int chooseOccupasion = (int) (Math.random() * 4);
        int chooseGenre = (int) (Math.random() * 3);


        age = (int) (Math.random() * 100);
        yearInterest = (int) (Math.random() * 500) + 1500;

        gender = Gender.values()[chooseGender];
        occupasion = Occupasion.values()[chooseOccupasion];

        interest = Artifacts.Genre.values()[chooseGenre];

        name = Name.values()[chooseOccupasion];

    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Artifacts.Genre getInterest() {
        return interest;
    }

    public void setInterest(Artifacts.Genre interest) {
        this.interest = interest;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public int getYearInterest() {
        return yearInterest;
    }

    public void setYearInterest(int yearInterest) {
        this.yearInterest = yearInterest;
    }

    public Occupasion getOccupasion() {
        return occupasion;
    }

    public void setOccupasion(Occupasion occupasion) {
        this.occupasion = occupasion;
    }

    public static void main(String[] args) {
        User user = new User();
        System.out.println(user.age + " " + user.name + " " + user.interest);
    }
}
