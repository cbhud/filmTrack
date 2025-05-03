package me.cbhud.scheduler;

import io.quarkus.scheduler.Scheduled;
import me.cbhud.model.TimeResponse;
import me.cbhud.restclient.TimeClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

public class SchedulerUtility {

    @RestClient
    TimeClient timeClient;
    //@Scheduled(every ="1s")
    void increment(){
        TimeResponse time = timeClient.getTime("Europe/Amsterdam");
        System.out.println(time);
    }

    public void getTimeByIp(){



    }
}
