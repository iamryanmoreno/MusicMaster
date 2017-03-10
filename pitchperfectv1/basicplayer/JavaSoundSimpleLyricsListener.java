
package pitchperfectv1.basicplayer;

public class JavaSoundSimpleLyricsListener extends JavaSoundLyricsListener{
    public void clearScreen() {
        System.out.println("\n\n");
    };
    
    public void newLine() {
        System.out.println("\n");
    };
    
    public void outputLyric(String lyric) {
        System.out.print(lyric);
    };
};
