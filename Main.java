package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private final List<Shape> shapes = new ArrayList<>();
    private Shape currentShape;
    private boolean isDrawing = false;
    private int flag = 0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Drawing Shapes");

        // Create a canvas to draw on
        Canvas canvas = new Canvas(500, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Create a button to select the rectangle
        Image rectangleImage = new Image(getClass().getResourceAsStream("RRectangle2.jpg"));
        Button rectangleButton = new Button(null, new javafx.scene.image.ImageView(rectangleImage));
        rectangleButton.setOnAction(event -> {
            currentShape = new Rectangle();
            isDrawing = true;
            flag=1;
          //  rectangleButton.setPrefSize(5, 5);
        });
        
        Image circleImage = new Image(getClass().getResourceAsStream("circle2.jpg"));
        Button circleButton = new Button(null, new javafx.scene.image.ImageView(circleImage));
        circleButton.setOnAction(event -> {
            currentShape = new Circle();
            isDrawing = true;
            flag=2;
        //    circleButton.setPrefSize(5, 5);
        });
       
       

        // Add the canvas and button to a layout
        BorderPane root = new BorderPane();
        root.setCenter(canvas);
       // root.setTop(rectangleButton);
        root.setTop(new HBox(rectangleButton, circleButton));
        // Handle mouse events to draw the shapes
        canvas.setOnMousePressed(event -> {
            if (isDrawing) {
                currentShape.startX = event.getX();
                currentShape.startY = event.getY();
            }
        });

        canvas.setOnMouseDragged(event -> {
            if (isDrawing) {
                currentShape.endX = event.getX();
                currentShape.endY = event.getY();
                redraw(gc);
            }
        });

        canvas.setOnMouseReleased(event -> {
            if (isDrawing) {
                currentShape.endX = event.getX();
                currentShape.endY = event.getY();
                shapes.add(currentShape); 
                if(flag==1)
                	currentShape = new Rectangle();
                else if (flag==2)
                currentShape = new Circle();
              //  isDrawing = false;
            }
        });

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void redraw(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        for (Shape shape : shapes) {
            shape.draw(gc);
        }
        if (currentShape != null) {
            currentShape.draw(gc);
        }
    }

    private abstract class Shape {
        double startX, startY, endX, endY;

        abstract void draw(GraphicsContext gc);
    }

    private class Rectangle extends Shape {
        @Override
        void draw(GraphicsContext gc) {
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);
            gc.strokeRect(startX, startY, endX - startX, endY - startY);
        }
    }
    private class Circle extends Shape {
        @Override
        void draw(GraphicsContext gc) {
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);
            double radius = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
            gc.strokeOval(startX - radius, startY - radius, radius * 2, radius * 2);
        }
    }
    private void redraw1(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        for (Shape shape : shapes) {
            shape.draw(gc);
        }
        if (currentShape != null) {
            gc.setFill(Color.WHITE);
            currentShape.draw(gc);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}