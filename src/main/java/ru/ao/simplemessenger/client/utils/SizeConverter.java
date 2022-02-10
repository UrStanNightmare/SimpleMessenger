package ru.ao.simplemessenger.client.utils;

public class SizeConverter {
    private final double designWidth;
    private final double designHeight;

    private final double appWidth;
    private final double appHeight;

    public SizeConverter(double appWidth, double appHeight, double designWindowWidth, double designWindowHeight) {
        this.appWidth = appWidth;
        this.appHeight = appHeight;

        this.designWidth = designWindowWidth;
        this.designHeight = designWindowHeight;
    }

    public double convertFromWidth(double designElementWidth) {
        return designElementWidth * this.appWidth / this.designWidth;
    }

    public double convertFromHeight(double designElementHeight) {
        return designElementHeight * this.appHeight / this.designHeight;
    }

    public double getAppWidth() {
        return this.appWidth;
    }

    public double getAppHeight() {
        return this.appHeight;
    }
}
