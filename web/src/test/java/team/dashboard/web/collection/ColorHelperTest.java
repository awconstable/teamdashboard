package team.dashboard.web.collection;

import be.ceau.chart.color.Color;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ColorHelperTest
    {

    @Test
    public void generateSteppedBlueColor1Step()
        {
        Color color = ColorHelper.generateSteppedColor(1, new Color(71, 143, 255));
        assertThat(color.getR(), is(equalTo(71)));
        assertThat(color.getG(), is(equalTo(143)));
        assertThat(color.getB(), is(equalTo(225)));
        }

    @Test
    public void generateSteppedBlueColor8Steps()
        {
        Color color = ColorHelper.generateSteppedColor(8, new Color(71, 143, 255));
        assertThat(color.getR(), is(equalTo(71)));
        assertThat(color.getG(), is(equalTo(143)));
        assertThat(color.getB(), is(equalTo(15)));
        }

    @Test
    public void generateSteppedBlueColorLowerLimit()
        {
        Color color = ColorHelper.generateSteppedColor(9, new Color(71, 143, 255));
        assertThat(color.getR(), is(equalTo(71)));
        assertThat(color.getG(), is(equalTo(83)));
        assertThat(color.getB(), is(equalTo(10)));
        }
    }