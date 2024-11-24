package me.theentropyshard.xkcdbot;

import me.theentropyshard.xkcdbot.xkcd.Comic;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class XkcdBot implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;

    public XkcdBot(String botToken) {
        this.telegramClient = new OkHttpTelegramClient(botToken);
    }

    @Override
    public void consume(Update update) {
        if (!update.hasMessage()) {
            return;
        }

        Message message = update.getMessage();

        if (!message.hasText()) {
            return;
        }

        String text = message.getText();

        if (text.charAt(0) != '/') {
            return;
        }

        this.handleCommand(message.getChatId().toString(), this.preprocessCommand(text.substring(1)));
    }

    private String[] preprocessCommand(String command) {
        String[] rawParts = command.split("\\s");

        List<String> list = new ArrayList<>();

        for (String part : rawParts) {
            String s = part.trim();

            if (s.isEmpty()) {
                continue;
            }

            list.add(s);
        }

        return list.toArray(new String[0]);
    }

    private void handleCommand(String chatId, String[] parts) {
        if (parts.length == 0) {
            return;
        }

        if (parts.length == 1) {
            if (parts[0].equals("xkcd")) {
                this.sendError(chatId, "Expected comic id parameter for 'xkcd' command! Usage: /xkcd <comic id>");
            }

            return;
        }

        if (parts.length > 2) {
            this.sendError(chatId, "Too many parameters for 'xkcd' command! Usage: /xkcd <comic id>");

            return;
        }

        int comicId;

        try {
            comicId = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            this.sendError(chatId, "Could not parse '" + parts[1] + "' to integer");

            return;
        }

        if (comicId <= 0) {
            this.sendError(chatId, "Number '" + comicId + "' is not a valid comic id");

            return;
        }

        Main.xkcdApi.getComic(comicId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Comic> call, Response<Comic> response) {
                int code = response.code();

                if (code != 200) {
                    if (code == 404) {
                        XkcdBot.this.sendError(chatId, "Could not find comic by id '" + comicId + "'");
                    } else {
                        XkcdBot.this.sendError(chatId, "Unknown error occurred. Code: " + code + "");
                    }

                    return;
                }

                Comic comic = response.body();

                if (comic == null) {
                    XkcdBot.this.sendError(chatId, "Could not deserialize Comic object. Ask developer to check the code");

                    return;
                }

                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(comic.getImg()));

                try {
                    XkcdBot.this.telegramClient.execute(sendPhoto);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Comic> call, Throwable throwable) {
                XkcdBot.this.sendError(chatId, "Could not get comic by id '" + comicId + "'. " + "Reason: " + throwable.getMessage());
            }
        });
    }

    private void sendError(String chatId, String message) {
        this.sendTextMessage(chatId, "Error: " + message);
    }

    private void sendTextMessage(String chatId, String message) {
        try {
            XkcdBot.this.telegramClient.execute(new SendMessage(chatId, message));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
