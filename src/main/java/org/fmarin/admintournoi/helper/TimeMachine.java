package org.fmarin.admintournoi.helper;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeMachine {

    private static ZoneId zoneId = ZoneId.of("Europe/Paris");
    private static Clock clock = Clock.system(zoneId);

    public static LocalDateTime now() {
        return LocalDateTime.now(getClock());
    }

    public static void useFixedClockAt(LocalDateTime date){
        clock = Clock.fixed(date.atZone(zoneId).toInstant(), zoneId);
    }

    private static Clock getClock() {
        return clock ;
    }
}