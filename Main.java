import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.lang.String;
import java.util.LinkedList;
import java.util.Queue;

import java.util.ArrayList;

public class Main extends Application {

    static Label label = new Label("Enter an instruction!");

    static ArrayList<Button> code = new ArrayList<>();
    static ArrayList<Button> memory = new ArrayList<>();

    public static Label finishedProgram = new Label();

    static final int HEIGHT = 800;
    static final int WIDTH = 1280;

    public static Queue<Instruction> program = new LinkedList<>();

    static String tempCode;
    static String tempOpCode;
    static String enterMemAddr;


    static boolean enterAddress = false;
    static boolean enterCode = true;
    static boolean enterMem = false;

    static int lastIndex = 0;
    public static int programCounter = 0;
    public static int lastValue = 0;

    private static boolean loaded = false;

    public static Queue<Integer> inputs = new LinkedList<>();
    public static Queue<Integer> outputs = new LinkedList<>();

    static BackgroundFill[] memEmpty = {(new BackgroundFill(Color.LIGHTBLUE, null, null))};
    static BackgroundFill[] memFill = {(new BackgroundFill(Color.LIGHTPINK, null, null))};
    static BackgroundFill[] memRun = {(new BackgroundFill(Color.FORESTGREEN, null, null))};
    static BackgroundFill[] memStore = {(new BackgroundFill(Color.FUCHSIA, null, null))};
    static BackgroundFill[] memHalt = {(new BackgroundFill(Color.RED, null, null))};

    @Override
    public void start(Stage primaryStage) throws Exception{

        BorderPane bPane = new BorderPane();
        Scene scene = new Scene(bPane, WIDTH, HEIGHT);

        label.setTextAlignment (TextAlignment.CENTER);
        label.setAlignment(Pos.CENTER);
        label.setMinHeight(100);
        label.setMinWidth(WIDTH);
        bPane.setTop(label);

//  MEMORY
        GridPane memGrid = genMemGrid();
        memGrid.setMinHeight(500);
       // memGrid.setMinWidth(WIDTH/3);
        memGrid.setAlignment(Pos.CENTER);
        bPane.setCenter(memGrid);

//  CODE
        GridPane codeGrid = genCodeGrid();
        codeGrid.setMinHeight(200);
        codeGrid.setMinWidth(WIDTH);
        bPane.setBottom(codeGrid);

//  I/O
       GridPane inOutRun = genInOutRunGrid();
       inOutRun.setMinHeight(500);
       inOutRun.setMinWidth((WIDTH - 500)/2);
       inOutRun.setAlignment(Pos.CENTER);
       bPane.setLeft(inOutRun);

//   FINISHED PROGRAM
        GridPane doneGrid = new GridPane();
        doneGrid.getColumnConstraints().add(new ColumnConstraints(150));
        doneGrid.getColumnConstraints().add(new ColumnConstraints(((WIDTH - 500) /2 )-300));
        doneGrid.getColumnConstraints().add(new ColumnConstraints(150));
        finishedProgram.setMinWidth((WIDTH - 500)/2);
        finishedProgram.setTextAlignment(TextAlignment.CENTER);
        doneGrid.add(finishedProgram, 1, 0);
        //doneGrid.setGridLinesVisible(true);
        bPane.setRight(doneGrid);

       primaryStage.setResizable(false);
       primaryStage.setScene(scene);
       primaryStage.show();

    }

    //  Generates the grid for the memory
    public static GridPane genMemGrid() {

        GridPane pane = new GridPane();

        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {

                String memAddr = "0";
                if((i*10)+j < 10)
                    memAddr += "0";
                memAddr += Integer.toString((i*10) + j);
                pane.add(memButton(memAddr), j, i);
            }

            pane.getRowConstraints().add(new RowConstraints(50));
            pane.getColumnConstraints().add(new ColumnConstraints(50));

        }

