/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.karaoke;


public class CdgScrollCopy
{
	public static final int WIDTH = 300;

	public static final int HEIGHT = 216;

	/**
	 * @return true if a scroll is done, false if only an offset is done
	 */
	public static boolean scroll(byte data[], byte pixels[])
	{
		byte[] trash = new byte[12 * WIDTH]; // max size
		// byte color;
		byte hcmd;
		byte vcmd;
		byte hOffset;
		byte vOffset;
		byte h = 0, v = 0;
		boolean ret = false;

		// Only 4 lower bits are used
		// color = (byte) ((data[0] & 0x0F) + 2);

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
				// memcpy(trash,&pixels[(i*WIDTH)],h);
				System.arraycopy(pixels, (i * WIDTH), trash, 0, h);
				// memmove(&pixels[(i*WIDTH)],&pixels[(i*WIDTH)+h],WIDTH-h);
				System.arraycopy(pixels, (i * WIDTH) + h, pixels, (i * WIDTH), WIDTH - h);
				// memcpy(&pixels[(i*WIDTH)+WIDTH-h],trash,h);
				System.arraycopy(trash, 0, pixels, (i * WIDTH) + WIDTH - h, h);
			}
		}
		else
		{
			h *= -1;
			for (int i = 0; i < HEIGHT; i++)
			{
				// memcpy(trash,&pixels[(i*WIDTH)+WIDTH-h],h);
				System.arraycopy(pixels, (i * WIDTH) + WIDTH - h, trash, 0, h);
				// memmove(&pixels[(i*WIDTH)+h],&pixels[(i*WIDTH)],WIDTH-h);
				System.arraycopy(pixels, (i * WIDTH), pixels, (i * WIDTH) + h, WIDTH - h);
				// memcpy(&pixels[(i*WIDTH)],trash,h);
				System.arraycopy(trash, 0, pixels, (i * WIDTH), h);

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
			// memcpy(trash,pixels,WIDTH*(v));
			System.arraycopy(pixels, 0, trash, 0, WIDTH * (v));
			// memmove(pixels,&pixels[(v)*WIDTH],WIDTH*(HEIGHT-v));
			System.arraycopy(pixels, (v) * WIDTH, pixels, 0, WIDTH * (HEIGHT - v));
			// memcpy(&pixels[(HEIGHT-v)*WIDTH],trash,WIDTH*(v));
			System.arraycopy(trash, 0, pixels, (HEIGHT - v) * WIDTH, WIDTH * (v));
		}
		else
		{
			v *= -1;
			// memcpy(trash,&pixels[(HEIGHT-v)*WIDTH],WIDTH*(v));
			System.arraycopy(pixels, (HEIGHT - v) * WIDTH, trash, 0, WIDTH * (v));
			// memmove(&pixels[(v)*WIDTH],pixels,WIDTH*(HEIGHT-v));
			System.arraycopy(pixels, 0, pixels, (v) * WIDTH, WIDTH * (HEIGHT - v));
			// memcpy(pixels,trash,WIDTH*(v));
			System.arraycopy(trash, 0, pixels, 0, WIDTH * (v));
		}
		return ret;
	}
}

