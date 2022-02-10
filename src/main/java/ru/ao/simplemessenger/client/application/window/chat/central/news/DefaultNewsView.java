package ru.ao.simplemessenger.client.application.window.chat.central.news;

public interface DefaultNewsView{
    void addNews(String label, String text, String time);

    void removeNews(String label);

    void removeNews(int position);

    void cleanNews();
}
