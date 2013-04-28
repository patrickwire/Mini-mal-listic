package com.gamelab.mmi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class WetWiperTool extends Tool {
	
	private Vector2 lastDrawPos;

	public WetWiperTool(Map map) {
		super(map);
		lastDrawPos = new Vector2();
	}

	@Override
	public void draw(Vector2 curPos, Vector2 lastPos, float radius,
			float distance) {
		curDistanceUntilDraw -= distance;
		
		if (curDistanceUntilDraw > 0) return;
		
		curDistanceUntilDraw = 0.3f*radius;
		
		int PixelRadius = (int)(radius/2.0f);
		
		int r = (int) radius;
		for (int x = -r; x <= r; x+= 1) {
			for (int y = -r; y <= r; y+= 1) {
				if (x * x + y * y <= r * r) {
					int pX = (int) (curPos.x + x);
					int pY = (int) (curPos.y + y);
					
					int pLastX = (int) (lastDrawPos.x);
					int pLastY = (int) (lastDrawPos.y);
					
					Color lastAVGColor = getAverageArroundPixel(pLastX, pLastY, (int) 0.5f * PixelRadius);
					
					int value = pixmapHelper.pixmap.getPixel(pX, Gdx.graphics.getHeight() - pY);
					Color valColor = new Color();
					Color.rgba8888ToColor(valColor, value);
					
					valColor.r = (0.5f * (valColor.r + lastAVGColor.r));
					valColor.g = (0.5f * (valColor.g + lastAVGColor.g));
					valColor.b = (0.5f * (valColor.b + lastAVGColor.b));
					
					pixmapHelper.pixmap.drawPixel(pX, Gdx.graphics.getHeight() -pY, Color.rgba8888(valColor));
					
					map.touchPixel(pX, pY);					
				}							
			}			
		}
		
		lastDrawPos = curPos.cpy();
		
		pixmapHelper.reload();
		

	}

	private Color getAverageArroundPixel(int _x, int _y, int pixelRadius) {
		Color average = new Color();
		Color.rgba8888ToColor(average, 0);
		average.a = 1;

		for (int x = _x-pixelRadius; x <= _x+pixelRadius; x++) {
				for (int y = _y-pixelRadius; y <= _y+pixelRadius; y++) {
					int value = pixmapHelper.pixmap.getPixel(x, Gdx.graphics.getHeight() - y);
					Color valColor = new Color();
					Color.rgba8888ToColor(valColor, value);
					average.r += valColor.r;
					average.g += valColor.g;
					average.b += valColor.b;
			}			
		}
		float num = ((2*pixelRadius)+1) * ((2*pixelRadius)+1);
		average.r /= num;
		average.g /= num;
		average.b /= num;
		return average;
	}
}
