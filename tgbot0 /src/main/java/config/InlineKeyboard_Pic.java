package config;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboard_Pic {
    public static SendMessage pictureInlineKBMethod(Long chatId){
        //создаём объект сообщения
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("In order to see the pic, press on the actual button\uD83C\uDF07!");
        //создаём объект встроенной клавиатуры
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        // создаем список списков кнопок, который впоследствии объединит ряды кнопок
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        // создаем список с кнопками для первого ряда
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        // создаем первую кнопку в ряду
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        // устанавливаем параметр текста на кнопке
        inlineKeyboardButton1.setText("photo №1");
        // устанавливаем параметр callback_data
        inlineKeyboardButton1.setCallbackData("1");
        // создаем по аналогии вторую кнопку в ряду
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setText("photo №2");
        inlineKeyboardButton2.setCallbackData("2");
// добавляем кнопки в первый ряд в том порядке,
// какой нам необходим. В рассматриваемом случае ряд будет содержать 2 кнопки,
// размер которых будет одинаково пропорционально растянут по ширине сообщения,
// под которым клавиатура располагается
        rowInline1.add(inlineKeyboardButton1);
        rowInline1.add(inlineKeyboardButton2);
// если необходимо в кнопку запрограммировать переход на адрес
// Интернет страницы, такой параметр устанавливается через setUrl(String s).
// При этом в приведенном примере ряд кнопок будет состоять всего из одной кнопки
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton3.setText("Переход на внешний сайт");
        inlineKeyboardButton3.setUrl("https://reactor.cc/post/5218934");

// устанавливаем url, указывая строковый параметр с адресом страницы
        inlineKeyboardButton3.setCallbackData("ПЕРЕХОД НА ВНЕШНИЙ САЙТ");

        rowInline2.add(inlineKeyboardButton3);

// настраиваем разметку всей клавиатуры
        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);

// добавляем встроенную клавиатуру в сообщение
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        return message;
    }
}
