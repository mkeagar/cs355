package cs355.model;

import java.util.Arrays;

public class Image2D
{
	private int height, width;
	private int[][] pixels = null;

	public Image2D(int[][] pixels)
	{
		this.height = pixels[0].length;
		this.width = pixels.length;
		this.pixels = pixels;
	}

	public int getHeight()
	{
		return this.height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public int getWidth()
	{
		return this.width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int[][] getPixels()
	{
		return this.pixels;
	}

	public void setPixels(int[][] pixels)
	{
		this.pixels = pixels;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + Arrays.hashCode(pixels);
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Image2D other = (Image2D) obj;
		if (height != other.height) return false;
		if (!Arrays.deepEquals(pixels, other.pixels)) return false;
		if (width != other.width) return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Image2D [height=" + height + ", width=" + width + ", pixels=" + Arrays.toString(pixels) + "]";
	}

}
