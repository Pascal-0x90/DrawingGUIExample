

// Assignment #: Arizona State University CSE205 #7
//         Name: Nathan Smith
//    StudentID: 1211898087
//      Lecture: T, Th 4:30 pm
//  Description: The DrawPane class creates a canvas where we can use
//               mouse key to draw either a Rectangle or a Circle with different
//               colors. We can also use the the two buttons to erase the last
//				 drawn shape or clear them all.
//import any classes necessary here
//----
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Insets;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.FlowPane;

public class DrawPane extends BorderPane {

    private final Button undoBtn;
    private final Button eraseBtn;
    private final ComboBox<String> colorCombo;
    private final RadioButton rbRect;
    private final RadioButton rbCircle;
    private final ArrayList<Shape> shapeList;
    private final Pane canvas;
    //declare any other necessary instance variables here
    //----
    private final HBox hb;
    private final VBox vb;
    private final TilePane tp;
    private final BorderPane bp;
    private final ToggleGroup tg;
    private Rectangle rect;
    private Circle circle;
    private final HBox hb2;
    private final FlowPane fp;
    private Color pickedColor;
    private String selectShape;

    //Constructor
    public DrawPane() {
        //Step #1: initialize each instance variable and set up layout
        undoBtn = new Button("Undo");
        eraseBtn = new Button("Erase");
        undoBtn.setMinWidth(80.0);
        eraseBtn.setMinWidth(80.0);

        //Create the color comboBox and intial its default color
        //----
        colorCombo = new ComboBox<>();
        colorCombo.getItems().setAll("Black", "Blue", "Green", "Red", "Yellow", "Orange", "Pink");
        colorCombo.setValue("Black");

        //Create the two radio buttons and also a ToggleGroup
        //so that the two radio buttons can be selected
        //mutually exclusively. Otherwise they are indepedant of each other
        //----
        rbRect = new RadioButton();
        rbCircle = new RadioButton();
        rbRect.setText("Rectangle");
        rbCircle.setText("Circle");

        tg = new ToggleGroup();

        rbRect.setToggleGroup(tg);
        rbCircle.setToggleGroup(tg);

        //initialize shapeList, it is a data structure we used
        //to track the shape we created      
        shapeList = new ArrayList<>();

        //canvas is a Pane where we will draw rectagles and circles on it
        canvas = new Pane();
        canvas.setStyle("-fx-background-color: beige;");

        //initialize the remaining instance variables and set up
        //the layout
        vb = new VBox();
        tp = new TilePane();
        bp = new BorderPane();
        hb = new HBox();
        hb2 = new HBox();
        fp = new FlowPane();
        pickedColor = Color.BLACK;

        hb.getChildren().addAll(undoBtn, eraseBtn);
        hb.setAlignment(Pos.CENTER);
        hb.setStyle("-fx-border-color: black;");
        
        hb.setPadding(new Insets(10, 10, 10, 10));
        hb.setSpacing(10);

        vb.getChildren().addAll(colorCombo, rbRect, rbCircle);
        vb.setAlignment(Pos.CENTER_LEFT);
        vb.setPadding(new Insets(10, 10, 10, 10));
        vb.setSpacing(40);
        vb.setStyle("-fx-border-color: black;");

        
        this.setCenter(canvas);
        this.setLeft(vb);
        this.setBottom(hb);

        //Step #3: Register the source nodes with its handler objects
        ButtonHandler bh = new ButtonHandler();
        ShapeHandler sh = new ShapeHandler();
        ColorHandler ch = new ColorHandler();
        MouseHandler mh = new MouseHandler();

        canvas.setOnMouseClicked(mh);
        canvas.setOnMousePressed(mh);
        canvas.setOnMouseDragged(mh);
        canvas.setOnMouseReleased(mh);

        undoBtn.setOnAction(bh);
        eraseBtn.setOnAction(bh);
        colorCombo.setOnAction(ch);
        rbRect.setOnAction(sh);
        rbCircle.setOnAction(sh);

    }

    //Step #2(A) - MouseHandler
    private class MouseHandler implements EventHandler<MouseEvent> {

