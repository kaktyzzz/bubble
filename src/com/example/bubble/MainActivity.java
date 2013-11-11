package com.example.bubble;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends Activity {
	
	ListBall listBall = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		GraphicsView gView=new GraphicsView(this);
		setContentView(gView);
	}

	public class GraphicsView extends View {
		Handler mHandler = new Handler();
		static final long FRAME_TIME = 300;
		
		public GraphicsView(Context context) {
			super(context);
		}
		
		public boolean onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				Point p = new Point(event.getX(), event.getY());
				Ball ball = new Ball (p);
				
				if (listBall == null) 
					listBall = new ListBall (ball);
				else
					listBall.addEl(ball);
				
				invalidate();
			}
			return true;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			ListBall cursor;
			cursor = listBall;
			canvas.drawColor(Color.WHITE);
			
			while (cursor != null) {
				Ball ball = cursor.getValue();
				ball.move();
				ball.cross(canvas);
				
				canvas.drawCircle(ball.centre.x, ball.centre.y, ball.r, ball.paint);
				
				cursor = cursor.getNext();
			}
			invalidate();
		}
		
		

	}	
}

class Point {
	float x,y;
	
	public Point (float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void copyFrom (Point p) {
		x = p.x;
		y = p.y;
	}
}

class ListBall { //связный список шаров (вместо массива)
	private Ball value;
	private ListBall next;
	
	public ListBall (Ball ball) {
		value = ball;
		next = null;
	}
	
	public void addEl (Ball ball) {
		if (next == null)
			next = new ListBall(ball);
		else
			next.addEl(ball);
	}
	
	public ListBall getNext () {
		return next;
	}
	
	public Ball getValue () {
		return value;
	}
}

class Ball {
	public Point centre, base;
	public float r,speed;
	public Paint paint;
	private int angle;
	
	public Ball (Point p) {
		centre = p;
		base = new Point (p.x, p.y);
		r = (float) Math.random() * 40 + 10;
		//angle = 45;
		angle = (int) Math.round(Math.random() * 360);
		speed = (float) Math.random() * 4 + 2;
		//speed = 1;
		
		paint = new Paint ();
		paint.setColor(Color.rgb((int) Math.round(Math.random() * 255), (int) Math.round(Math.random() * 255), (int) Math.round(Math.random() * 255)));
	}
	
	public void reduceSpeed () {
		speed -= speed * 0.1;
	}
	
	public void inverseSpeed () {
		speed *= -1;
	}
	
	public void move () {
        centre.x += (float) Math.cos(angle * Math.PI / 180) * speed;
        float t = (float) Math.tan (angle * Math.PI / 180);
        centre.y = t * centre.x + (base.y - t * base.x); // уравнение прямой
	}
	
	private void correct_params () {
		reduceSpeed(); //снижаем скорость
		inverseSpeed(); // меняем направление
    	base.copyFrom(centre); // выравниваем абсолютные координаты
	}
	
	public void cross (Canvas canvas) { // пересечение с канвой
            if ((cross (r, 0, centre.y)) || (cross (r, canvas.getHeight(), centre.y))) {
            	angle = 180 - angle;
            	correct_params();
            }
            else if ((cross (r, 0, centre.x)) || (cross (r, canvas.getWidth(), centre.x))) {
            	angle = 360 - angle;
            	correct_params();
            }
    }
        public Boolean cross (float r, float c0, float c1) { //пересечение окружности с прямой
            return (r * r - Math.pow ((c0 - c1), 2)) >= 0;
    }
	
}
