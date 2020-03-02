package sample;

/*Record Class
* This clasee is intended to store the individual scores and the owner of the pair of files compared
* name - String variable that stores the name of the owner of the files
* score - int double variable that stores the resultant score
* */

public class Record {
    private String name;
    private double score;

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }

}
