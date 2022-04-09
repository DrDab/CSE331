package setup;

import java.util.Random;

/** RandomHello selects and prints a random greeting. */
public class RandomHello
{
    private Random randomGenerator;
    private String[] greetings;

    public RandomHello()
    {
        randomGenerator = new Random();
        greetings = new String[]
                                {
                                    "Hello World",
                                    "Hola Mundo",
                                    "Bonjour Monde",
                                    "Hallo Welt",
                                    "Ciao Mondo"
                                };
    }

    /**
     * Prints a random greeting to the console.
     *
     * @param args command-line arguments (ignored)
     */
    public static void main(String[] args)
    {
        RandomHello randomHello = new RandomHello();
        System.out.println(randomHello.getGreeting());
    }

    /** @return a greeting, randomly chosen from five possibilities */
    public String getGreeting()
    {
        return greetings[randomGenerator.nextInt(greetings.length)];
    }
}