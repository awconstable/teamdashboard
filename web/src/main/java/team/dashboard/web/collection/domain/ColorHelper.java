package team.dashboard.web.collection.domain;

import be.ceau.chart.color.Color;

public class ColorHelper
    {

    public static final Color generateSteppedColor(int step, Color baseColor)
        {
        final int lowerLimit = 10;
        int blue = baseColor.getB() - (step * 30);
        int green = step > 8 ? baseColor.getG() - ((step - 7) * 30) : baseColor.getG();
        int lowerGreen = Math.max(green, lowerLimit);

        int lowerBlue = Math.max(blue, lowerLimit);

        Color color = new Color(baseColor.getR(), lowerGreen, lowerBlue);

        return color;
        }
    }
