package com.example.bubble;

import android.os.Bundle;
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
			
			while (cursor != null) {
				Ball ball = cursor.getValue();
				
				canvas.drawCircle(ball.centre.x, ball.centre.y, ball.r, ball.paint);
				
				cursor = cursor.getNext();
			}
		}

	}	
}

class Point {
	float x,y;
	
	public Point (float x, float y) {
		this.x = x;
		this.y = y;
	}
}

class ListBall {
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
	public Point centre;
	public float r,w;
	public Paint paint;
	
	public Ball (Point p) {
		centre = p;
		r = (float) Math.random() * 30 + 10;
		
		paint = new Paint ();
		paint.setColor(Color.rgb((int) Math.round(Math.random()*255), (int) Math.round(Math.random()*255), (int) Math.round(Math.random()*255)));
	}
}