        public void handle(MouseEvent event) {
            //handle MouseEvent here
            //Note: you can use if(event.getEventType()== MouseEvent.MOUSE_PRESSED)
            //to check whether the mouse key is pressed, dragged or released
            //write your own codes here
            //----

            if (rbRect.isSelected()) {
                if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                    rect = new Rectangle();
                    rect.setStroke(Color.BLACK);
                    rect.setFill(Color.WHITE);
                    rect.setX(event.getX());
                    rect.setY(event.getY());
                    System.out.println("X: " + rect.getX() + " Y: " + rect.getY());

                } else if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
                    
                    Double dx = event.getX() - rect.getX();
                    Double dy = event.getY() - rect.getY();
                    
                    if(dx < 0) {
                        if(dy <0){
                            rect.setTranslateX(dx);
                            rect.setWidth(-dx);
                            rect.setTranslateY(dy);
                            rect.setHeight(-dy);
                        }
                        else if(dy > 0){
                            rect.setTranslateX(dx);
                            rect.setWidth(-dx);
                            rect.setTranslateY(0);
                            rect.setHeight(dy);
                        }
                    }
                    else if(dx > 0){
                        if(dy <0){
                            rect.setTranslateX(0);
                            rect.setWidth(dx);
                            rect.setTranslateY(dy);
                            rect.setHeight(-dy);
                        }
                        else if(dy > 0){
                            rect.setTranslateX(0);
                            rect.setWidth(dx);
                            rect.setTranslateY(0);
                            rect.setHeight(dy);
                        }
                    }
                    
                    
                    canvas.getChildren().add(rect);

                } else if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
                    shapeList.add(rect);
                    rect.setFill(pickedColor);
                    canvas.getChildren().addAll(shapeList);
                    System.out.println(shapeList.size());
                    
                }
            }
            
            else if(rbCircle.isSelected()){
                if(event.getEventType().equals(MouseEvent.MOUSE_PRESSED)){
                    circle = new Circle();
                    circle.setStroke(Color.BLACK);
                    circle.setFill(Color.WHITE);
                    circle.setCenterX(event.getX());
                    circle.setCenterY(event.getY());
                    System.out.println("X: " + circle.getCenterX() + " Y: " + circle.getCenterY()  );
                }
                else if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)){
                    
                    Double dx = Math.abs(event.getX() - circle.getCenterX());
                    Double dy = event.getY() - circle.getCenterX();
                    
                    circle.setRadius(dx);
                    canvas.getChildren().add(circle);
                }
                    
                    else if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
                    shapeList.add(circle);
                    circle.setFill(pickedColor);
                    canvas.getChildren().addAll(shapeList);
                    System.out.println(shapeList.size());
                    shapeList.clear();
                }
                    
                
                
                
                
            }

        }//end handle()
    }//end MouseHandler

    //Step #2(B)- A handler class used to handle events from Undo & Erase buttons
    private class ButtonHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent event) {
            //write your codes here
            //----
            if (event.getSource() == undoBtn) {
                canvas.getChildren().remove(canvas.getChildren().size()-1);
            } else if (event.getSource() == eraseBtn) {
                canvas.getChildren().clear();
            } else {

            }

        }
    }//end ButtonHandler

    //Step #2(C)- A handler class used to handle events from the two radio buttons
    private class ShapeHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent event) {
            //write your own codes here
            //----
            if (event.getSource() == rbRect) {
                selectShape = "Rectangle";

            } else if (event.getSource() == rbCircle) {
                selectShape = "Circle";
            } else {

            }

        }
    }//end ShapeHandler

    //Step #2(D)- A handler class used to handle colors from the combo box
    private class ColorHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent event) {
            //write your own codes here
            //----
            pickedColor = Color.valueOf(colorCombo.getSelectionModel().getSelectedItem().toUpperCase());
            System.out.println(pickedColor.toString());
        }
    }//end ColorHandler

}//end class DrawPane

//                    __
//         .,-;-;-,. /'_\
//       _/_/_/_|_\_\) /
//     '-<_><_><_><_>=/\
//       `/_/====/_/-'\_\
//        ""     ""    ""