/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.karaoke;


import java.util.Arrays;


public class CdgBorderPreset
{
	static byte color;

	static final int WIDTH = 300;

	// byte []filler = new byte[15];

	public static void drawBorder(byte[] data, byte[] pixels)
	{
		// data is the 16 bytes array of the cdg chunk.
		// For this one, only the lower 4 bits are used
		color = (byte) (data[0] & 0x0F);

		Arrays.fill(pixels, 0, 12 * WIDTH, color); // top
		Arrays.fill(pixels, 204 * WIDTH, (204 * WIDTH) + (12 * WIDTH), color); // bottom

		for (int i = 12; i < 204; i++)
		{ // sides
			Arrays.fill(pixels, i * WIDTH, (i * WIDTH) + 6, color); // left
			Arrays.fill(pixels, i * WIDTH + 294, ((i * WIDTH) + 294) + 6, color); // right
		}
	}
}
