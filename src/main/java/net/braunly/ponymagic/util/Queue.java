package net.braunly.ponymagic.util;

import java.util.Arrays;

public class Queue {
	
	public static float toDelta(float amount, float time){
		return amount / time * 40;
	}

	public Queue() {
		queue = new float[0][2];
	}
	
	public void add(float amount) {
		add(amount, amount);
	}
	
	public void add(float amount, float delta) {
		queue = Arrays.copyOf(queue, queue.length + 1);
		queue[queue.length - 1] = new float[2];
		queue[queue.length - 1][0] = delta;
		queue[queue.length - 1][1] = amount;
		this.netChange += delta;
	}
	
	public void remove(int index) {
		float[][] temp = Arrays.copyOf(queue, queue.length);
		queue = new float[queue.length - 1][2];
		System.arraycopy(temp, 0, queue, 0, index);
		System.arraycopy(temp, index + 1, queue, index, queue.length - index);
	}
	
	public float getNetChange() {
		float change = 0;
		for(float[] effect : queue) {
			change += effect[1] > effect[0] ? effect[0] : effect[1];
		}
		update();
		return change;
	}
	
	public void reset() {
		queue = new float[0][2];
	}
	
	private void update() {
		for(int i = 0; i < queue.length; i++) {
			queue[i][1] -= queue[i][1] > queue[i][0] ? queue[i][0] : queue[i][1];			
			if((int) queue[i][1] <= 0.0F) {
				remove(i);
				--i;
			}
		}
	}
	
	private float netChange = 0;
	
	private float[][] queue;
}