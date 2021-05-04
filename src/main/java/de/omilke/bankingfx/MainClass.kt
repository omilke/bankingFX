package de.omilke.bankingfx

object MainClass {

    @JvmStatic
    fun main(args: Array<String>) {

        //relay to a second "Main Class" in order to allow JavaFX fat jar packaging by hacking the Main Class check for JavaFX
        BankingFxApplication.main(args)
    }
}