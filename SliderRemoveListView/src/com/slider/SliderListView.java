package com.slider;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class SliderListView extends ListView{
	public static final int SlidRate = 3;
	private boolean canSlid;
	private boolean animationStarted;
	private int childViewId;
	public SliderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	private float distanceX = 0;
	private float distanceY = 0;
	private float preX = 0;
	private float preY = 0;
	private View tempView;
	private float viewLeft,tempViewLeft;
	private float viewRight,tempViewRight;
	private int position;
	/**
	 * if the item of listView is not the view you want control,and you just want control the sub view  
	 * @param childViewId   childId of the item view 
	 */
	public void setViewId(int childViewId){
		this.childViewId = childViewId;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch(ev.getAction()){
			case MotionEvent.ACTION_DOWN:
				distanceX = 0;
				distanceY = 0;
				preX = ev.getRawX();
				preY = ev.getRawY();
				tempView =getChildAt(position=pointToPosition((int)ev.getX(),(int)ev.getY())
						- getFirstVisiblePosition());
				if(childViewId!=0&&tempView!=null){
					tempView = tempView.findViewById(childViewId);
				}
				if(tempView==null)break;
				tempViewLeft = viewLeft = tempView.getLeft();
				tempViewRight = viewRight = tempView.getRight();
				break;
			case MotionEvent.ACTION_MOVE:
				return true;
			case MotionEvent.ACTION_UP:
				return true;
		}
		return super.onInterceptTouchEvent(ev);		
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
			switch(ev.getAction()){
				case MotionEvent.ACTION_MOVE:
					if(!canSlid){
						distanceX += ev.getRawX()-preX;
						distanceY += Math.abs(ev.getRawY()-preY);
						preX = ev.getRawX();
						preY = ev.getRawY();
						if((distanceX>30&&distanceY==0)||(distanceY!=0&&(distanceX/distanceY)>SlidRate&&distanceX>30)){
							canSlid = true;
							distanceX = 0;
							distanceY = 0;
							return false;
						}
						break;
					}					
					if(tempView!=null&&canSlid&&!animationStarted){
						tempViewLeft += (int)(ev.getRawX()-preX);
						tempViewRight += (int)(ev.getRawX()-preX);
						if(tempViewLeft<viewLeft)tempViewLeft = viewLeft;
						if(tempViewRight<viewRight)tempViewRight = viewRight;
						tempView.layout((int)tempViewLeft,tempView.getTop(),
								(int)tempViewRight,tempView.getBottom());

						preX = ev.getRawX();
						preY = ev.getRawY();
						return false;
					}
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					if(tempView!=null&&canSlid){
						float to = 0;
						boolean remove = false;
						if(tempView.getLeft()>0&&tempView.getLeft()>(viewLeft+(viewRight-viewLeft)/2.5f)){
							to = viewRight-tempView.getLeft();
							remove = true;
						}else{
							to = 0-tempView.getLeft();
							remove = false;
						}
						SildAnimation ani = new SildAnimation(tempView,to,remove);
						ani.setDuration(500);
						ani.start(null);
						animationStarted = true;
						return true;
					}
			}
			
		return super.onTouchEvent(ev);
	}
	
	class SildAnimation extends AsyncAnimation{
		private float to;
		private int from;
		private View view;
		public SildAnimation(View view,float to,boolean remove){
			this.to = to;
			this.view  = view;
			from = view.getLeft();
		}

		@Override
		public void asyncRun(View views, float interpolatedTime) {
			 float step = to*interpolatedTime;
			 view.layout(from+(int)step,view.getTop(),from+view.getWidth()+(int)step,view.getBottom());
			 if(step==to&&listener!=null){
				 if(to>0)listener.remove(position+getFirstVisiblePosition(), view);
				 canSlid = false;
				 animationStarted = false;
				 stop();
			 }
		}
		
	}
	
	
	public interface SildRemoveListener{
		public void remove(int position,View view);
	}
	private SildRemoveListener listener;
	public void setSildRemoveListener(SildRemoveListener listener){
		this.listener = listener;
	}
}
