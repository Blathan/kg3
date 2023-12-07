package com.example.kg3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import static java.lang.Math.*;

public class Controller extends Pane{
    @FXML
    public TextField x1;
    @FXML
    public TextField x2;
    @FXML
    public TextField y1;
    @FXML
    public TextField y2;
    public RadioButton Linear;
    public Pane pane;
    public RadioButton Brezenhem;

    public Controller(){
    }
    public void drawLine(ActionEvent actionEvent) {
        pane.getChildren().clear();

        System.out.println("x");
        float x1 ;
        float x2 ;
        float y1 ;
        float y2 ;
        try {
            x1 = Integer.parseInt(this.x1.getText());
            x2 = Integer.parseInt(this.x2.getText());
            y1 = Integer.parseInt(this.y1.getText());
            y2 = Integer.parseInt(this.y2.getText());
            if(x1>x2){
                float temp = x1;
                x1 = x2;
                x2 = temp;
                temp = y1;
                y1 = y2;
                y2 = temp;
            }

        } catch (NumberFormatException e) {
            drawGrid(30);
            return;
        }
        int step = calculateStep(x1,y1,x2,y2);
        if(step<2){
            step=2;
        }
        int size = drawGrid(step);
        if (Brezenhem.selectedProperty().get()){
            Bresenham(x1,y1,x2,y2,step,size);
            return;
        }
        Linear(x1,y1,x2,y2,step,size);
    }
    private void Linear(float x1, float y1, float x2, float y2,float step,float size){
        long start = System.currentTimeMillis();
        double max = max(abs(x2-x1),abs(y2-y1));
        double dist = ((x2-x1))/max;
        float k = (y2 - y1)/(x2-x1);
        float b = -k*x1 + y1;
        for (int i = 0; i <= max; i++) {
            double temp_x = x1 + dist * i;
            double temp_y = round(k*(temp_x) + b);
            Rectangle rectangle = new Rectangle(Math.round(temp_x+size)*step ,(temp_y+size)*step,step,step);
            rectangle.setFill(Color.BLACK);
            pane.getChildren().add(rectangle);
        }
        long finish = System.currentTimeMillis();
        long elapsed = finish - start;
        System.out.println(elapsed);
    }
    private int calculateStep(float x1,float y1,float x2,float y2){
//
//        double max = max(abs(x2-x1),abs(y2-y1));
//        double first = min(abs(min(x1,x2)),abs(min(y1,y2)));
//
        double max_x = max(x1,x2);
        double max_y = max(y1,y2);

        double min_x = min(x1,x2);
        double min_y = min(y1,y2);

        double min = min(min_x,min_y);
        double max = max(max_x,max_y);

        double neg = 0;
        double pos = 0;

        if(min<0){
            neg = abs(min);
        }

        if(max>0){
            pos = max;
        }

        if(pane.getWidth()/(neg + pos + 10)>50){
            return 50;
        }
        return (int) (pane.getWidth()/((neg + pos + 10)));
    }
    public void Bresenham(float x1, float y1, float x2, float y2,float step,float size){
        long start = System.currentTimeMillis();
        ArrayList<Point2D> points = new ArrayList<>();
        int dx = (int) Math.abs(x2 - x1);
        int dy = (int) Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;

        while (true) {
            points.add(new Point2D(x1, y1));
            if (x1 == x2 && y1 == y2) {
                break;
            }
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
        for (Point2D point : points) {
            Rectangle rectangle = new Rectangle(Math.round(point.getX()+size) * step, (point.getY() + size) * step, step, step);
            rectangle.setFill(Color.BLACK);
            pane.getChildren().add(rectangle);
        }
        long finish = System.currentTimeMillis();
        long elapsed = finish - start;
        System.out.println(elapsed);
    }
    public int drawGrid(int step){
        int size = (int) (pane.getPrefHeight()/step);

        Line line = new Line(size*step,0,size*step , 2*(size) * step);
        line.setStrokeWidth(step/5d);
        pane.getChildren().add(line);
        Line line2 =  new Line(0,size*step,2*(size)*step,size*step);
        line2.setStrokeWidth(step/5d);
        pane.getChildren().add(line2);

        int c_y = Integer.MAX_VALUE-step*2;
        int c_x = Integer.MAX_VALUE-step*2;
        for (int i = 0; i < (2 * (size)); i++) {
            c_y+=step;
            c_x+=step;
            if( step<=5 && i%11 != 0){
                continue;
            }
            if(-size+i!=0&& c_y > 15){
                Text textField = new Text(Integer.toString( (size-(int)i)));
                textField.setLayoutX(step*size*0.95);
                textField.setLayoutY(i*step+15);
                pane.getChildren().add(textField);
                c_y = 0;
                }
            if(c_x>15) {
                Text textField = new Text(Integer.toString(-size + (int) i));
                textField.setLayoutX(i * step + 5);
                textField.setLayoutY(step * size * 1.05);
                    pane.getChildren().add(textField);
                    c_x = 0;
            }
        }
        if(step==2){
            return size;
        }
        for (int i = 0; i < 2*(size)+1; i++) {
            Line lineY = new Line(i*step,0,i*step , 2*(size) * step);
            lineY.setStrokeWidth(step/20d);
            pane.getChildren().add(lineY);
        }
        for (int i = 0; i < 2*(size)+1; i++) {
            Line lineY = new Line(0,i*step,2*(size)*step,i*step);
            lineY.setStrokeWidth(step/20d);
            lineY.setStrokeWidth(step/20d);
            pane.getChildren().add(lineY);
        }
        return size;
    }
    public void initialize(){
        drawGrid(30);
    };
}