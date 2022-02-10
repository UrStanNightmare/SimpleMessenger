package ru.ao.simplemessenger.client.utils;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class AnchorManager {
    private AnchorManager() {
    }

    public static void setAnchors(Node anchorTarget, Double topAnchor, Double leftAnchor, Double bottomAnchor, Double rightAnchor) {
        AnchorPane.setTopAnchor(anchorTarget, topAnchor);
        AnchorPane.setLeftAnchor(anchorTarget, leftAnchor);
        AnchorPane.setBottomAnchor(anchorTarget, bottomAnchor);
        AnchorPane.setRightAnchor(anchorTarget, rightAnchor);
    }
}
