package io.c_project.tgbot0;

import config.*;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import model.CurrencyModel;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.CurrencyService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;


@Component
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private static final ChatGPT CHAT_GPT = new ChatGPT("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");
    private final BotConfig botConfig;
    private WhatTypeOfMessage type = new WhatTypeOfMessage();


    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {


            if (update.hasMessage() && update.getMessage().hasText()) {
                String messageText = update.getMessage().getText() + " ";
                String name = update.getMessage().getChat().getFirstName();
                long chatId = update.getMessage().getChatId();
                String command = messageText.substring(0, messageText.indexOf(" "));


                System.out.println(messageText);
                System.out.println("0." + WhatTypeOfMessage.contextMap.get(chatId) + ": " + chatId );
                if(WhatTypeOfMessage.contextAddUser(chatId, "default").equals("default")){
                    switch (command) {
                        case "/start":
                            startCommandWithReplyKeyBoard(chatId,"Hi, " + name
                                    + ", nice to meet you!" + "\n" +
                                    "I am capable to do several actions." + "\n" +
                                    "If interested, send"+" /help");
                            break;
                        case "/gpt":
                            WhatTypeOfMessage.changeTypeForUser(chatId, "gpt");
                            sendMessage(chatId,"To cancel talk with ChatGPT, send /stop " +
                                    "\nAsk your quest:");

                            break;
                        case "/currency":
                            WhatTypeOfMessage.changeTypeForUser(chatId,"currency");
                            currencyCommandRecieved(chatId, update.getMessage().getChat().getFirstName());
                            System.out.println("1." + WhatTypeOfMessage.contextMap.get(chatId) + ": " + chatId );

                            break;
                        case "/help":
                            helpCommandRecieved(chatId);
                            break;
                        case "/myid":
                            System.out.println(name+ ": " + update.getMessage().getChat().getId());
                            sendMessage(chatId, "Your Telegram ID: " +
                                    update.getMessage().getChat().getId());
                            break;
                        case "/pictures":
                            picturesCommandWithInlineKeyboard(chatId);
                            break;

                    }


                }
                else if(WhatTypeOfMessage.contextAddUser(chatId, "default").equals("currency")) {
                    System.out.println("2. " + WhatTypeOfMessage.contextMap.get(chatId) + ": " + chatId);
                    searchCurrencyInApi(command,chatId);
                    WhatTypeOfMessage.changeTypeForUser(chatId,"default");

                    System.out.println("After sending:" + WhatTypeOfMessage.contextMap.get(chatId) + ": " + chatId);

                }
                else if (WhatTypeOfMessage.contextAddUser(chatId, "default").equals("gpt")) {
                    if(!command.equals("/stop"))
                    gptCommandRecieved(chatId,update);
                    else {
                        WhatTypeOfMessage.changeTypeForUser(chatId,"default");
                        sendMessage(chatId,"You stopped the discussion");
                    }
                }

            }

        if(update.hasCallbackQuery()){
// то бот совершает определенные действия
// (в моем случае – отправляет пользователю картинки
// или перенаправляет его на страницу в Интернете)
            String call_data = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            File pathPic1 = new File("/Users/macbookair/Desktop/tgbot0 /src/main/164552.jpg");
            File pathPic2 =new File("/Users/macbookair/Desktop/tgbot0 /src/main/1648249939_8-kartinkof-club-p-kachok-muzhik-mem-8.jpg");
            if(call_data.equals("1")){
                sendPhoto(String.valueOf(chatId),"Mem 1", pathPic1);
            }
            else if(call_data.equals("2")){
                sendPhoto(String.valueOf(chatId),"Mem 2", pathPic2);
            }

        }

    }

//    private void startCommandReceived(Long chatId, String name) {
//        String answer = "Hi, " + name + ", nice to meet you!" + "\n" +
//                "I am capable to do several actions." + "\n" +
//                "If interested, send"+" /help";
//
//        sendMessage(chatId, answer);
//    }
    private void gptCommandRecieved(Long chatId, Update update){
        try{
                sendMessage(chatId, CHAT_GPT.askChatGPT(update.getMessage().getText()));
            }

        catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    private void currencyCommandRecieved(Long chatId, String name){
        String answer = name +
                ". Enter the currency whose official exchange rate" + "\n" +
                "you want to know in relation to BYN." + "\n" +
                "For example: USD";
        sendMessage(chatId, answer);



    }
    private void helpCommandRecieved(Long chatId){
        String answer = "1. /gpt — sends your requests to ChatGPT."+
                "\n" + "2. /currency — shows actual rate of "+
                "\n" + "every currency to BYN." +"\n"+ "3. /myid — shows your Telegram id "+
                "\n"+"4. /pictures — shows you some pictures, fr fr";
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }
    private void startCommandWithReplyKeyBoard(Long chatId, String textToSend){
        //отправляем сообщение пользователю со встроенной ReplyKeyboard
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        sendMessage.setReplyMarkup(ReplyKeyboard_SuckMyDick.suckMyDickMethod(chatId,textToSend));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }
    public void sendPhoto(String chatId, String photoCaption, File photoFile) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        //sendPhoto.setCaption(photoCaption);
        sendPhoto.setPhoto(new InputFile(photoFile));

        try {
            execute(sendPhoto); // Отправляем фотографию
        } catch (TelegramApiException e) {
            System.out.println("Error sending photo: " + e.getMessage());
        }
    }
    private void picturesCommandWithInlineKeyboard(Long chatId){
        //отправляем сообщение пользователю со встроенной InlineKeyboard
        try {
            execute(InlineKeyboard_Pic.pictureInlineKBMethod(chatId));
        } catch (TelegramApiException e) {
        }
    }

    private void searchCurrencyInApi(String command, Long chatId) {
        String currency = null;
        try {
            CurrencyModel currencyModel = new CurrencyModel();

            System.out.println(command);
            currency = CurrencyService.getCurrencyRate(command, currencyModel);


        } catch (IOException e) {
            sendMessage(chatId, "We have not found such a currency." + "\n" +
                    "Enter the currency whose official exchange rate" + "\n" +
                    "you want to know in relation to BYN." + "\n" +
                    "For example: USD");
        } catch (ParseException e) {
            throw new RuntimeException("Unable to parse date");
        }
        sendMessage(chatId, currency);
    }
    private void sleep(){
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}






