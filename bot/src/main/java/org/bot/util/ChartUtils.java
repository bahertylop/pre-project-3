package org.bot.util;

import org.dto.CarPositionPriceDto;
import org.knowm.xchart.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ChartUtils {

    private static final int CHART_WIDTH = 800;
    private static final int CHART_HEIGHT = 600;
    private static final int X_AXIS_LABEL_ROTATION = 90;
    private static final double Y_AXIS_MIN_VALUE = 0;

    public static InputFile getChartByPrices(List<CarPositionPriceDto> prices) throws IOException {
        XYChart chart = new XYChartBuilder()
                .width(CHART_WIDTH).height(CHART_HEIGHT)
                .title("График медианной цены на автомобиль")
                .xAxisTitle("Дата")
                .yAxisTitle("Цена")
                .build();

        chart.addSeries("Цены",
                prices.stream()
                        .map(CarPositionPriceDto::getDate)
                        .map((localDate) -> Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                        .collect(Collectors.toList()),
                prices.stream()
                        .map(CarPositionPriceDto::getPrice)
                        .collect(Collectors.toList())
        );

        chart.getStyler().setDatePattern("dd-MM-yyyy");
        chart.getStyler().setXAxisLabelRotation(X_AXIS_LABEL_ROTATION);
        chart.getStyler().setYAxisDecimalPattern("#");

        chart.getStyler().setYAxisMin(Y_AXIS_MIN_VALUE);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitmapEncoder.saveBitmap(chart, baos, BitmapEncoder.BitmapFormat.JPG);

        return new InputFile(new ByteArrayInputStream(baos.toByteArray()), "chart.jpg");
    }
}