        return pane;
    }

    public static GridPane genCodeGrid() {

        GridPane pane = new GridPane();

        pane.add(codeButton(Instruction.ADD), 0, 1);
        pane.add(codeButton(Instruction.SUB), 1, 1);
        pane.add(codeButton(Instruction.STA), 2, 1);
        pane.add(codeButton(Instruction.LDA), 3, 1);
        pane.add(codeButton(Instruction.INP), 4, 1);
        pane.add(codeButton(Instruction.OUT), 5, 1);
        pane.add(codeButton(Instruction.BRA), 6, 1);
        pane.add(codeButton(Instruction.BRZ), 7, 1);
        pane.add(codeButton(Instruction.BRP), 8, 1);
        pane.add(codeButton(Instruction.HLT), 9, 1);

        pane.getRowConstraints().add(new RowConstraints(50));
        pane.getRowConstraints().add(new RowConstraints(100));
        pane.getRowConstraints().add(new RowConstraints(50));

        return pane;
    }

    //  Creates a button for a memory location
    public static Button memButton(String name) {

        Button button = new Button(name);
        button.setMinWidth(50); button.setMaxWidth(50);
        button.setMinHeight(50); button.setMaxHeight(50);
        button.setBackground(new Background(memEmpty));

        button.setOnAction(e -> {

            if(enterAddress) {



            }

            if(enterMem) {
                label.setText("Enter an instruction!");

                String addr = button.getText().substring(1);

                memory.get(lastIndex).setText(Instruction.getOpCode(tempCode, addr));
                program.offer(new Instruction(tempCode, addr));

                if(Instruction.isJump(tempCode)){
                    lastIndex = Integer.parseInt(addr);
                }

                button.setBackground(new Background(memStore));

                enterCode = true;
                enterMem = false;
                enterAddress = false;
                lastIndex++;

            }

            else if(enterCode) {
                /* swap one location with another*/
            }



        });


        memory.add(button);
        return button;
    }

    //  Creates a button for the code
    public static Button codeButton(String name) {
        Button button = new Button(name);

        button.setMinHeight(100);
        button.setMinWidth(WIDTH / 10);
        button.setTextAlignment(TextAlignment.CENTER);
        button.setWrapText(true);

        button.setOnAction(e -> {

            if(enterAddress) {

            }

            else if(enterCode) {
                tempCode = button.getText();

                String addr = memory.get(lastIndex).getText().substring(1);
                tempOpCode = Instruction.getOpCode(tempCode, addr);
                memory.get(lastIndex).setBackground(new Background(memFill));

                if (!Instruction.needsAddress(tempCode)) {
                    memory.get(lastIndex).setText(tempOpCode);
                    program.offer(new Instruction(tempCode, addr));
                    lastIndex++;

                    enterAddress = false;
                    enterMem = false;
                    enterCode = true;
                } else {
                    label.setText("Which address?");
                    enterAddress = false;
                    enterMem = true;
                    enterCode = false;
                }

            }

        });

        code.add(button);
        return button;
    }

    //  Creates the In/Out/Run buttons
    public static GridPane genInOutRunGrid() {

        GridPane pane = new GridPane();

        Button load = new Button("Load");
        Button start = new Button("Start");
        Button step = new Button("Step");

        GridPane controlpane = new GridPane(); controlpane.setAlignment(Pos.CENTER);
        controlpane.add(load, 0, 0);
        controlpane.add(step, 1, 0);
        controlpane.add(start, 2, 0);

        TextField input = new TextField(); input.setAlignment(Pos.CENTER); input.setText("");
        TextField output = new TextField(); output.setAlignment(Pos.CENTER); output.setText("");

        pane.add(new Label("Input: "), 0, 0);
        pane.add(input, 1, 0);
        pane.add(controlpane, 1, 1);
        pane.add(new Label("Output: "), 0, 2);
        pane.add(output, 1, 2);

        start.setOnAction(e -> {

            if(loaded) {
                while (!program.isEmpty() || programCounter == 100) {
                    memory.get(programCounter).setBackground(new Background(memRun));
                    program.poll().execute();


                }

                memory.get(programCounter).setBackground(new Background(memHalt));

                while (!outputs.isEmpty()) {
                    output.setText(output.getText() + " " + outputs.poll());
                }
                finishedProgram.setText(Instruction.getProgram());
                label.setText("Finished!");
            }

            else {
                label.setText("Error, click load first!");
            }
        });

        step.setOnAction(e -> {

            if(loaded) {
                if (!program.isEmpty() || programCounter == 100) {
                    memory.get(programCounter).setBackground(new Background(memRun));
                    program.poll().execute();
                } else if (program.isEmpty() && !outputs.isEmpty()) {

                    memory.get(programCounter+1).setBackground(new Background(memHalt));



                    while (!outputs.isEmpty()) {
                        output.setText(output.getText() + " " + outputs.poll());

                    }

                    finishedProgram.setText(Instruction.getProgram());
                    label.setText("Finished!");

                }
            }

            else {
                label.setText("Error, click load first!");
            }
        });

        load.setOnAction(e -> {
            String in[] = input.getText().split(" ");;
            for(String i : in) {
                inputs.offer(Integer.parseInt(i));
            }
            loaded = true;
        });

        return pane;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
