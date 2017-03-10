/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.basicplayer;


import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;


public abstract class JavaSoundLyricsListener implements MetaEventListener {
    
    public static final int LYRIC = 1;
    public static final int COPYRIGHT_NOTICE = 2;
    public static final int TRACK_NAME = 3;
    
    // / Various meaningful characters in Karaoke files
    public static final char NEW_LINE = '/';
    public static final char CLEAR_SCREEN = '\\';
    public static final char META_INFO = '@';
    public static final char WEIRD_META = (char) 9;
    public static final char SONG_INFO = 'I';
    public static final char TITLE = 'T';
    public static final char VERSION = 'V';
    public static final char LANGUAGE = 'L';
    public static final char KARAOKE = 'K';
    
    
    public void meta(MetaMessage meta) {
        int metaType = meta.getType();
        switch (metaType) {
            case (LYRIC):
                if (meta.getData().length != 0) {
                    String lyric = new String(meta.getData());
                    char firstChar = lyric.charAt(0);
                    switch (firstChar) {
                        case CLEAR_SCREEN:
                            clearScreen();
                            lyric = lyric.substring(1);
                            outputLyric(lyric);
                            break;
                        case NEW_LINE:
                            newLine();
                            lyric = lyric.substring(1);
                            outputLyric(lyric);
                            break;
                        case META_INFO:
                            char nextChar = lyric.charAt(1);
                            switch (nextChar) {
                                case SONG_INFO:
                                    String songInfo = lyric.substring(2);
                                    songInfo(songInfo);
                                    break;
                                case TITLE:
                                    String title = lyric.substring(2);
                                    outputTitle(title);
                                    break;
                                case VERSION:
                                    String version = lyric.substring(2);
                                    outputVersion(version);
                                    break;
                                case LANGUAGE:
                                    String language = lyric.substring(2);
                                    outputLanguage(language);
                                    break;
                                case KARAOKE:
                                    String karaokeMess = lyric.substring(2);
                                    outputKaraokeMess(karaokeMess);
                                    break;
                                default:
                                    System.err.println("UNKNOWN  message " + nextChar + " == " + (int) nextChar + " :: " + lyric);
                            };
                            break;
                        case WEIRD_META:
                            // / Some songs like Sailor Moon start with a 9 ...
                            // / Not sure why...
                            break;
                        default:
                            outputLyric(lyric);
                    };
                };
                break;
            case COPYRIGHT_NOTICE:
                break;
            case TRACK_NAME:
                break;
            default:
        }
    };

    public abstract void clearScreen();
    public abstract void newLine();
    public abstract void outputLyric(String lyric);
    public void songInfo(String info) {
        System.out.println(info);
    };
    
    public void outputTitle(String title) {
		System.out.println("TITLE: " + title);
    };
    
    public void outputVersion(String version) {
        System.out.println("VERSION: " + version);
    };
    
    public void outputLanguage(String language) {
        System.out.println("LANGUAGE: " + language);
    };

    public void outputKaraokeMess(String message) {
        System.out.println("KARAOKE: " + message);
    };
};
