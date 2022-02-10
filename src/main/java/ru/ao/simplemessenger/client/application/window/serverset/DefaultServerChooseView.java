package ru.ao.simplemessenger.client.application.window.serverset;

import ru.ao.simplemessenger.client.defaults.DefaultView;
import ru.ao.simplemessenger.client.handlers.ConnectButtonPressedHandler;

public interface DefaultServerChooseView extends DefaultView {
    void registerConnectButtonEventHandler(ConnectButtonPressedHandler handler);

    void highlightPortField(boolean isHighlighted);

    void highlightIpField(boolean isHighlighted);

    void setProgressIndicatorVisibility(boolean isVisible);

}
