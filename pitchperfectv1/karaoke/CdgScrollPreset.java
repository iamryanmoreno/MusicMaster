/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.karaoke;


import java.util.Arrays;

public class CdgScrollPreset
{
	public static final int WIDTH = 300;

	public static final int HEIGHT = 216;

	public static boolean scroll(byte data[], byte pixels[])
	{
		byte color;
		byte hcmd;
		byte vcmd;
		byte hOffset;
		byte vOffset;
		byte h = 0, v = 0;
		boolean ret = false;

		// Only 4 lower bits are used
		color = (byte) ((data[0] & 0x0F) + 2);
		// Only lower 6 bits are used
		hcmd = (byte) ((data[1] & 0x30) >> 4);
		vcmd = (byte) ((data[2] & 0x30) >> 4);
		hOffset = (byte) ((data[1] & 0x7) % 6);
		vOffset = (byte) ((data[2] & 0xF) % 12);

		switch (hcmd)
		{
			case 0:
				h = hOffset;
				break;
			case 2: // left
				h = 6;
				ret = true;
				break;
			case 1: // right
				h = -6;
				ret = true;
				break;
			default:
				break;
		}

		if (h > 0)
		{
			for (int i = 0; i < HEIGHT; i++)
			{
				// memmove(&pixels[(i*WIDTH)],&pixels[(i*WIDTH)+h],WIDTH-h);
				System.arraycopy(pixels, (i * WIDTH) + h, pixels, (i * WIDTH), WIDTH - h);

				// memset(&pixels[(i*WIDTH)+WIDTH-h],color,h);
				Arrays.fill(pixels, (i * WIDTH) + WIDTH - h, (i * WIDTH) + WIDTH, color);
			}
		}
		else
		{
			h *= -1;
			for (int i = 0; i < HEIGHT; i++)
			{
				// memmove(&pixels[(i*WIDTH)+h],&pixels[(i*WIDTH)],WIDTH-h);
				System.arraycopy(pixels, (i * WIDTH), pixels, (i * WIDTH) + h, WIDTH - h);

				// memset(&pixels[(i*WIDTH)],color,h);
				Arrays.fill(pixels, (i * WIDTH), (i * WIDTH) + h, color);
			}
		}

		switch (vcmd)
		{
			case 0:
				v = vOffset;
				break;
			case 2: // up
				v = 12;
				ret = true;
				break;
			case 1: // down
				v = -12;
				ret = true;
				break;
			default:
				break;
		}

		if (v > 0)
		{
			// memmove(pixels,&pixels[(v)*WIDTH],WIDTH*(HEIGHT-v));
			System.arraycopy(pixels, (v) * WIDTH, pixels, 0, WIDTH * (HEIGHT - v));

			// memset(&pixels[(HEIGHT-v)*WIDTH],color,WIDTH*(v));
			Arrays.fill(pixels, (HEIGHT - v) * WIDTH, (HEIGHT - v) * WIDTH + WIDTH * (v), color);
		}
		else
		{
			v *= -1;
			// memmove(&pixels[(v)*WIDTH],pixels,WIDTH*(HEIGHT-v));
			System.arraycopy(pixels, 0, pixels, (v) * WIDTH, WIDTH * (HEIGHT - v));
			// memset(pixels,color,WIDTH*(v));
			Arrays.fill(pixels, 0, WIDTH * (v), color);
		}
		return ret;
	}
};
