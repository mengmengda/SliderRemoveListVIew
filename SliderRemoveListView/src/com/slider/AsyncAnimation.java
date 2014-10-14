package com.slider;

import android.os.Handler;
import android.view.View;

public abstract class AsyncAnimation {
	private int duration = 1000;
	private float interpolatedTime = 0;
	private int tempDur = 0;
	private boolean stop;
	private Handler handler = new Handler();
	public void start(final View view){
		new Thread(new Runnable() {
			@Override
			public void run() {	
				try{			
					while(tempDur<duration){
						tempDur+=4;
						if(tempDur>=duration)tempDur=duration;
						Thread.sleep(4);
						handler.post(new Runnable() {
							@Override
							public void run() {
								if(!stop)
								asyncRun(view,interpolatedTime+1f*tempDur/duration);								
							}
						});
					}
				}catch(Exception e){}
	
			}
		}).start();

	}
	public abstract void asyncRun(View view,float interpolatedTime);
	public AsyncAnimation setDuration(int duration){
		this.duration = duration;
		return this;
	}
	public void stop(){
		this.stop = true;
	}
}
