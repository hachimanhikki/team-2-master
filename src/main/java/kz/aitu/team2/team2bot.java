package kz.aitu.team2;

import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.lyrics.Lyrics;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class team2bot extends TelegramLongPollingBot {

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            setButton(sendMessage);
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendText(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/start":
                    sendText(message, "Lyrics BOT, Telegram BOT that helps you to find the lyrics of the songs you want in a quick manner! Helping you find correct lyrics to the songs you want, simply, and efficiently !\n\n"+
                            "In order to find lyrics, please enter artist name and song name in one line.");
                    break;
                case "/help":
                    sendMsg(message, "OMG, what is so difficult about entering artist name and song name in one line ?");
                    break;
                default:
                    try {
                        lyrics(message);
                    } catch (MusixMatchException e) {
                        sendMsg(message,"OOPS! Apparently, you entered non-existing song, type again please.");
                    }
            }
        }

    }

    public void lyrics(Message message) throws MusixMatchException {
        String apiKey = "b7ff35e9c9b721d945b105274c68f5a3";
        MusixMatch musixMatch = new MusixMatch(apiKey);

        String trackName = message.getText();
        String artistName = message.getText();

        Track track = musixMatch.getMatchingTrack(trackName, artistName);
        TrackData data = track.getTrack();
        int trackID = data.getTrackId();
        Lyrics lyrics = musixMatch.getLyrics(trackID);
        sendText(message,"Artist Name : " + data.getArtistName() +
                "\n\nAlbum Name : " + data.getAlbumName() +
                "\n\nTrack Name : " + data.getTrackName() +
                "\n\nLyrics: \n\n" + lyrics.getLyricsBody());
    }


    public void setButton(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/help"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }


    public String getBotUsername() {
        return "Lyrics BOT";
    }

    public String getBotToken() {
        return "1032730486:AAHyeJd-Kv2hgOA1_Zd8EpPX7pTxJQRTmD8";
    }
}
