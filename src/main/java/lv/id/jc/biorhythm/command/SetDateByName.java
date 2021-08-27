package lv.id.jc.biorhythm.command;

import lv.id.jc.biorhythm.model.Context;

import java.time.LocalDate;
import java.util.Map;

public class SetDateByName extends DateCommand {

    private final Map<String, LocalDate> commandsMap;

    public SetDateByName(Context context) {
        super(context);
        commandsMap = Map.of(
                "today", LocalDate.now(),
                "now", LocalDate.now(),
                "epoch", LocalDate.EPOCH,
                "birthday", context.getBirthday(),
                "tomorrow", LocalDate.now().plusDays(1L),
                "after tomorrow", LocalDate.now().plusDays(2L),
                "yesterday", LocalDate.now().minusDays(1L),
                "before yesterday", LocalDate.now().minusDays(2L));
    }

    @Override
    public boolean test(String request) {
        final var date = commandsMap.get(request);
        if (date == null) {
            return false;
        }
        context.setDate(date);
        return true;
    }
}

