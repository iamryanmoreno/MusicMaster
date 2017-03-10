/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.karaoke;


import java.util.Arrays;

public class CdgMemoryPreset
{
	// Color refers to a color to clear the screen to. The entire screen should
	// be cleared to this color.
	// Only lower 4 bits are used, mask with 0x0F
	private static byte color;

	private static byte repeat;

	private static byte[] filler = new byte[14];

	public static boolean clearScreen(byte[] data, byte[] pixels)
	{
		// data is the 16 bytes array of the cdg chunk.

		// For these, only the lower 4 bits are used
		color = (byte) (data[0] & 0x0F);
		repeat = (byte) (data[1] & 0x0F);

		if (repeat != 0)
			return false;

		for (int i = 0; i < 14; i++)
		{
			filler[i] = (byte) (data[2 + i] & 0x3F);
		}
		clearScreen(pixels, color);
		return true;
	}

	public static void clearScreen(byte[] pixels, byte color)
	{
            Arrays.fill(pixels, color);
	}

}

