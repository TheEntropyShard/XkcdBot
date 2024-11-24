package me.theentropyshard.xkcdbot;

import me.theentropyshard.xkcdbot.xkcd.XkcdApi;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {
    public static XkcdApi xkcdApi = new XkcdApi();

    public static void main(String[] args) {
        String botToken = System.getenv("BOT_TOKEN");

        if (botToken == null) {
            System.out.println("Environment variable BOT_TOKEN is not set");

            System.exit(1);
        }

        @SuppressWarnings("resource")
        TelegramBotsLongPollingApplication application = new TelegramBotsLongPollingApplication();

        try {
            application.registerBot(botToken, new XkcdBot(botToken));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
