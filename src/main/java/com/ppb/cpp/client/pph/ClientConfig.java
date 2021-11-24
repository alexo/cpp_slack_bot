package com.ppb.cpp.client.pph;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.PromotionApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Bean
    public PromotionApi getCient() {
        //default api client, configured based on promos.json
        var logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        var builder = new OkHttpClient.Builder().addInterceptor(logging);
        var apiClient = new ApiClient(builder.build());
        return new PromotionApi(apiClient);
    }
}
