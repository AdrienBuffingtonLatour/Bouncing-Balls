package BouncingBall;

import java.util.ArrayList;
import java.util.Random;

import BouncingBall.ManyBalls.Ball;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BouncingBalls extends Application {
	
	final double WIDTH = 600;
	final double HEIGHT = 600;
	final double RADIUS = 20;
	private ArrayList<Ball> balls = new ArrayList<Ball>();
		
	@Override
	public void start(Stage stage) {
		
		Pane pane = new Pane();
		pane.setStyle("-fx-border-color: blue; -fx-background-color: lightgray;");
		Scene scene = new Scene(pane, WIDTH, HEIGHT);
		for(int i = 0; i < 30; i++)
			balls.add(new Ball(RADIUS));
		
		pane.getChildren().addAll(balls);			
	
		Timeline animation = new Timeline(new KeyFrame(Duration.millis(25),
			e ->{
				for(Ball ball: balls) {
					ball.move();							
				}			
			}
		));
		
		animation.setAutoReverse(false);
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play();
		
		stage.setScene(scene);
		stage.setTitle("Bouncing Balls");
		stage.show();
	}
	
		public class Ball extends Circle{
			
			protected double dx, dy, radius;
			protected double mass, velocity;
			
				Ball(double radius){
					super(radius);
					this.setFill(Color.BLUE);
					this.mass = 1;
					this.dx = Math.random() * 5 + 1;
					this.dy = Math.random()* 5 + 1;
					this.setCenterX(Math.random() * (WIDTH - this.getRadius()) + this.getRadius() );
					this.setCenterY(Math.random() * (HEIGHT - this.getRadius()) + this.getRadius());
					System.out.println(this.getCenterX());
					System.out.println(this.getCenterY());
				}
		
			public void move() {
				this.setCenterX(this.getCenterX() + this.dx);
				this.setCenterY(this.getCenterY() + this.dy);
				
				//After each check the ball is repositioned to avoid "stickyness"//
				if(this.getCenterX()<= this.getRadius()) {
					this.setCenterX(this.getRadius());
					this.dx *= -1;
				}
				
				if(this.getCenterX() >= WIDTH - this.getRadius()) {
					this.setCenterX(WIDTH - this.getRadius());
					this.dx *= -1;
				}
				
				if(this.getCenterY() >= HEIGHT - this.getRadius()) {
					this.setCenterY(HEIGHT - this.getRadius());
					this.dy *= -1;
				}
				
				if(this.getCenterY() <= this.getRadius()) {
					this.setCenterY(this.getRadius());
					this.dy *= -1;
				}
					
				for(Ball b:balls) {
					if(b != this && b.collide(this)) 
						b.collision_resolution(this);	
				}
			}
		
			public boolean collide(Ball ball) {
				double deltaX = super.getCenterX() - ball.getCenterX();
				double deltaY = this.getCenterY() - ball.getCenterY();
				double distance = Math.sqrt(Math.pow(this.getCenterX() - ball.getCenterX(), 2) 
						+ Math.pow(this.getCenterY() - ball.getCenterY(),2));
				if(distance <= this.getRadius() + ball.getRadius())
					if(deltaX * (this.dx-ball.dx) + deltaY * (this.dy-ball.dy) < 0)//balls are moving toward each others//
						return true;
				return false;
			}
		
			public void collision_resolution(Ball ball) {
				double distance = Math.sqrt(Math.pow(this.getCenterX() - ball.getCenterX(), 2) 
						+ Math.pow(this.getCenterY() - ball.getCenterY(),2));			
				double deltaX = ball.getCenterX() - this.getCenterX();
				double deltaY = ball.getCenterY() - this.getCenterY();
				double unitContactX = deltaX / distance;
				double unitContactY = deltaY / distance;
				
				double u1 = this.dx * unitContactX + this.dy * unitContactY;
				double u2 = ball.dx * unitContactX + ball.dy * unitContactY;
				
				double massSum = this.mass + ball.mass;
				double massDiff = this.mass - ball.mass;
				
				double v1 = (2 * ball.mass * u2 + u1 * massDiff) / massSum;
				double v2 = (2 * this.mass * u1 - u2 * massDiff) / massSum;
				
				double u1PerpX = this.dx - u1 * unitContactX;
				double u1PerpY = this.dy - u1 * unitContactY;
				double u2PerpX = ball.dx - u2 * unitContactX;
				double u2PerpY = ball.dy - u2 * unitContactY;
				
				//New velocities for x-axis and y-axis//
				this.dx = v1 * unitContactX + u1PerpX;
				this.dy = v1 * unitContactY + u1PerpY;
				ball.dx = v2 * unitContactX + u2PerpX;
				ball.dy = v2 * unitContactY + u2PerpY;
			}
		}
		
	public static void main(String[] args) {
		launch(args);
	}
}

	
