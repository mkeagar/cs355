package cs355.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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

	public int[][] getPixels2D()
	{
		return this.pixels;
	}

	public int[] getPixelsSingleArray()
	{
		int[] pixels = new int[this.width * this.height];

		for (int j = 0, pixNum = 0; j < this.height; j++)
			for (int i = 0; i < this.width; i++)
			{
				pixels[pixNum] = this.pixels[i][j];
				pixNum++;
			}

		return pixels;
	}

	public void setPixels(int[][] pixels)
	{
		this.pixels = pixels;
	}

	public void changeBrightness(int deltaBrightness)
	{
		for (int i = 0, row = 0, col = 0; i < this.width * this.height; i++)
		{
			this.pixels[col][row] += deltaBrightness;
			if (this.pixels[col][row] < 0) this.pixels[col][row] = 0;
			if (this.pixels[col][row] > 255) this.pixels[col][row] = 255;

			col++;
			if (col == width)
			{
				col = 0;
				row++;
			}
		}
	}

	public Image2D deepCopy()
	{
		int[][] copy = new int[this.width][this.height];

		for (int i = 0, row = 0, col = 0; i < this.width * this.height; i++)
		{
			copy[col][row] = this.pixels[col][row];
			col++;
			if (col == this.width)
			{
				col = 0;
				row++;
			}
		}

		return new Image2D(copy);
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

	public void print()
	{
		for (int j = 0; j < this.height; j++)
			for (int i = 0; i < this.width; i++)
			{
				System.out.print(this.pixels[i][j] + " ");
				if (i == width - 1) System.out.println();
			}
	}

	public void changeContrast(int contrastAmountNum)
	{
		int[][] newPixels = new int[this.width][this.height];

		for (int i = 0, row = 0, col = 0; i < this.width * this.height; i++)
		{
			newPixels[col][row] = (int) Math.round(Math.pow((contrastAmountNum + 100.0) / 100.0, 4.0) * (this.pixels[col][row] - 128.0) + 128.0);
			if (newPixels[col][row] < 0) newPixels[col][row] = 0;
			if (newPixels[col][row] > 255) newPixels[col][row] = 255;
			
			col++;
			if (col == width)
			{
				col = 0;
				row++;
			}
		}

		this.pixels = newPixels;
	}

	public void medianBlur()
	{
		int[][] newPixels = new int[this.width][this.height];

		for (int i = 0, row = 0, col = 0; i < this.width * this.height; i++)
		{
			ArrayList<Double> temp = new ArrayList<Double>();

			if (row == 0)
			{
				if (col == 0)
				{
					temp.add((double) pixels[col][row]);
					temp.add((double) pixels[col + 1][row]);
					temp.add((double) pixels[col][row + 1]);
					temp.add((double) pixels[col + 1][row + 1]);
					Collections.sort(temp);
					newPixels[col][row] = (int) Math.round(this.findMedian(temp));
				}
				else if (col == this.width - 1)
				{
					temp.add((double) pixels[col - 1][row]);
					temp.add((double) pixels[col][row]);
					temp.add((double) pixels[col - 1][row + 1]);
					temp.add((double) pixels[col][row + 1]);
					Collections.sort(temp);
					newPixels[col][row] = (int) Math.round(this.findMedian(temp));
				}
				else
				{
					temp.add((double) pixels[col - 1][row]);
					temp.add((double) pixels[col][row]);
					temp.add((double) pixels[col + 1][row]);
					temp.add((double) pixels[col - 1][row + 1]);
					temp.add((double) pixels[col][row + 1]);
					temp.add((double) pixels[col + 1][row + 1]);
					Collections.sort(temp);
					newPixels[col][row] = (int) Math.round(this.findMedian(temp));
				}
			}
			else if (row == this.height - 1)
			{
				if (col == 0)
				{
					temp.add((double) pixels[col][row - 1]);
					temp.add((double) pixels[col + 1][row - 1]);
					temp.add((double) pixels[col][row]);
					temp.add((double) pixels[col + 1][row]);
					Collections.sort(temp);
					newPixels[col][row] = (int) Math.round(this.findMedian(temp));
				}
				else if (col == this.width - 1)
				{
					temp.add((double) pixels[col - 1][row - 1]);
					temp.add((double) pixels[col][row - 1]);
					temp.add((double) pixels[col - 1][row]);
					temp.add((double) pixels[col][row]);
					Collections.sort(temp);
					newPixels[col][row] = (int) Math.round(this.findMedian(temp));
				}
				else
				{
					temp.add((double) pixels[col - 1][row - 1]);
					temp.add((double) pixels[col][row - 1]);
					temp.add((double) pixels[col + 1][row - 1]);
					temp.add((double) pixels[col - 1][row]);
					temp.add((double) pixels[col][row]);
					temp.add((double) pixels[col + 1][row]);
					Collections.sort(temp);
					newPixels[col][row] = (int) Math.round(this.findMedian(temp));
				}
			}
			else
			{
				if (col == 0)
				{
					temp.add((double) pixels[col][row - 1]);
					temp.add((double) pixels[col + 1][row - 1]);
					temp.add((double) pixels[col][row]);
					temp.add((double) pixels[col + 1][row]);
					temp.add((double) pixels[col][row + 1]);
					temp.add((double) pixels[col + 1][row + 1]);
					Collections.sort(temp);
					newPixels[col][row] = (int) Math.round(this.findMedian(temp));
				}
				else if (col == this.width - 1)
				{
					temp.add((double) pixels[col - 1][row - 1]);
					temp.add((double) pixels[col][row - 1]);
					temp.add((double) pixels[col - 1][row]);
					temp.add((double) pixels[col][row]);
					temp.add((double) pixels[col - 1][row + 1]);
					temp.add((double) pixels[col][row + 1]);
					Collections.sort(temp);
					newPixels[col][row] = (int) Math.round(this.findMedian(temp));
				}
				else
				{
					temp.add((double) pixels[col - 1][row - 1]);
					temp.add((double) pixels[col][row - 1]);
					temp.add((double) pixels[col + 1][row - 1]);
					temp.add((double) pixels[col - 1][row]);
					temp.add((double) pixels[col][row]);
					temp.add((double) pixels[col + 1][row]);
					temp.add((double) pixels[col - 1][row + 1]);
					temp.add((double) pixels[col][row + 1]);
					temp.add((double) pixels[col + 1][row + 1]);
					Collections.sort(temp);
					newPixels[col][row] = (int) Math.round(this.findMedian(temp));
				}
			}

			if (newPixels[col][row] < 0) newPixels[col][row] = 0;
			if (newPixels[col][row] > 255) newPixels[col][row] = 255;

			col++;
			if (col == this.width)
			{
				col = 0;
				row++;
			}
		}

		this.pixels = newPixels;
	}

	public void uniformBlur()
	{
		int[][] newPixels = new int[this.width][this.height];

		for (int i = 0, row = 0, col = 0; i < this.width * this.height; i++)
		{
			if (row == 0)
			{
				if (col == 0)
				{
					newPixels[col][row] = (int) Math
							.round((this.pixels[col][row] + this.pixels[col + 1][row] + this.pixels[col + 1][row + 1] + this.pixels[col][row + 1]) / 4.0);
				}
				else if (col == this.width - 1)
				{
					newPixels[col][row] = (int) Math
							.round((this.pixels[col][row] + this.pixels[col - 1][row] + this.pixels[col - 1][row + 1] + this.pixels[col][row + 1]) / 4.0);
				}
				else
				{
					newPixels[col][row] = (int) Math.round((this.pixels[col][row] + this.pixels[col + 1][row] + this.pixels[col + 1][row + 1]
							+ this.pixels[col][row + 1] + this.pixels[col - 1][row + 1] + this.pixels[col - 1][row]) / 6.0);
				}
			}
			else if (row == this.height - 1)
			{
				if (col == 0)
				{
					newPixels[col][row] = (int) Math
							.round((this.pixels[col][row] + this.pixels[col + 1][row] + this.pixels[col + 1][row - 1] + this.pixels[col][row - 1]) / 4.0);
				}
				else if (col == this.width - 1)
				{
					newPixels[col][row] = (int) Math
							.round((this.pixels[col][row] + this.pixels[col - 1][row] + this.pixels[col - 1][row - 1] + this.pixels[col][row - 1]) / 4.0);
				}
				else
				{
					newPixels[col][row] = (int) Math.round((this.pixels[col][row] + this.pixels[col + 1][row] + this.pixels[col + 1][row - 1]
							+ this.pixels[col][row - 1] + this.pixels[col - 1][row - 1] + this.pixels[col - 1][row]) / 6.0);
				}
			}
			else
			{
				if (col == 0)
				{
					newPixels[col][row] = (int) Math.round((this.pixels[col][row - 1] + this.pixels[col + 1][row - 1] + this.pixels[col][row]
							+ this.pixels[col + 1][row] + this.pixels[col][row + 1] + this.pixels[col + 1][row + 1]) / 6.0);
				}
				else if (col == this.width - 1)
				{
					newPixels[col][row] = (int) Math.round((this.pixels[col - 1][row - 1] + this.pixels[col][row - 1] + this.pixels[col - 1][row]
							+ this.pixels[col][row] + this.pixels[col - 1][row + 1] + this.pixels[col][row + 1]) / 6.0);
				}
				else
				{
					newPixels[col][row] = (int) Math.round((this.pixels[col - 1][row - 1] + this.pixels[col][row - 1] + this.pixels[col + 1][row - 1]
							+ this.pixels[col - 1][row] + this.pixels[col][row] + this.pixels[col + 1][row] + this.pixels[col - 1][row + 1]
							+ this.pixels[col][row + 1] + this.pixels[col + 1][row + 1]) / 9.0);
				}
			}

			col++;
			if (col == this.width)
			{
				col = 0;
				row++;
			}
		}

		this.pixels = newPixels;
	}

	public void sharpen()
	{
		int[][] newPixels = new int[this.width][this.height];

		for (int i = 0, row = 0, col = 0; i < this.width * this.height; i++)
		{
			if (row == 0)
			{
				if (col == 0)
				{
					newPixels[col][row] = (int) Math.round((6 * this.pixels[col][row] - this.pixels[col + 1][row] - this.pixels[col][row + 1]) / 2.0);
				}
				else if (col == this.width - 1)
				{
					newPixels[col][row] = (int) Math
							.round((-this.pixels[col - 1][row] + 6 * this.pixels[col][row] - this.pixels[col][row + 1]) / 2.0);
				}
				else
				{
					newPixels[col][row] = (int) Math
							.round((-this.pixels[col - 1][row] + 6 * this.pixels[col][row] - this.pixels[col + 1][row] - this.pixels[col][row + 1]) / 2.0);
				}
			}
			else if (row == this.height - 1)
			{
				if (col == 0)
				{
					newPixels[col][row] = (int) Math
							.round((-this.pixels[col][row - 1] + 6 * this.pixels[col][row] - this.pixels[col + 1][row]) / 2.0);
				}
				else if (col == this.width - 1)
				{
					newPixels[col][row] = (int) Math
							.round((-this.pixels[col][row - 1] - this.pixels[col - 1][row] + 6 * this.pixels[col][row]) / 2.0);
				}
				else
				{
					newPixels[col][row] = (int) Math
							.round((-this.pixels[col][row - 1] - this.pixels[col - 1][row] + 6 * this.pixels[col][row] - this.pixels[col + 1][row]) / 2.0);
				}
			}
			else
			{
				if (col == 0)
				{
					newPixels[col][row] = (int) Math
							.round((-this.pixels[col][row - 1] + 6 * this.pixels[col][row] - this.pixels[col + 1][row] - this.pixels[col][row + 1]) / 2.0);
				}
				else if (col == this.width - 1)
				{
					newPixels[col][row] = (int) Math
							.round((-this.pixels[col][row - 1] - this.pixels[col - 1][row] + 6 * this.pixels[col][row] - this.pixels[col][row + 1]) / 2.0);
				}
				else
				{
					newPixels[col][row] = (int) Math.round((-this.pixels[col][row - 1] - this.pixels[col - 1][row] + 6 * this.pixels[col][row]
							- this.pixels[col + 1][row] - this.pixels[col][row + 1]) / 2.0);
				}
			}

			col++;
			if (col == this.width)
			{
				col = 0;
				row++;
			}
		}

		this.pixels = newPixels;
	}

	public void detectEdges()
	{
		int[][] newPixels = new int[this.width][this.height];

		for (int i = 0, row = 0, col = 0; i < this.width * this.height; i++)
		{
			float x;
			float y;

			if (row == 0)
			{
				if (col == 0)
				{
					x = (2 * this.pixels[col + 1][row] + this.pixels[col + 1][row + 1]) / 8.0f;
					y = (2 * this.pixels[col][row + 1] + this.pixels[col + 1][row + 1]) / 8.0f;
				}
				else if (col == this.width - 1)
				{
					x = (-2 * this.pixels[col - 1][row] - this.pixels[col - 1][row + 1]) / 8.0f;
					y = (this.pixels[col - 1][row + 1] + 2 * this.pixels[col][row + 1]) / 8.0f;
				}
				else
				{
					x = (-2 * this.pixels[col - 1][row] + 2 * this.pixels[col + 1][row] - this.pixels[col - 1][row + 1] + this.pixels[col + 1][row + 1]) / 8.0f;
					y = (this.pixels[col - 1][row + 1] + 2 * this.pixels[col][row + 1] + this.pixels[col + 1][row + 1]) / 8.0f;
				}
			}
			else if (row == this.height - 1)
			{
				if (col == 0)
				{
					x = (this.pixels[col + 1][row - 1] + 2 * this.pixels[col + 1][row]) / 8.0f;
					y = (-2 * this.pixels[col][row - 1] - this.pixels[col + 1][row - 1]) / 8.0f;
				}
				else if (col == this.width - 1)
				{
					x = (-this.pixels[col - 1][row - 1] - 2 * this.pixels[col - 1][row]) / 8.0f;
					y = (-this.pixels[col - 1][row - 1] - 2 * this.pixels[col][row - 1]) / 8.0f;
				}
				else
				{
					x = (-this.pixels[col - 1][row - 1] + this.pixels[col + 1][row - 1] - 2 * this.pixels[col - 1][row] + 2 * this.pixels[col + 1][row]) / 8.0f;
					y = (-this.pixels[col - 1][row - 1] - 2 * this.pixels[col][row - 1] - this.pixels[col + 1][row - 1]) / 8.0f;
				}
			}
			else
			{
				if (col == 0)
				{
					x = (this.pixels[col + 1][row - 1] + 2 * this.pixels[col + 1][row] + this.pixels[col + 1][row + 1]) / 8.0f;
					y = (-2 * this.pixels[col][row - 1] - this.pixels[col + 1][row - 1] + 2 * this.pixels[col][row + 1] + this.pixels[col + 1][row + 1]) / 8.0f;
				}
				else if (col == this.width - 1)
				{
					x = (-this.pixels[col - 1][row - 1] - 2 * this.pixels[col - 1][row] - this.pixels[col - 1][row + 1]) / 8.0f;
					y = (-this.pixels[col - 1][row - 1] - 2 * this.pixels[col][row - 1] + this.pixels[col - 1][row + 1] + 2 * this.pixels[col][row + 1]) / 8.0f;
				}
				else
				{
					x = (-this.pixels[col - 1][row - 1] + this.pixels[col + 1][row - 1] - 2 * this.pixels[col - 1][row] + 2
							* this.pixels[col + 1][row] - this.pixels[col - 1][row + 1] + this.pixels[col + 1][row + 1]) / 8.0f;
					y = (-this.pixels[col - 1][row - 1] - 2 * this.pixels[col][row - 1] - this.pixels[col + 1][row - 1]
							+ this.pixels[col - 1][row + 1] + 2 * this.pixels[col][row + 1] + this.pixels[col + 1][row + 1]) / 8.0f;
				}
			}

			newPixels[col][row] = (int) Math.round(Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0)));

			col++;
			if (col == width)
			{
				col = 0;
				row++;
			}
		}

		this.pixels = newPixels;
	}

	// array input must be sorted
	public double findMedian(ArrayList<Double> sortedInput)
	{
		int middle = sortedInput.size() / 2;
		if (sortedInput.size() % 2 == 1)
		{
			return sortedInput.get(middle);
		}
		else
		{
			return (sortedInput.get(middle - 1) + sortedInput.get(middle)) / 2.0;
		}
	}
}
