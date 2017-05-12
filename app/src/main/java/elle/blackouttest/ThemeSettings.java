package elle.blackouttest;

/**
 * Created by Elle on 5/12/2017 at 9:57 AM.
 */

class ThemeSettings {
    private static ThemeSettings instance;
    private int currentTheme;
    final private int MAIN_THEME = R.style.AppTheme;
    final private int TEST_THEME = R.style.TestTheme;
    final private int NIGHT_THEME = R.style.NighVision;

    static void initInstance(){
        if(instance == null){
            instance = new ThemeSettings();
        }
    }

    static ThemeSettings getInstance(){
        return instance;
    }

    private ThemeSettings(){
        currentTheme = MAIN_THEME;
    }

    int getCurrentTheme(){
        return currentTheme;
    }

    void setCurrentTheme(int themeIn){
        currentTheme = themeIn;
    }

    void findNextTheme(){
        if(currentTheme == MAIN_THEME){
            currentTheme = TEST_THEME;
        }else if(currentTheme == TEST_THEME){
            currentTheme = NIGHT_THEME;
        }else if(currentTheme == NIGHT_THEME){
            currentTheme = MAIN_THEME;
        }
    }
}
