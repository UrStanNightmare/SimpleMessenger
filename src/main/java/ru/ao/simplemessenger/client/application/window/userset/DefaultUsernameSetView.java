package ru.ao.simplemessenger.client.application.window.userset;

import ru.ao.simplemessenger.client.defaults.DefaultView;
import ru.ao.simplemessenger.client.handlers.LogInButtonPressedHandler;
import ru.ao.simplemessenger.transfer.UserStatus;

public interface DefaultUsernameSetView extends DefaultView {
    void registerLogInButtonPressedHandler(LogInButtonPressedHandler handler);

    void setStatusLabelsVisibility(boolean isVisible);

    void setInputComponentsDisableState(boolean state);

    void updateStatusLabel(UserStatus status);

    void highlightUsernameField(boolean isHighlighted);

    void highlightPasswordField(boolean isHighlighted);

    void cleanUserData();

    String getSpecifiedUsername();
}
