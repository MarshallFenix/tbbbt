package service;

import config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class WriteData {
    BotConfig botConfig = new BotConfig();
    public void writerMethod(String textToWrite, Update update) throws IOException {
        String name = update.getMessage().getChat().getUserName();
        File file = new File(botConfig.getHistoryPath() + name + ".txt");
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(textToWrite);
            writer.append("\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


        }
    }

