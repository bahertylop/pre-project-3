package org.bot.api;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "api")
public class ApiProperties {

    private ApiUrl url;

    private Constraint constraint;

    @Data
    public static class ApiUrl {
        private String signIn;
        private String refresh;
        private String getProfile;
        private String getCarPositions;
        private String getCarPosition;
        private String createCarPosition;
        private String getBrands;
        private String getModels;
    }

    @Data
    public static class Constraint {
        private int minYearFrom;

        private int maxMileage;
    }

}
