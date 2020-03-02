package sample;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class Controller {
    public GridPane matrix;
    public Stage stage;
    public Button fileBtn;
    public Label fileLabel;
    public Button exitBtn;
    public Button compBtn;
    public String fileLoc;
    public ImageView legPic;
    public Button metricsBtn;

    public void initStage(Stage stage){
        this.stage = stage;
        legPic.setVisible(false);
    }

    public void openFile(){

        //File Selector Dialogue Box that appears when the user selected the Select File Path Button

        Stage stage1 = new Stage();
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select a folder");
        File dirFile = dirChooser.showDialog(stage1);
        if(dirFile!=null){
            fileLabel.setText(dirFile.getAbsolutePath() + " selected");
            fileLoc = dirFile.getAbsolutePath();
        }


    }

    public void exit(){
        stage.close();
    }

    public void compare() {
        if(fileLoc==null){
            System.out.println("No file selected");
        }
        else {
            matrix.getChildren().clear();
            File root = new File(fileLoc);
            File[] list = root.listFiles();
            assert list != null;

            //if file is empty then return
            if (list.length == 0) {
                fileLabel.setText("File Selected is Empty!");
                System.out.println("File is empty");
                return;
            }

             //Creates a new Class of Student and Record


            Student[] students = new Student[list.length];
            ArrayList<Record> records = new ArrayList<>();


            for (int i = 0; i < list.length; i++) {
                students[i] = new Student();
                students[i].setName(list[i].getName());
                System.out.println(students[i].getName());
                try {
                    find(list[i].getAbsolutePath(), students[i]);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            int sLength = list.length;

            for (int i = 1; i < sLength + 1; i++) {
                for (int k = 1; k < sLength + 1; k++) {
                    //this process the gridpane to show the rectangle boxes and score
                    String strScore;
                    Label scoreLabel = new Label();
                    StackPane stackPane = new StackPane();
                    double score = compareFiles(students[i - 1].getToken(), students[k - 1].getToken());
                    strScore = new DecimalFormat("#.##").format(score);
                    scoreLabel.setText(strScore);
                    stackPane.getChildren().addAll(recScore(score),scoreLabel);
                    matrix.add(stackPane, i, k);
                }
            }

            for (int i = 0; i < sLength; i++) {
                Label label = new Label("  " + students[i].getName());
                label.setFont(new Font("Arial", 10));
                label.setAlignment(Pos.CENTER);
                matrix.add(label, i + 1, 0);
            }

            for (int i = 0; i < sLength; i++) {
                Label label = new Label(students[i].getName());
                label.setFont(new Font("Arial", 10));
                label.setAlignment(Pos.CENTER);
                matrix.add(label, 0, i + 1);
            }
            legPic.setVisible(true);

            int j =0;
            int counter =0;
            while(j != sLength){
                for (int x =j;x<sLength-1;x++){
                    String strName;
                    strName = students[j].getName() + "&" + students [x+1].getName();
                    double score = compareFiles(students[j].getToken(),students[x+1].getToken());
                    records.add(new Record());
                    records.get(counter).setName(strName);
                    records.get(counter).setScore(score);
                    counter++;
                }
                j++;
            }
            for(int i=0;i<3;i++){
                System.out.print(i+"  Name: "+records.get(i).getName());
                System.out.println("   Score: "+ records.get(i).getScore());
            }

            bubbleSort(records);
            for(int i=0;i<3;i++){
                System.out.print(i+1 +". Name: "+records.get(i).getName());
                System.out.println(" Score: "+ records.get(i).getScore());
            }
            matrix.add(new Label("Top 10 List"),sLength+3,0);
            matrix.add(new Label("Name"), sLength+3,1);
            matrix.add(new Label("Score"), sLength+4,1);

            for(int i =0;i<10;i++){
                String strName = i+1 +". " +records.get(i).getName();
                matrix.add(new Label(strName),sLength+3,i+2);
                String strScore = new DecimalFormat("#.##").format(records.get(i).getScore());
                matrix.add(new Label(strScore+"        "),sLength+4,i+2);
            }
        }



    }

    public Rectangle recScore(double score){
        Rectangle rec = new Rectangle();
        rec.setHeight(25);
        rec.setWidth(50);
        rec.setStroke(Color.BLACK);
        if(score>=0&&score <=10) rec.setFill(Color.GREEN);
        else if(score>10 && score <=20) rec.setFill(Color.DARKSEAGREEN);
        else if(score >20 && score <=30) rec.setFill(Color.YELLOWGREEN);
        else if(score >30 && score <=40) rec.setFill(Color.YELLOW);
        else if(score >40 && score <=50) rec.setFill(Color.LIGHTBLUE);
        else if(score >50 && score <=60) rec.setFill(Color.BLUE);
        else if(score >60 && score <=70) rec.setFill(Color.ORANGE);
        else if(score >70 && score <=80) rec.setFill(Color.ORANGERED);
        else if(score >80 && score <=90) rec.setFill(Color.RED);
        else if(score >90 && score <=100) rec.setFill(Color.DARKRED);

        return rec;
    }



    public  void find(String path, Student student) throws IOException {
        //instantiate a new FILE object that takes the argument path
        File root = new File(path);

        //store all the list of file on a list array
        File[] list = root.listFiles();

        //if the dir contains none then return
        if (list == null){
            File f = new File(path);
            if(f.getName().endsWith(".java") || f.getName().endsWith(".cpp")){
                student.addToken(f.getAbsolutePath());
            }
        }
        else{
            for ( File f : list ) {
                if ( f.isDirectory() ) {
                    find( f.getAbsolutePath(),student );
                }
                else {
                    if(f.getName().endsWith(".java") || f.getName().endsWith(".cpp")) student.addToken(f.getAbsolutePath());
                }
            }
        }

    }

    public static double compareFiles(ArrayList<Integer> tk1, ArrayList<Integer> tk2) {
        double length;
        double similar = 0;
        double denum;


        if(tk1.size() >= tk2.size()) {
            length = tk2.size();
            denum = tk1.size();
        }
        else {
            length = tk1.size();
            denum = tk2.size();
        }

        for(int i=0; i < length;i++){
            if(tk1.get(i).equals(tk2.get(i))) similar++;
        }
        return  similar/denum *100;
    }

    public static void bubbleSort(ArrayList<Record> records) {
        boolean sorted = false;
        Record temp;
        while(!sorted) {
            sorted = true;
            for (int i = 0; i < records.size() - 1; i++) {
                if (records.get(i).getScore() < records.get(i+1).getScore()) {
                    temp = records.get(i);
                    records.set(i,records.get(i+1));
                    records.set(i+1,temp);
                    sorted = false;
                }
            }
        }
    }

    public void metrics() {
        if (fileLoc == null) System.out.println("No file selected");
        else {
            matrix.getChildren().removeAll();
            matrix.getChildren().clear();
            File root = new File(fileLoc);
            File[] list = root.listFiles();

            if (list == null) {
                System.out.println("File is empty");
                return;
            }
            if (list.length == 0) {
                fileLabel.setText("File Selected is Empty!");
                System.out.println("File is empty");
                return;
            }

            Student[] students = new Student[list.length];

            for (int i = 0; i < list.length; i++) {
                students[i] = new Student();
                students[i].setName(list[i].getName());
                System.out.println(students[i].getName());
                try {
                    currMetrics(list[i].getAbsolutePath(), students[i]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String temp;
            matrix.add(new Label("Name"),0,0);
            for(int i=0;i< students.length;i++){
                matrix.add(new Label(students[i].getName()),0,i+1);
            }

            Label label = new Label("Operators");
            StackPane stackPane2 = new StackPane();
            stackPane2.getChildren().add(label);
            matrix.add(stackPane2,1,0);
            for(int i=0;i< students.length;i++){
                matrix.getColumnConstraints().add(new ColumnConstraints(60)); // column 0 is 50 wide
                matrix.getRowConstraints().add(new RowConstraints(20)); //column is 25 long
                StackPane stackPane = new StackPane();
                temp = Integer.toString(students[i].getUnOperators());
                stackPane.getChildren().addAll(new Label(temp));
                matrix.add(stackPane,1,i+1);

            }

            matrix.add(new Label("Occurrences"),2,0);
            for(int i=0;i< students.length;i++){
                StackPane stackPane = new StackPane();
                temp = Integer.toString(students[i].getSumOperators());
                stackPane.getChildren().addAll(new Label(temp));
                matrix.add(stackPane,2,i+1);
            }


            matrix.add(new Label("Operands"),3,0);
            for(int i=0;i< students.length;i++){
                StackPane stackPane = new StackPane();
                temp = Integer.toString(students[i].getUnOperands());
                stackPane.getChildren().addAll(new Label(temp));
                matrix.add(stackPane,3,i+1);
            }

            matrix.add(new Label("Occurrences"),4,0);
            for(int i=0;i< students.length;i++){
                StackPane stackPane = new StackPane();
                temp = Integer.toString(students[i].getSumOperands());
                stackPane.getChildren().addAll(new Label(temp));
                matrix.add(stackPane,4,i+1);
            }

            matrix.add(new Label("Length"),5,0);
            for(int i=0;i< students.length;i++){
                StackPane stackPane = new StackPane();
                temp = Integer.toString(students[i].getSumOperands()+students[i].getSumOperators());
                stackPane.getChildren().addAll(new Label(temp));
                matrix.add(stackPane,5,i+1);
            }

            matrix.add(new Label("Vocabulary"),6,0);
            for(int i=0;i< students.length;i++){
                StackPane stackPane = new StackPane();
                temp = Integer.toString(students[i].getUnOperands()+students[i].getUnOperators());
                stackPane.getChildren().addAll(new Label(temp));
                matrix.add(stackPane,6,i+1);
            }

            matrix.add(new Label("Volume"),7,0);
            for(int i=0;i< students.length;i++){
                StackPane stackPane = new StackPane();
                double N = students[i].getSumOperands() + students[i].getSumOperators();
                double n =  students[i].getUnOperands() + students[i].getUnOperators();
                double v = N * Math.log(n)/ Math.log(2);
                temp = new DecimalFormat("#.##").format(v);
                stackPane.getChildren().addAll(new Label(temp));
                matrix.add(stackPane,7,i+1);
            }
        }
    }

    public  void currMetrics(String path, Student student) throws IOException {
        //instantiate a new FILE object that takes the argument path
        File root = new File(path);

        //store all the list of file on a list array
        File[] list = root.listFiles();

        //if the dir contains none then return
        if (list == null){
            File f = new File(path);
            if(f.getName().toLowerCase().endsWith(".java") || f.getName().toLowerCase().endsWith(".cpp")){
                student.setMetrics(f.getAbsolutePath());
            }
            System.out.println(f.getAbsolutePath());
        }
        else{
            for ( File f : list ) {
                if ( f.isDirectory() ) currMetrics(f.getAbsolutePath(), student);
                else {
                    if(f.getName().toLowerCase().endsWith(".java") || f.getName().toLowerCase().endsWith(".cpp")){
                        student.setMetrics(f.getAbsolutePath());
                    }
                }
            }
        }

    }


}
