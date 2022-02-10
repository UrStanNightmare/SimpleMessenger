package ru.ao.simplemessenger.client.application.window.chat.sidebar;

import javafx.beans.value.ChangeListener;
import ru.ao.simplemessenger.client.enums.MenuTabs;

public interface DefaultSideBarView {
    void setUsername(String username);
    String getUsername();

    void registerMenuSelectionListener(ChangeListener<MenuTabs> listener);
}
