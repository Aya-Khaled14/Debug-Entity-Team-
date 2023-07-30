 package application;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;



public class Main extends Application {

	private final List<Shape> shapes = new ArrayList<>();
    private Shape currentShape;
    private boolean isDrawing = false;
    private int flag = 0;
    private ColorPicker colorPicker;
    private ComboBox<Double> lineThicknessComboBox;
    private ComboBox<String> fillComboBox;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Paint App ~ by Debug Entity");

        //  canvas
        Canvas canvas = new Canvas(725, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // color 
        colorPicker = new ColorPicker(Color.BLACK);

        //line thickness 
        lineThicknessComboBox = new ComboBox<>();
        lineThicknessComboBox.getItems().addAll(1.0, 2.0, 3.0, 4.0, 5.0);
        lineThicknessComboBox.setValue(3.0);

        //fill 
        fillComboBox = new ComboBox<>();
        fillComboBox.getItems().addAll("None", "Solid");
        fillComboBox.setValue("None");

        //rectangle button
        Image rectangleImage = new Image(getClass().getResourceAsStream("rectangle2.png"));
        Button rectangleButton = new Button(null, new javafx.scene.image.ImageView(rectangleImage));
        rectangleButton.setOnAction(event -> {
            currentShape = new Rectangle();
            currentShape.color = colorPicker.getValue();
            currentShape.lineThickness = lineThicknessComboBox.getValue();
            if (fillComboBox.getValue().equals("Solid")) {
                currentShape.isFilled = true;
                currentShape.fillColor = colorPicker.getValue();
            }
            isDrawing = true;
            flag = 1;
        });
        
        //circle 
        Image circleImage = new Image(getClass().getResourceAsStream("circle2.png"));
        Button circleButton = new Button(null, new javafx.scene.image.ImageView(circleImage));
        circleButton.setOnAction(event -> {
            currentShape = new Circle();
            currentShape.color = colorPicker.getValue();
            currentShape.lineThickness = lineThicknessComboBox.getValue();
            if (fillComboBox.getValue().equals("Solid")) {
                currentShape.isFilled = true;
                currentShape.fillColor = colorPicker.getValue();
            }
            isDrawing = true;
            flag = 2;
        });

        //line 
        Image lineImage = new Image(getClass().getResourceAsStream("line2.png"));
        Button lineButton = new Button(null, new javafx.scene.image.ImageView(lineImage));
        lineButton.setOnAction(event -> {
            currentShape = new Line();
            currentShape.color = colorPicker.getValue();
            currentShape.lineThickness = lineThicknessComboBox.getValue();
            isDrawing = true;
            flag = 3;
        });
        
        //triangle
        Image triangleImage = new Image(getClass().getResourceAsStream("triangle2.png"));
        Button triangleButton = new Button(null, new javafx.scene.image.ImageView(triangleImage));
        triangleButton.setOnAction(event -> {
            currentShape = new Triangle();
            currentShape.color = colorPicker.getValue();
            currentShape.lineThickness = lineThicknessComboBox.getValue();
            if (fillComboBox.getValue().equals("Solid")) {
                currentShape.isFilled = true;
                currentShape.fillColor = colorPicker.getValue();
            }
            isDrawing = true;
            flag = 6;
        });
        
        //erase
        Image eraseImage = new Image(getClass().getResourceAsStream("erase3.png"));
        Button eraseButton = new Button(null, new javafx.scene.image.ImageView( eraseImage));
        eraseButton.setOnAction(event -> {
            currentShape = new Erase();
            currentShape.color = Color.WHITE;
            currentShape.lineThickness = 7.0;
            isDrawing = true;
            flag = 4;
        });
        
        // clear 
        Image clearImage = new Image(getClass().getResourceAsStream("clear2.png"));
        Button clearButton =  new Button(null, new javafx.scene.image.ImageView( clearImage));
        clearButton.setOnAction(event -> {
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            shapes.clear();
            isDrawing = true;
            flag = 5;
        });
        
        //save button
        Image saveImage = new Image(getClass().getResourceAsStream("save2.png"));
        Button saveButton = new Button(null, new javafx.scene.image.ImageView(saveImage));  //for save !!don't change it => for bounce
        saveButton.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("png files (.png)", ".png");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(primaryStage);
            
            if (file != null) {
                try {
                    WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                    canvas.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                } catch (IOException ex) {
                }
            }
        });
    
       
        
        // Add the canvas and buttons to a layout
        BorderPane root = new BorderPane();
        root.setCenter(canvas);
        root.setTop(new HBox(saveButton,rectangleButton, circleButton,triangleButton, lineButton,eraseButton, fillComboBox, colorPicker,lineThicknessComboBox,clearButton));

        //  mouse events 
        canvas.setOnMousePressed(event -> {
            if (isDrawing) {
                if (flag == 4) { // Brush
                    currentShape = new Erase();
                    currentShape.color = Color.WHITE;
                    currentShape.lineThickness = 7.0;
                    isDrawing = true;
                } else {
                    currentShape.startX = event.getX();
                    currentShape.startY = event.getY();
                }
            }
        });

        canvas.setOnMouseDragged(event -> {
            if (isDrawing) {
                if (flag == 4) { // erase
                    ((Erase) currentShape).xPoints.add(event.getX());
                    ((Erase) currentShape).yPoints.add(event.getY());
                    redraw(gc);
                } else {
                    currentShape.endX = event.getX();
                    currentShape.endY = event.getY();
                    redraw(gc);
             }
            }
        });

        canvas.setOnMouseReleased(event -> {
            if (isDrawing) {
                if (flag == 4) { //erase
                    ((Erase) currentShape).xPoints.add(event.getX());
                    ((Erase) currentShape).yPoints.add(event.getY());
                    shapes.add(currentShape);
                    currentShape = new Erase();
                    currentShape.color = Color.WHITE;
                    currentShape.lineThickness = 5.0;
                } else {
                    currentShape.endX = event.getX();
                    currentShape.endY = event.getY();
                    shapes.add(currentShape);
                    if (flag == 1)
                        currentShape = new Rectangle();
                    else if (flag == 2)
                        currentShape = new Circle();
                    else if (flag == 3)
                        currentShape = new Line();
                    else if (flag == 6)
                        currentShape = new Triangle();
                    currentShape.color = colorPicker.getValue();
                    currentShape.lineThickness = lineThicknessComboBox.getValue();
                    if (fillComboBox.getValue().equals("Solid")) {
                        currentShape.isFilled = true;
                        currentShape.fillColor = colorPicker.getValue();
                    }
                }
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

    //class shape
    private abstract class Shape {
        double startX, startY, endX, endY;
        Color color;
        Double lineThickness;
        boolean isFilled;
        Color fillColor;

        abstract void draw(GraphicsContext gc);
    }

    //rectangle class
    private class Rectangle extends Shape {
        @Override
        void draw(GraphicsContext gc) {
            gc.setStroke(color);
            gc.setLineWidth(lineThickness);
            if (isFilled) {
                gc.setFill(fillColor);
                gc.fillRect(startX, startY, endX - startX, endY - startY);
            }
            gc.strokeRect(startX, startY, endX - startX, endY - startY);
        }
    }
    
    //Triangle class
    private class Triangle extends Shape {
        @Override
        void draw(GraphicsContext gc) {
            gc.setStroke(color);
            gc.setLineWidth(lineThickness);
            if (isFilled) {
                gc.setFill(fillColor);
            }
            double[] xPoints = {startX, (startX + endX) / 2, endX};
            double[] yPoints = {endY, startY, endY};
            if (isFilled) {
                gc.fillPolygon(xPoints, yPoints, 3);
            }
            gc.strokePolygon(xPoints, yPoints, 3);
        }
    }

    //Circle class
    private class Circle extends Shape {
        @Override
        void draw(GraphicsContext gc) {
            gc.setStroke(color);
            gc.setLineWidth(lineThickness);
            if (isFilled) {
                gc.setFill(fillColor);
                double radius = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
                gc.fillOval(startX - radius, startY - radius, radius * 2, radius * 2);
            }
            double radius = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
            gc.strokeOval(startX - radius, startY - radius, radius * 2, radius * 2);
        }
    }
    
    //line class
    private class Line extends Shape {
        @Override
        void draw(GraphicsContext gc) {
            gc.setStroke(color);
            gc.setLineWidth(lineThickness);
            gc.strokeLine(startX, startY, endX, endY);
        }
    }
    
    //erase class
    private class Erase extends Shape {
        List<Double> xPoints = new ArrayList<>();
        List<Double> yPoints = new ArrayList<>();

        @Override
        void draw(GraphicsContext gc) {
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(5.0);
            gc.setLineCap(StrokeLineCap.ROUND);
            gc.setLineJoin(StrokeLineJoin.ROUND);

            for (int i = 0; i < xPoints.size() - 1; i++) {
                double startX = xPoints.get(i);
                double startY = yPoints.get(i);
                double endX = xPoints.get(i + 1);
                double endY = yPoints.get(i + 1);

                double radius = 5.0 / 2.0;
                gc.strokeOval(startX - radius, startY - radius, radius * 2, radius * 2);
                gc.strokeLine(startX, startY, endX, endY);
            }
        }
    }



    public static void main(String[] args) {
   	 launch();

}
}   
