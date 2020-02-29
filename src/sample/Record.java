package sample;

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

    public void setNameandScore(String name, double score){
        this.name = name;
        this.score = score;
    }
}
