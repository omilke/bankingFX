package de.omilke.bankingfx;

public class MainClass {

    public static void main(final String[] args) {

        //relay to a second "Main Class" in order to allow JavaFX fat jar packaging by hacking the Main Class check for JavaFX
        BankingFxApplication.main(args);
    }

}
